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

public class DetailingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.Detailing$DetailInput");
			Object inputObj = recipeClass.cast(recipe);
			
			a.add(new ItemStack(recipe.getClass().getField("inputID").getInt(recipe),1,recipe.getClass().getField("inputMeta").getInt(recipe)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.Detailing$DetailInput");
			Object inputObj = recipeClass.cast(recipe);

			return new ItemStack(recipe.getClass().getField("outputID").getInt(recipe),1,recipe.getClass().getField("outputMeta").getInt(recipe));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Class recipeClass = Class.forName("tconstruct.library.crafting.Detailing$DetailInput");
		
		Object registry = Class.forName("tconstruct.library.TConstructRegistry").getMethod("getChiselDetailing").invoke(null);
		Collection list = (Collection) registry.getClass().getField("detailing").get(registry);
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
