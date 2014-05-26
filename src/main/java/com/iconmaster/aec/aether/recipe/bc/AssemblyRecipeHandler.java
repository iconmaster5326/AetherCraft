package com.iconmaster.aec.aether.recipe.bc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;

public class AssemblyRecipeHandler implements IDynamicAVRecipeHandler {
	
	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			for (Object input : (Object[])recipe.getClass().getMethod("getInputs").invoke(recipe)) {
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
			return (ItemStack) recipe.getClass().getMethod("getOutput").invoke(recipe);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		try {
			Object obj = Class.forName("buildcraft.api.recipes.BuildcraftRecipes").getField("assemblyTable").get(null);
			for (Object recipe : (Iterable)obj.getClass().getMethod("getRecipes").invoke(obj)) {
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
