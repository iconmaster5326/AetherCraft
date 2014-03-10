package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class FermenterHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFermenter$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getField("resource").get(inputObj);
			
			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getField("liquid").get(inputObj));
			int bid = fstack.getFluid().getBlockID();
			if (bid == -1) {
				fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
			} else {
				fluid = new ItemStack(bid,fstack.amount,0);
			}
			a.add(fluid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input != null) {
			a.add(input);
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFermenter$Recipe");
			Object inputObj = recipeClass.cast(recipe);

			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getField("output").get(inputObj));
			int bid = fstack.getFluid().getBlockID();
			if (bid == -1) {
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
		 Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFermenter$Recipe");
		List list = (List) Class.forName("forestry.factory.gadgets.MachineFermenter$RecipeManager").getField("recipes").get(null);
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
