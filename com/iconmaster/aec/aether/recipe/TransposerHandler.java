package com.iconmaster.aec.aether.recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

public class TransposerHandler implements IDynamicAVRecipeHandler {
	
	private Class recipeClass = ModHelpers.getTERecipeObject("Transposer");

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		ItemStack input = null;
		ItemStack input2 = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			input = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getInput").invoke(inputObj);
			
			FluidStack fluid = (FluidStack) recipeClass.cast(recipe).getClass().getMethod("getFluid").invoke(inputObj);
			int bid = fluid.getFluid().getBlockID();
			if (bid == -1) {return null;}
			input2 = new ItemStack(bid,fluid.amount,0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		a.add(input);
		a.add(input2);
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		ItemStack output = null;
		try {
			Object inputObj = recipeClass.cast(recipe);
			output = (ItemStack) recipeClass.cast(recipe).getClass().getMethod("getOutput").invoke(inputObj);
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
		Class inputObj = (Class.forName("thermalexpansion.util.crafting.TransposerManager"));
		Object list = inputObj.getMethod("getFillRecipeList").invoke(inputObj);
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
