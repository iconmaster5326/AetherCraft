package com.iconmaster.aec.aether;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IProduceBehavior {
	public float getProduceAV(ItemStack stack);
	public ItemStack produce(ItemStack stack,ItemStack[] inv);
}
