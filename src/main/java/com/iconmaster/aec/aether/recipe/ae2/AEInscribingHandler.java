package com.iconmaster.aec.aether.recipe.ae2;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AEInscribingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			if (recipe.getClass().getField("usePlates").getBoolean(recipe)) {
				a.add(recipe.getClass().getField("plateA").get(recipe));
				a.add(recipe.getClass().getField("plateB").get(recipe));
			}
			
			a.add(recipe.getClass().getField("imprintable").get(recipe));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (a.size() == 0) {
			return null;
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			output = (ItemStack) recipe.getClass().getField("output").get(recipe);
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
		List list = (List) Class.forName("appeng.recipes.handlers.Inscribe").getField("recipes").get(null);
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
