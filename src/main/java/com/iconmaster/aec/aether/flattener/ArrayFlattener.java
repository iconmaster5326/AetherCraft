package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class ArrayFlattener implements IFlattenerHandler<Object[]> {

	@Override
	public ItemStack flatten(Object[] input) {
		return (new ListFlattener()).flatten(Arrays.asList(input));
	}
}
