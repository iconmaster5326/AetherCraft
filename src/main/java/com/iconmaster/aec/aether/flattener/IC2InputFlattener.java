package com.iconmaster.aec.aether.flattener;

import net.minecraft.item.ItemStack;

import java.util.List;

//IC2:itemDust::0=127.75
public class IC2InputFlattener implements IFlattenerHandler {

	@Override
	public ItemStack flatten(Object input) {
		ItemStack stack = null;
		
		try {
			List list = (List) input.getClass().getMethod("getInputs").invoke(input);
			stack = (new ListFlattener()).flatten(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stack;
	}

}
