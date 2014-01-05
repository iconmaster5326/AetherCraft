package com.iconmaster.aec.common.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
			float got =  ((IAetherStorageItem)topStack.getItem()).extractAether(topStack, Math.min(max - energy,chargeRate));
			energy += got;
			this.sync();

		}

		// ------------------- Charging - BOTTOM SLOT -------------------
		if (bottomStack != null && bottomStack.getItem() instanceof IAetherStorageItem) {
			float rest = ((IAetherStorageItem)bottomStack.getItem()).addAether(bottomStack, Math.min(this.energy,chargeRate));
			this.energy -= Math.min(this.energy,chargeRate)-rest;
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
}