package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class SmeltingRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		// TODO Auto-generated method stub
		return ((AVSmeltingRecipe)recipe).getInputs();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		// TODO Auto-generated method stub
		return ((AVSmeltingRecipe)recipe).getOutput();
	}
	
}
