package com.iconmaster.aec.tileentity;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IAetherStorageItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileEntityAetherContainer extends AetherCraftTileEntity implements
		IInventory, IAetherStorage {

	public TileEntityAetherContainer() {
		super();
		inventory = new ItemStack[2];
	}

	@Override
	public String getInventoryName() {
		return "aec.container";
	}

	@Override
	public boolean handleAether() {
		getMax();
		boolean doneSomething = false;
		float chargeRate = (float) (AetherCraft.options.getFloat("chargerate")*Math.pow(2,getMetadata()*2));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack bottomStack = this.getStackInSlot(1);

		// ------------------- Discharging - TOP SLOT -------------------
		if (topStack != null && topStack.getItem() instanceof IAetherStorageItem) {
			float got =  ((IAetherStorageItem)topStack.getItem()).extractAether(topStack, chargeRate);
			if (energy+got > getMax()) {
				energy = getMax();
				float rest = AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, energy+got-getMax());
				((IAetherStorageItem)topStack.getItem()).addAether(topStack, rest);
			} else {
				energy += got;
			}
			if(got>0) {
				doneSomething = true;
			}
		}

		// ------------------- Charging - BOTTOM SLOT -------------------
		if (bottomStack != null && bottomStack.getItem() instanceof IAetherStorageItem) {
			float rest = ((IAetherStorageItem)bottomStack.getItem()).addAether(bottomStack, Math.min(this.energy,chargeRate));
			float drawn = Math.min(this.energy,chargeRate);
			if (chargeRate > this.energy ) {
				float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, Math.min(chargeRate*AetherCraft.options.getFloat("excesspull")-this.energy,this.getMax()-chargeRate));
				this.energy = got;
			} else {
				this.energy -= drawn-rest;
			}
			if (drawn-rest>0) {
				doneSomething = true;
			}
		}
		return doneSomething;
	}
	
	@Override
	public float getMax() {
        return (float) ((AetherCraft.options.getFloat("acmaxstorage"))*(Math.pow(2,getMetadata()*2)));
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 1:
			return new int[] {1};
		case 0:
			return new int[] {0};
		default:
			return new int[] {0,1};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot != 1;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot != 0;
	}
}