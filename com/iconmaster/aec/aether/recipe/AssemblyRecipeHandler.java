package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import buildcraft.api.recipes.AssemblyRecipe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class AssemblyRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		for (Object input : ((AssemblyRecipe)recipe).input) {
			a.add(input);
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((AssemblyRecipe)recipe).output;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	      for (AssemblyRecipe recipe : AssemblyRecipe.assemblyRecipes) {
				ItemStack output = DynamicAVRegister.getOutput(recipe);
				List uid = UidUtils.getUID(output);
				if (recipeList.get(uid) == null) {
					recipeList.put(uid, new ArrayList());
				}
				((ArrayList) recipeList.get(uid)).add(recipe);
	      }
	}
}
