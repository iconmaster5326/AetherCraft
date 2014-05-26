package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.ItemStack;

public class ItemStackFlattener implements IFlattenerHandler<ItemStack> {

	@Override
	public ItemStack flatten(ItemStack input) {
		if (input.stackSize<=0) {
			return new ItemStack(input.getItem(),1,input.getItemDamage());
		}
		return input;
	}

}
