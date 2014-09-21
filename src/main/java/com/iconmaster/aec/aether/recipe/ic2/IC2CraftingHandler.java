package com.iconmaster.aec.aether.recipe.ic2;

import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: Download IC2 API and REFLECT AROUND IT!!!
public class IC2CraftingHandler implements IDynamicAVRecipeHandler {
	
	private String type;

	public IC2CraftingHandler(String type) {
		this.type = type;
	}

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName(type);
			Object inputObj = recipeClass.cast(recipe);
			Object[] items = (Object[]) recipeClass.cast(recipe).getClass().getField("input").get(inputObj);
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
			Class recipeClass = Class.forName(type);
			Object inputObj = recipeClass.cast(recipe);
			ItemStack item = (ItemStack) recipeClass.cast(recipe).getClass().getField("output").get(inputObj);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
