package com.iconmaster.aec.common.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.common.AetherCraft;

public class TileEntityAetherCondenser extends AetherCraftTileEntity implements
		ISidedInventory, IAetherStorage {

	public TileEntityAetherCondenser() {
		super();
		energyBlockType = AetherCraft.GUI_ID_CONDENSER;
		inventory = new ItemStack[9];
	}

	@Override
	public String getInvName() {
		return "aec.condenser";
	}

	@Override
	public void handleAether() {
		float emMaxStorage = Float.parseFloat(AetherCraft.getOptions("ammaxstorage"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 1; i < this.getSizeInventory(); i++) {
			boolean failed = false;
			currentStack = this.getStackInSlot(i);

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

	@Override
	public void calculateProgress() {
		calcMax();
		this.progress = (int) ((this.getAether()/max)*100);
		if (this.progress > 100) {
			this.progress = 100;
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
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))/2)*(Math.pow(2,getMetadata()*2)));
		}
	}
}
