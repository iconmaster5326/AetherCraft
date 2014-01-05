package com.iconmaster.aec.aether.recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.util.UidUtils;

public class ForestryBottlerHandler implements IDynamicAVRecipeHandler {
	
	//private Class recipeClass = ModHelpers.getTERecipeObject("Pulverizer");

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getField("can").get(inputObj);
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
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			output = (ItemStack) recipeClass.cast(recipe).getClass().getField("bottled").get(recipe);
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
		Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
		Class inputObj = (Class.forName("forestry.factory.gadgets.MachineBottler$RecipeManager"));
		Map list = (Map)inputObj.getMethod("getRecipes").invoke(null);
	    Iterator it = list.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry p = (Entry) it.next();
	        Object recipe = p.getValue();
			Object inputObj2 = recipeClass.cast(recipe);
			ItemStack output = (ItemStack) recipeClass.cast(recipe).getClass().getField("bottled").get(inputObj2);
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
