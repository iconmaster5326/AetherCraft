package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

public class FluidContainerHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		FluidContainerData data = (FluidContainerData)recipe;
		a.add(data.emptyContainer);
		int bid = data.fluid.getFluid().getBlockID();
		if (bid == -1 || AVRegistry.getAV(new ItemStack(bid,1000,0))==0) {return null;}
		a.add(new ItemStack(bid,1000,0));
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((FluidContainerData)recipe).filledContainer;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		FluidContainerData[] list = FluidContainerRegistry.getRegisteredFluidContainerData();
		for (FluidContainerData data : list) {
			ItemStack output = DynamicAVRegister.getOutput(data);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(data);
		}
	}
}
