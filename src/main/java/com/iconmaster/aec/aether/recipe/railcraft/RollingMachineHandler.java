package com.iconmaster.aec.aether.recipe.railcraft;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RollingMachineHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return null;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Object inputObj = Class.forName("mods.railcraft.api.crafting.RailcraftCraftingManager").getField("rollingMachine").get(null);
		List list = (List)inputObj.getClass().getMethod("getRecipeList").invoke(inputObj);
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
