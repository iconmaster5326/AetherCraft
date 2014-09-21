package com.iconmaster.aec.aether.recipe;

import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EIOAlloySmelterHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		Object[] inputs = null;
		try {
			inputs = (Object[]) recipe.getClass().getMethod("getInputs").invoke(recipe);
			
			if (inputs == null) {
				return null;
			}
			
			for (Object input : inputs) {
				ArrayList all = new ArrayList();
				all.add(input.getClass().getMethod("getInput").invoke(input));
				all.add(input.getClass().getMethod("getEquivelentInputs").invoke(input));
				a.add(all);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (inputs == null) {
			return null;
		}

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
		Object obj = Class.forName("crazypants.enderio.machine.alloy.AlloyRecipeManager").getMethod("getInstance").invoke(null);
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
