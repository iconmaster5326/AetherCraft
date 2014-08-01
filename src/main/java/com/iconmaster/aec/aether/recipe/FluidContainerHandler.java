package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.DynamicAVRegister;
import com.iconmaster.aec.util.UidUtils;

public class FluidContainerHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		FluidContainerData data = (FluidContainerData)recipe;
		a.add(data.emptyContainer);
		if (data.fluid == null || data.fluid.getFluid() == null) return null;
		Block bid = data.fluid.getFluid().getBlock();
		if (bid == null) {
			a.add(AVRegistry.createFluidStack(data.fluid.getFluid(), data.fluid.amount));
		} else {
			a.add(new ItemStack(bid,data.fluid.amount,0));
		}
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
