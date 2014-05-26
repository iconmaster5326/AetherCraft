package com.iconmaster.aec.aether.recipe.ae2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;

public class AEShapedCraftingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Object[] items = (Object[]) recipe.getClass().getMethod("getInput").invoke(recipe);
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
