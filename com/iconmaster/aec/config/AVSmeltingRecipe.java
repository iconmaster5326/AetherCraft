package com.iconmaster.aec.config;

import net.minecraft.item.ItemStack;

public class AVSmeltingRecipe {

	private ItemStack input;
	private ItemStack output;
	
	public AVSmeltingRecipe(Integer input, ItemStack output) {
		this.input = new ItemStack((int)input,1,0);
		this.output = output;
	}
	
	public AVSmeltingRecipe(ItemStack input, ItemStack output) {
		this.input = input;
		this.output = output;
	}

	public ItemStack getInput() {
		return input;
	}
	
	public ItemStack getOutput() {
		return output;
	}
	
}
