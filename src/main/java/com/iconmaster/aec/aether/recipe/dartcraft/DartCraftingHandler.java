package com.iconmaster.aec.aether.recipe.dartcraft;

import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class DartCraftingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("bluedart.core.recipes.ShapedDartCrafting");
			Object inputObj = recipeClass.cast(recipe);
			Object[] items = (Object[]) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
			for (Object item : items) {
				a.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
			Class recipeClass = Class.forName("bluedart.core.recipes.ShapedDartCrafting");
			Object inputObj = recipeClass.cast(recipe);
			ItemStack item = ((IRecipe)recipe).getRecipeOutput();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
