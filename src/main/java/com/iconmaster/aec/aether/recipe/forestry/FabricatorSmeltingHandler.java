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

public class FabricatorSmeltingHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFabricator$Smelting");
			Object inputObj = recipeClass.cast(recipe);
	
			ItemStack input = (ItemStack) inputObj.getClass().getMethod("getResource").invoke(inputObj);
			if (input!=null) {
				a.add(input);
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
			Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFabricator$Smelting");
			Object inputObj = recipeClass.cast(recipe);
			
			FluidStack fstack = (FluidStack) inputObj.getClass().getMethod("getProduct").invoke(inputObj);
			ItemStack fluid;
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
		 Class recipeClass = Class.forName("forestry.factory.gadgets.MachineFabricator$Smelting");
		List list = (List) Class.forName("forestry.factory.gadgets.MachineFabricator$RecipeManager").getField("smeltings").get(null);
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
