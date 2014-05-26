package com.iconmaster.aec.aether.recipe.ae2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import appeng.api.AEApi;
import appeng.recipes.handlers.Inscribe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

public class AEGrindingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			input = (ItemStack) recipe.getClass().getMethod("getInput").invoke(recipe);
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
			output = (ItemStack) recipe.getClass().getMethod("getOutput").invoke(recipe);
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
		Object obj = Class.forName("appeng.api.AEApi").getMethod("instance").invoke(null);
		obj = obj.getClass().getMethod("registries").invoke(obj);
		obj = obj.getClass().getMethod("grinder").invoke(obj);
		List list = (List) obj.getClass().getMethod("getRecipes").invoke(obj);
	    for (Object recipe : list) {
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
