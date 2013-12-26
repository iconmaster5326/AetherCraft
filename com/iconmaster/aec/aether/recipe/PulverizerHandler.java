package com.iconmaster.aec.aether.recipe;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

public class PulverizerHandler implements IDynamicAVRecipeHandler {
	
	private Class recipeClass = ModHelpers.getRecipePulverizer();

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		for (int i=1;i<=input.stackSize;i++) {
			a.add(input);
		}
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
		Class inputObj = (Class.forName("thermalexpansion.util.crafting.PulverizerManager"));
		Object list = inputObj.getMethod("getRecipeList").invoke(inputObj);
	    int length = Array.getLength(list);
	    for (int i = 0; i < length; i ++) {
	        Object recipe = Array.get(list, i);
			Object inputObj2 = recipeClass.cast(recipe);
			for (Method m : inputObj.getClass().getMethods()) {
				System.out.println("[AEC TE] "+m.getName());
			}
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
