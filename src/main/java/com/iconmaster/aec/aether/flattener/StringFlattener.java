package com.iconmaster.aec.aether.flattener;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

//We're assuming here that a string means an OreDict entry
public class StringFlattener implements IFlattenerHandler<String> {

	@Override
	public ItemStack flatten(String input) {
		ArrayList ores = OreDictionary.getOres(input);
		if (ores.size() == 0) {return null;}
		float lav = Float.MAX_VALUE;
		ItemStack lentry = null;
		ArrayList<ItemStack> flattened = DynamicAVRegister.flattenInputsPassively(ores);
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
