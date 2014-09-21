package com.iconmaster.aec.inventory;

import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAetoChest extends Slot {
	public TileEntityAetologistsChest te;

	public SlotAetoChest(TileEntityAetologistsChest te, int id, int x, int y) {
		super(te, id, x, y);
		this.te = te;
	}
}
