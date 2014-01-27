package com.iconmaster.aec.common.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.common.AetherCraft;

public class TileEntityAetherManipulator extends AetherCraftTileEntity implements
		ISidedInventory, IAetherStorage {
	public TileEntityAetherManipulator() {
		super();
		use0 = false;
		energyBlockType = AetherCraft.GUI_ID_MANIPULATOR;
		inventory = new ItemStack[55];
	}

	@Override
	public String getInvName() {
		return "gra.tileentityem";
	}

	public void handleAether() {
		calcMax();
		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 1; i < this.getSizeInventory(); i++) {
			boolean failed = false;
			currentStack = this.getStackInSlot(i);

			// ------------------- Consuming -------------------
			if (canConsume(topStack,currentStack)) {
				float stackEv;
				if (currentStack.getItem() instanceof IConsumeBehavior) {
					stackEv = ((IConsumeBehavior)currentStack.getItem()).getConsumeAV(currentStack);
				} else {
					stackEv = AVRegistry.getAV(currentStack);
				}
				stackEv *= Float.parseFloat(AetherCraft.getOptions("consumeprecision")) / 100.0f;
				//System.out.println("Consuming... ");
				if (stackEv+getAether()>max) {
					//System.out.println("Has more aether than we can hold!");
					boolean canSend = AetherNetwork.canSendAV(worldObj, xCoord, yCoord, zCoord, stackEv+getAether()-max);
					if (!canSend) {
						//System.out.println("Could not transfer!");
						//AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, stackEv-getAether()-left);
						failed = true;
					} else {
						AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, stackEv+getAether()-max);
						energy = max;
					}
				} else {
					energy += stackEv;
				}
				//System.out.println("Done Consuming! ");
				
				if (!failed) {
					if (currentStack.getItem() instanceof IConsumeBehavior) {
						ItemStack output =  ((IConsumeBehavior)currentStack.getItem()).consume(currentStack,inventory);
						if (output == null) {
							this.decrStackSize(i, 1);
						} else {
							int slot = this.getStackableSlot(output);
							if (slot > 0) {
								ItemStack newStack = this.getStackInSlot(slot);
								newStack.stackSize++;
							} else {
								slot = this.getEmptySlot();
								//newStack.stackSize = 1;
								this.setInventorySlotContents(slot, output);
							}
						}
					} else {
						this.decrStackSize(i, 1);
					}
					doneSomething = true;
				}
			}

			// ------------------- Producing -------------------
			if (canProduce(topStack)) {
				//System.out.println("Producing...");
				float av;
				if (topStack.getItem() instanceof IProduceBehavior) {
					av = ((IProduceBehavior)topStack.getItem()).getProduceAV(topStack);
				} else {
					av = AVRegistry.getAV(topStack);
				}
				if (getAether() - av < 0) {
					//System.out.println("Not enough AV!");
					boolean canGet = AetherNetwork.canRequestAV(worldObj, xCoord, yCoord, zCoord, av-getAether());
					if (!canGet) {
						//System.out.println("Couldn't get enough!");
						//AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, got);
						failed = true;
					} else {
						AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, av-getAether());
						this.energy = 0;
					}
				} else {
					energy -= av;
				}
				
				//System.out.println("Done Producing!");
				if (!failed) {
					ItemStack newStack = null;
					if (topStack.getItem() instanceof IProduceBehavior) {
						newStack = ((IProduceBehavior)topStack.getItem()).produce(topStack,this.inventory);
					}
					int slot = this.getStackableSlot(topStack);
					if (slot > 0) {
						newStack = this.getStackInSlot(slot);
						newStack.stackSize++;
						doneSomething = true;
					} else {
						slot = this.getEmptySlot();
						if (newStack == null) {
							newStack = new ItemStack(topStack.itemID,1,topStack.getItemDamage());
						}
						//newStack.stackSize = 1;
						this.setInventorySlotContents(slot, newStack);
					}
					
					doneSomething = true;
				}
				
				
			}
			
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("instantconsume")) && i >= 9 && doneSomething) {
				this.sync();
				return;
			}
		}
		if (doneSomething) {
			this.sync();
			return;
		}
	}

	public void calculateProgress() {
		ItemStack stack = this.getStackInSlot(0);

		if (stack != null) {
			this.progress = (int) ((float) this.getPossibleAether()
					/ (float) AVRegistry.getAV(stack) * 100.0f);

			if (this.progress > 100) {
				this.progress = 100;
			}
		} else {
			this.progress = 0;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 0:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
			// Top
		case 1:
			return new int[] { 0 };
			// Sides
		default:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void calcMax() {
		if (max == 0) {
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage")))*(Math.pow(2,getMetadata()*2)));
		}
	}
}
