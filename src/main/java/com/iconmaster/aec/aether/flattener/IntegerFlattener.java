package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IntegerFlattener implements IFlattenerHandler<Integer> {

	@Override
	public ItemStack flatten(Integer input) {
		return new ItemStack(Item.getItemById(input));
	}

}
