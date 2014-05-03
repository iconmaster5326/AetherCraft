package com.iconmaster.aec.aether.recipe.te3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

public class CrucibleHandler implements IDynamicAVRecipeHandler {
	
	private Class recipeClass = ModHelpers.getTERecipeObject("Crucible");

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
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
		FluidStack output = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			output = (FluidStack) recipeClass.cast(recipe).getClass().getMethod("getOutput").invoke(inputObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (output == null) {
			return null;
		}
		ItemStack fluid;
		Block bid = output.getFluid().getBlock();
		if (bid == null) {return null;}
		fluid = new ItemStack(bid,output.amount,0);
		return fluid;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
	 try {
		Class inputObj = (Class.forName("thermalexpansion.util.crafting.CrucibleManager"));
		Object list = inputObj.getMethod("getRecipeList").invoke(inputObj);
	    int length = Array.getLength(list);
	    for (int i = 0; i < length; i ++) {
	        Object recipe = Array.get(list, i);
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
