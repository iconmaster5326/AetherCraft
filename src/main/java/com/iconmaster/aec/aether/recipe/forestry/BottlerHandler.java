package com.iconmaster.aec.aether.recipe.forestry;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottlerHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getField("can").get(inputObj);
			
			ItemStack fluid;
			FluidStack fstack = ((FluidStack) recipeClass.cast(recipe).getClass().getField("input").get(inputObj));
			Block bid = fstack.getFluid().getBlock();
			if (bid == null) {
				fluid = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
			} else {
				fluid = new ItemStack(bid,fstack.amount,0);
			}
			a.add(fluid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		a.add(input);
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
			Object inputObj = recipeClass.cast(recipe);
			output = (ItemStack) (ItemStack) recipeClass.cast(recipe).getClass().getField("bottled").get(inputObj);
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
		 Class recipeClass = Class.forName("forestry.factory.gadgets.MachineBottler$Recipe");
		List list = (List) Class.forName("forestry.factory.gadgets.MachineBottler$RecipeManager").getField("recipes").get(null);
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
