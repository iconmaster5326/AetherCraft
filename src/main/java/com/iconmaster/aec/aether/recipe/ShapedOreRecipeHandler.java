package com.iconmaster.aec.aether.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ShapedOreRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return new ArrayList(Arrays.asList(((ShapedOreRecipe)recipe).getInput()));
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapedOreRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
	
}
