package com.iconmaster.aec.aether.recipe.forestry;

import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class ForestryCraftingRecipeHandler implements IDynamicAVRecipeHandler {
	
	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("forestry.core.interfaces.IDescriptiveRecipe");
			for (Object input : (Object[])recipeClass.cast(recipe).getClass().getMethod("getIngredients").invoke(recipe)) {
				a.add(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
			return ((IRecipe) recipe).getRecipeOutput();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
