package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.iconmaster.aec.aether.DynamicAVRegister;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ShapelessRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return DynamicAVRegister.flattenInputs(new ArrayList(((ShapelessRecipes)recipe).recipeItems));
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessRecipes)recipe).getRecipeOutput();
	}

	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
