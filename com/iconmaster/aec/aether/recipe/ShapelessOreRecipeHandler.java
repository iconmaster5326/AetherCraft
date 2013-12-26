package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return DynamicAVRegister.flattenInputs(((ShapelessOreRecipe)recipe).getInput());
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessOreRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
