package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return new ArrayList(Arrays.asList(((ShapedRecipes)recipe).recipeItems));
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapedRecipes)recipe).getRecipeOutput();
	}

}
