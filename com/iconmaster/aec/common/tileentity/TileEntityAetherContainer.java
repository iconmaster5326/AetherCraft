package com.iconmaster.aec.common.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IAetherStorageItem;
import com.iconmaster.aec.common.AetherCraft;

public class TileEntityAetherContainer extends AetherCraftTileEntity implements
		IInventory, IAetherStorage {

	public TileEntityAetherContainer() {
		super();
		energyBlockType = AetherCraft.GUI_ID_CONTAINER;
		inventory = new ItemStack[2];
	}

	@Override
	public String getInvName() {
		return "gra.tileentityec";
	}

	@Override
	public void handleAether() {
		calcMax();
		float chargeRate = (float) (Float.parseFloat(AetherCraft
				.getOptions("chargerate"))*Math.pow(2,getMetadata()*2));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack bottomStack = this.getStackInSlot(1);

		// ------------------- Discharging - TOP SLOT -------------------
		if (topStack != null && topStack.getItem() instanceof IAetherStorageItem) {
			float got =  ((IAetherStorageItem)topStack.getItem()).extractAether(topStack, chargeRate);
			if (energy+got > max) {
				energy = max;
				float rest = AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, energy+got-max);
				((IAetherStorageItem)topStack.getItem()).addAether(topStack, rest);
			} else {
				energy += got;
			}
			this.sync();

		}

		// ------------------- Charging - BOTTOM SLOT -------------------
		if (bottomStack != null && bottomStack.getItem() instanceof IAetherStorageItem) {
			float rest = ((IAetherStorageItem)bottomStack.getItem()).addAether(bottomStack, Math.min(this.energy,chargeRate));
			float drawn = Math.min(this.energy,chargeRate);
			if (chargeRate > this.energy ) {
				float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, chargeRate-energy);
				this.energy = got;
			} else {
				this.energy -= drawn-rest;
			}
			this.sync();
		}
	}

	@Override
	public void calculateProgress() {
		calcMax();
		this.progress = (int) ((this.energy / max) * 100.0f);
		if (this.progress > 100) {
			this.progress = 100;
		}
	}
	
	@Override
	public void calcMax() {
		if (max == 0) {
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("acmaxstorage")))*(Math.pow(2,getMetadata()*2)));
		}
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