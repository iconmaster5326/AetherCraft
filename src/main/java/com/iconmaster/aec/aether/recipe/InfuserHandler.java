package com.iconmaster.aec.aether.recipe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.item.ItemStack;

import java.util.*;

public class InfuserHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return ((InfuserRecipe)recipe).getInputs();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((InfuserRecipe)recipe).getOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		Iterator it = InfuserRegistry.getRecipes().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	InfuserRecipe recipe = new InfuserRecipe(UidUtils.getStackFromUid((List)pairs.getKey()),(ItemStack)pairs.getValue());
			ItemStack output = DynamicAVRegister.getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
        }
	}
}
