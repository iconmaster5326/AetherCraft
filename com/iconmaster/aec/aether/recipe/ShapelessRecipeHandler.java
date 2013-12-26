package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ShapelessRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return new ArrayList(((ShapelessRecipes)recipe).recipeItems);
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessRecipes)recipe).getRecipeOutput();
	}

}
