package com.iconmaster.aec.aether;

import net.minecraft.item.ItemStack;

/**
 * Items implement this to either have a special consumption AV or to have special actions execute upon consumption. For example, use this to have the AV of an item change based on NBT data. Tooltips display the custom consumption AV by default.
 * @author iconmaster
 *
 */
public interface IConsumeBehavior {
	/**
	 * Returns the amount of AV an ItemStack is worth on consumption.
	 * @param stack
	 * @return
	 */
	public float getConsumeAV(ItemStack stack);
	/**
	 * Called upon item consumption. Returns a new ItemStack to be placed back into the inventory, or null for default behavior.
	 * @param stack
	 * @param inv
	 * @return
	 */
	public ItemStack consume(ItemStack stack,ItemStack[] inv);
}
