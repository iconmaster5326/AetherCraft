package com.iconmaster.aec.aether;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IConsumeBehavior {
	public float getConsumeAV(ItemStack stack);
	public ItemStack consume(ItemStack stack,ItemStack[] inv);
}
