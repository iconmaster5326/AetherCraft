package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import com.iconmaster.aec.aether.DynamicAVRegister;

public class AECraftingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("appeng.recipes.AEShapedQuartzRecipe");
			Object inputObj = recipeClass.cast(recipe);
			Object[] items = (Object[]) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
			for (Object item : items) {
				a.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DynamicAVRegister.flattenInputs(a);
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
			Class recipeClass = Class.forName("appeng.recipes.AEShapedQuartzRecipe");
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
