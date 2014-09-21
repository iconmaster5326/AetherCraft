package com.iconmaster.aec.inventory;

import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;
import net.minecraft.inventory.Slot;

public class SlotAetoChest extends Slot {
	public TileEntityAetologistsChest te;

	public SlotAetoChest(TileEntityAetologistsChest te, int id, int x, int y) {
		super(te, id, x, y);
		this.te = te;
	}
}
