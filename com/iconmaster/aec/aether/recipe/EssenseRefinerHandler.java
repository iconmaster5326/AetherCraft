package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class EssenseRefinerHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("am2.items.RecipeArsMagica");
			Object inputObj = recipeClass.cast(recipe);
			Object[] items = (Object[]) recipeClass.cast(recipe).getClass().getMethod("getRecipeItems").invoke(inputObj);
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
			Class recipeClass = Class.forName("am2.items.RecipeArsMagica");
			Object inputObj = recipeClass.cast(recipe);
			ItemStack item = (ItemStack) inputObj.getClass().getMethod("getOutput").invoke(inputObj);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		Class recipeClass;
		try {
			recipeClass = Class.forName("am2.items.RecipeArsMagica");
			Object inputObj = Class.forName("am2.blocks.RecipesEssenceRefiner").getMethod("essenceRefinement").invoke(null);
			Map list = (Map)inputObj.getClass().getMethod("GetRecipeList").invoke(inputObj);
		    for (Object recipe : list.values()) {
				ItemStack output = DynamicAVRegister.getOutput(recipe);
				List uid = UidUtils.getUID(output);
				if (recipeList.get(uid) == null) {
					recipeList.put(uid, new ArrayList());
				}
				((ArrayList) recipeList.get(uid)).add(recipe);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
