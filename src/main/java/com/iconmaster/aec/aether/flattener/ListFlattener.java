package com.iconmaster.aec.aether.flattener;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;

public class ListFlattener implements IFlattenerHandler<List> {

	@Override
	public ItemStack flatten(List input) {
		if (input.size() == 0) {return null;}
		float lav = Float.MAX_VALUE;
		ItemStack lentry = null;
		ArrayList<ItemStack> flattened = DynamicAVRegister.flattenInputsPassively(input);
		if (flattened.size() == 0) {return null;}
		for (ItemStack entry : flattened) {
			float av = AVRegistry.getAV(entry);
			if (av != 0 && av < lav) {
				lav = av;
				lentry = (ItemStack) entry;
			}
		}
		if (lentry == null) {
			return flattened.get(0);
		}
		return lentry;
	}
}
