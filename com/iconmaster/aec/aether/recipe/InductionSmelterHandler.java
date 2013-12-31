package com.iconmaster.aec.aether.recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

public class InductionSmelterHandler implements IDynamicAVRecipeHandler {
	
	private Class recipeClass = ModHelpers.getTERecipeObject("Smelter");

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		ItemStack input2 = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getPrimaryInput").invoke(inputObj);
			Object inputObj2 = recipeClass.cast(recipe);
			input2 = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getSecondaryInput").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input == null) {
			return a;
		}
		a.add(input);
		if (input2 == null) {
			return a;
		}
		a.add(input2);
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			output = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getPrimaryOutput").invoke(inputObj);
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
		Class inputObj = (Class.forName("thermalexpansion.util.crafting.SmelterManager"));
		Object list = inputObj.getMethod("getRecipeList").invoke(inputObj);
	    int length = Array.getLength(list);
	    for (int i = 0; i < length; i ++) {
	        Object recipe = Array.get(list, i);
			Object inputObj2 = recipeClass.cast(recipe);
			ItemStack output = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getPrimaryOutput").invoke(inputObj2);
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
