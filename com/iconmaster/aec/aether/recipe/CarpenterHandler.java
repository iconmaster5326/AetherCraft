package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class CarpenterHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineCarpenter$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			ItemStack box = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getBox").invoke(inputObj);
			if (box!=null) {
				a.add(box);
			}
			
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getMethod("getLiquid").invoke(inputObj));
				if (fstack != null) {
				ItemStack fluid;
				int bid = fstack.getFluid().getBlockID();
				if (bid == -1) {
					fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
				} else {
					fluid = new ItemStack(bid,fstack.amount,0);
				}
				a.add(fluid);
			}
				
			Object grid = recipeClass.cast(recipe).getClass().getMethod("asIRecipe").invoke(inputObj);
			ArrayList gridArray = DynamicAVRegister.getInputs(grid);
			if (gridArray!=null) {
				a.addAll(gridArray);
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
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineCarpenter$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			
			Object grid = recipeClass.cast(recipe).getClass().getMethod("asIRecipe").invoke(inputObj);
			return DynamicAVRegister.getOutput(grid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		 Class recipeClass = Class.forName("forestry.factory.gadgets.MachineCarpenter$Recipe");
		List list = (List) Class.forName("forestry.factory.gadgets.MachineCarpenter$RecipeManager").getField("recipes").get(null);
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
