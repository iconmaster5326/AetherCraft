package com.iconmaster.aec.aether.recipe.ae2;

import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class AEShapelessCraftingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList items = null;
		try {
			items = (ArrayList) recipe.getClass().getMethod("getInput").invoke(recipe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
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
