package com.iconmaster.aec.aether.flattener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;

public class ArrayFlattener implements IFlattenerHandler<Object[]> {

	@Override
	public ItemStack flatten(Object[] input) {
		return (new ListFlattener()).flatten(Arrays.asList(input));
	}
}
