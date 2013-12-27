package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.iconmaster.aec.aether.DynamicAVRegister;

public class ShapedOreRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return DynamicAVRegister.flattenInputs(new ArrayList(Arrays.asList(((ShapedOreRecipe)recipe).getInput())));
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapedOreRecipe)recipe).getRecipeOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
	
}
