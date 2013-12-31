package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class AssemblyRecipeHandler implements IDynamicAVRecipeHandler {
	
	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("buildcraft.api.recipes.AssemblyRecipe");
			for (Object input : (Object[])recipeClass.cast(recipe).getClass().getField("input").get(recipe)) {
				a.add(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DynamicAVRegister.flattenInputs(a);
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
			Class recipeClass = Class.forName("buildcraft.api.recipes.AssemblyRecipe");
			return (ItemStack) recipeClass.cast(recipe).getClass().getField("output").get(recipe);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		try {
			Class recipeClass = Class.forName("buildcraft.api.recipes.AssemblyRecipe");
			for (Object recipe : (Iterable)recipeClass.getField("assemblyRecipes").get(null)) {
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
