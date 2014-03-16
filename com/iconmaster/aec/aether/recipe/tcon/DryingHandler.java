package com.iconmaster.aec.aether.recipe.tcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;

public class DryingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.DryingRackRecipes$DryingRecipe");
			Object inputObj = recipeClass.cast(recipe);
			
			a.add(recipe.getClass().getField("input").get(inputObj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.DryingRackRecipes$DryingRecipe");
			Object inputObj = recipeClass.cast(recipe);

			return (ItemStack) recipe.getClass().getMethod("getResult").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Class recipeClass = Class.forName("tconstruct.library.crafting.DryingRackRecipes$DryingRecipe");
		
		Collection list = (Collection) Class.forName("tconstruct.library.crafting.DryingRackRecipes").getField("recipes").get(null);
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
