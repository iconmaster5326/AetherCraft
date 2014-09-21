package com.iconmaster.aec.aether.recipe.tcon;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AlloyHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("tconstruct.library.crafting.AlloyMix");
			Object inputObj = recipeClass.cast(recipe);
			Collection list = ((Collection) recipeClass.cast(recipe).getClass().getField("mixers").get(inputObj));
			for (Object input : list) {
				ItemStack fluid;
				FluidStack fstack = (FluidStack)input;
				Block bid = fstack.getFluid().getBlock();
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
			Class recipeClass = Class.forName("tconstruct.library.crafting.AlloyMix");
			Object inputObj = recipeClass.cast(recipe);

			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getField("result").get(inputObj));
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
		Class recipeClass = Class.forName("tconstruct.library.crafting.AlloyMix");
		Object recipes = Class.forName("tconstruct.library.crafting.Smeltery").getField("instance").get(null);
		Collection list = (Collection) recipes.getClass().getMethod("getAlloyList").invoke(recipes);
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
