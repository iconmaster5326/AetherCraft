package com.iconmaster.avlib.flatten;

import com.iconmaster.avlib.IRecipeFlattener;
import com.iconmaster.avlib.ItemData;
import net.minecraft.item.ItemStack;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class ItemStackFlattener implements IRecipeFlattener<ItemStack> {

	@Override
	public Object flatten(ItemStack input) {
		if (input.getItemDamage()==32767) {
			input = new ItemStack(input.getItem(),input.stackSize,0);
		}
		return new ItemData(input);
	}
}
