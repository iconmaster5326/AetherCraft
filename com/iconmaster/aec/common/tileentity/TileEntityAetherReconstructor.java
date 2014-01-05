package com.iconmaster.aec.common.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IAetherStorageItem;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.SideUtils;

public class TileEntityAetherReconstructor extends AetherCraftTileEntity implements
		IInventory, IAetherStorage {

	public TileEntityAetherReconstructor() {
		super();
		energyBlockType = AetherCraft.GUI_ID_RECONSTRUCTOR;
		inventory = new ItemStack[2];
	}

	@Override
	public String getInvName() {
		return "aec.reconstructor";
	}

	@Override
	public void handleAether() {
		float rate = 8;
		ItemStack tool = getStackInSlot(0);
		if (tool != null) {
			if (tool.isItemDamaged()) {
				float av = AVRegistry.getAbsoluteAV(tool)/tool.getMaxDamage();
				boolean failed = false;
				if (energy >= av) {
					energy -= av;
				} else {
					float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, av);
					if (got < av) {
						energy += got;
						failed = true;
						if (got != 0) {sync();}
					}
				}
				if (!failed && av != 0) {
					tool.setItemDamage(tool.getItemDamage()-1);
					sync();
				}
			} else if (getStackInSlot(1)==null) {
				setInventorySlotContents(1, tool);
				inventory[0] = null;
				sync();
			}
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
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))/4)*(Math.pow(2,getMetadata()*2)));
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