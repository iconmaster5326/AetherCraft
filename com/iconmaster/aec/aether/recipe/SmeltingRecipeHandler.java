package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmeltingRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return ((AVSmeltingRecipe)recipe).getInputs();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((AVSmeltingRecipe)recipe).getOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		//put smelting recipes on the list too...
		Iterator it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	AVSmeltingRecipe recipe = new AVSmeltingRecipe((Integer)pairs.getKey(),(ItemStack)pairs.getValue());
			ItemStack output = DynamicAVRegister.getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
        }
        
        // AND meta-smelting. Hoo boy.
		it = FurnaceRecipes.smelting().getMetaSmeltingList().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	AVSmeltingRecipe recipe = new AVSmeltingRecipe(UidUtils.getStackFromUid((List)pairs.getKey()),(ItemStack)pairs.getValue());
			ItemStack output = DynamicAVRegister.getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
        }
	}
}
