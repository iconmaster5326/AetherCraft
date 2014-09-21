package com.iconmaster.aec.aether.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class ShapelessOreRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return ((ShapelessOreRecipe)recipe).getInput();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessOreRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
