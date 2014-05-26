package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFlattener implements IFlattenerHandler<Item> {

	@Override
	public ItemStack flatten(Item input) {
		return new ItemStack(input);
	}

}
