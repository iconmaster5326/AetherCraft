package com.iconmaster.aec.tileentity;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.*;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class TileEntityAetherManipulator extends AetherCraftTileEntity implements
		ISidedInventory, IAetherStorage {
	public TileEntityAetherManipulator() {
		super();
		use0 = false;
		inventory = new ItemStack[55];
	}

	@Override
	public String getInventoryName() {
		return "aec.manipulaator";
	}

	public boolean handleAether() {
		getMax();
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
				if (stackEv+getAether()>getMax()) {
					//System.out.println("Has more aether than we can hold!");
					boolean canSend = AetherNetwork.canSendAV(worldObj, xCoord, yCoord, zCoord, stackEv+getAether()-getMax())==0;
					if (!canSend) {
						//System.out.println("Could not transfer!");
						//AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, stackEv-getAether()-left);
						failed = true;
					} else {
						AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, stackEv+getAether()-getMax());
						energy = getMax();
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
					getMax();
					boolean willGet = AetherNetwork.canRequestAV(worldObj, xCoord, yCoord, zCoord, av-energy)==av-energy;
					if (willGet) {
						AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, av-energy);
						energy = 0;
					} else {
						float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, Math.min(av-energy,getMax()-energy));
						energy += got;
						failed = true;
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
							newStack = new ItemStack(topStack.getItem(),1,topStack.getItemDamage());
						}
						//newStack.stackSize = 1;
						this.setInventorySlotContents(slot, newStack);
					}
					
					doneSomething = true;
				}
				
				
			}
			
			if (!Boolean.parseBoolean(AetherCraft.getOptions("instantconsume")) && i >= 9 && doneSomething) {
				return true;
			}
		}
		if (doneSomething) {
			return true;
		}
		return false;
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
	public float getMax() {
		return (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage")))*(Math.pow(2,getMetadata()*2)));
	}
}
