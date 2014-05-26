package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.ItemStack;

/**
 * Make your object-to-ItemStack handlers implement this.
 * @author iconmaster
 *
 */
public interface IFlattenerHandler<T> {
	/**
	 * Takes in an object and returns an ItemStack. Called when a list of inputs needs to flattened.
	 * @param input
	 * @return
	 */
	public ItemStack flatten(T input);
}
