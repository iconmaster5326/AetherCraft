package com.iconmaster.aec.aether.recipe.tcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;

public class CastingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.CastingRecipe");
			Object inputObj = recipeClass.cast(recipe);
			
			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getField("castingMetal").get(inputObj));
			Block bid = fstack.getFluid().getBlock();
			if (bid == null) {
				fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
			} else {
				fluid = new ItemStack(bid,fstack.amount,0);
			}
			a.add(fluid);
			
			if ((Boolean) recipe.getClass().getField("consumeCast").get(recipe)) {
				a.add(recipe.getClass().getField("cast").get(recipe));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.CastingRecipe");
			Object inputObj = recipeClass.cast(recipe);

			return (ItemStack) recipe.getClass().getMethod("getResult").invoke(recipe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Class recipeClass = Class.forName("tconstruct.library.crafting.CastingRecipe");
		
		Object registry = Class.forName("tconstruct.library.TConstructRegistry").getMethod("getBasinCasting").invoke(null);
		Collection list = (Collection) registry.getClass().getMethod("getCastingRecipes").invoke(registry);
	    for (Object recipe : list) {
			Object inputObj2 = recipeClass.cast(recipe);
			ItemStack output = DynamicAVRegister.getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
		}
	    
		registry = Class.forName("tconstruct.library.TConstructRegistry").getMethod("getTableCasting").invoke(null);
		list = (Collection) registry.getClass().getMethod("getCastingRecipes").invoke(registry);
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
