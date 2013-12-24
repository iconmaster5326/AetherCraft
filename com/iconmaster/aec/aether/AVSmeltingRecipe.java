package com.iconmaster.aec.aether;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class AVSmeltingRecipe implements IDynamicAVRecipe {

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
	
	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public ArrayList getInputs() {
		ArrayList a = new ArrayList();
		a.add(input);
		return a;
	}
	
}
