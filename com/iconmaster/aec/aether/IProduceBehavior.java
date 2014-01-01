package com.iconmaster.aec.aether;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Items implement this to either have a special production AV or to have special actions execute upon production. For example, use this to have the AV of an item change based on NBT data.
 * @author iconmaster
 *
 */
public interface IProduceBehavior {
	/**
	 * Returns the amount of AV an ItemStack is worth on production.
	 * @param stack
	 * @return
	 */
	public float getProduceAV(ItemStack stack);
	/**
	 * Called upon item production. Returns a new ItemStack to be placed back into the inventory, or null for default behavior.
	 * @param stack
	 * @param inv
	 * @return
	 */
	public ItemStack produce(ItemStack stack,ItemStack[] inv);
}
