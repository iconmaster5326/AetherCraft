package com.iconmaster.aec.aether.recipe.ic2;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;

public class SimpleIC2RecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return ((SimpleIC2Recipe)recipe).getInputs();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((SimpleIC2Recipe)recipe).getOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		iterate(recipeList, Recipes.macerator);
		iterate(recipeList, Recipes.compressor);
		iterate(recipeList, Recipes.extractor);
		iterate(recipeList, Recipes.centrifuge);
		iterate(recipeList, Recipes.metalformerCutting);
		iterate(recipeList, Recipes.metalformerExtruding);
		iterate(recipeList, Recipes.metalformerRolling);
	}
	
	public void iterate(HashMap recipeList, IMachineRecipeManager manager) {
		Iterator it = manager.getRecipes().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	List list = ((IRecipeInput)pairs.getKey()).getInputs();
        	if (list.size()!=0) {
	        	SimpleIC2Recipe recipe = new SimpleIC2Recipe(((IRecipeInput)pairs.getKey()),((RecipeOutput)pairs.getValue()));
				ItemStack output = DynamicAVRegister.getOutput(recipe);
				List uid = UidUtils.getUID(output);
				if (recipeList.get(uid) == null) {
					recipeList.put(uid, new ArrayList());
				}
				((ArrayList) recipeList.get(uid)).add(recipe);
        	}
        }
	}
}
