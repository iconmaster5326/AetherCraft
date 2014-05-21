package com.iconmaster.aec.aether.recipe.bc;

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

public class RefineryHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipe.getClass().getMethod("getIngredient1").invoke(recipe));
			Block bid = fstack.getFluid().getBlock();
			if (bid == null) {
				fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
			} else {
				fluid = new ItemStack(bid,fstack.amount,0);
			}
			a.add(fluid);
			
			fstack = null;
			fstack = ((FluidStack) recipe.getClass().getMethod("getIngredient2").invoke(recipe));
			if (fstack != null) {
				bid = fstack.getFluid().getBlock();
				if (bid == null) {
					fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
				} else {
					fluid = new ItemStack(bid,fstack.amount,0);
				}
				a.add(fluid);
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
			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipe.getClass().getMethod("getResult").invoke(recipe));
			Block bid = fstack.getFluid().getBlock();
			if (bid == null) {
				fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
			} else {
				fluid = new ItemStack(bid,fstack.amount,0);
			}
			return fluid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Object obj = Class.forName("buildcraft.api.recipes.BuildcraftRecipes").getField("refinery").get(null);
		Collection list = (Collection) obj.getClass().getMethod("getRecipes").invoke(obj);
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
