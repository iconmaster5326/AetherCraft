package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import com.iconmaster.aec.aether.DynamicAVRegister;

public class ShapelessRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return new ArrayList(((ShapelessRecipes)recipe).recipeItems);
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessRecipes)recipe).getRecipeOutput();
	}

	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
