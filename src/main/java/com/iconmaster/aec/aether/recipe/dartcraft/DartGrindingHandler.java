package com.iconmaster.aec.aether.recipe.dartcraft;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DartGrindingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Class recipeClass = Class.forName("bluedart.api.recipe.IForceGrindRecipe");
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		a.add(input);
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("bluedart.api.recipe.IForceGrindRecipe");
			Object inputObj = recipeClass.cast(recipe);
			output = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getOutput").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (output == null) {
			return null;
		}
		return output;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		 Class recipeClass = Class.forName("bluedart.api.recipe.IForceGrindRecipe");
		Class inputObj = Class.forName("bluedart.core.plugin.DartPluginGrinding");
		List list = (List)inputObj.getMethod("getRecipes").invoke(inputObj);
	    for (Object recipe : list) {
			Object inputObj2 = recipeClass.cast(recipe);
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
