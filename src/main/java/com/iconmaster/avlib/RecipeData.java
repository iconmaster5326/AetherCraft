package com.iconmaster.avlib;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class RecipeData {
	public List inputs;
	public ItemData output;

	public RecipeData(ItemData output, List inputs) {
		this.output = output;
		this.inputs = inputs;
	}

	public RecipeData(ItemStack output, List inputs) {
		this.output = new ItemData(output);
		this.inputs = inputs;
	}

	@Override
	public String toString() {
		return output.toString()+"->"+inputs.toString();
	}
}
