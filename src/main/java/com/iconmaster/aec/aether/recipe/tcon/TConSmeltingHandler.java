package com.iconmaster.aec.aether.recipe.tcon;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TConSmeltingHandler implements IDynamicAVRecipeHandler {
	
	public class TConSmeltingEntry {
		private ItemStack input;
		private FluidStack output;
		
		public TConSmeltingEntry(ItemStack input, FluidStack output) {
			this.input = input;
			this.output = output;
		}

		public ItemStack getInput() {
			return input;
		}

		public FluidStack getOutput() {
			return output;
		}
	}

	@Override
	public ArrayList getInputs(Object recipe) {
		ArrayList a = new ArrayList();
		try {
			a.add(((TConSmeltingEntry)recipe).getInput());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		try {
			ItemStack fluid;
			FluidStack fstack = ((TConSmeltingEntry)recipe).getOutput();
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
		Class recipeClass;
		try {
			Object inputObj = Class.forName("tconstruct.library.crafting.Smeltery").getField("instance").get(null);
			Map list = (Map)inputObj.getClass().getMethod("getSmeltingList").invoke(inputObj);
		    for (Object recipe : list.entrySet()) {
		    	List<Integer> ilist = (List<Integer>) ((Entry)recipe).getKey();
				FluidStack fstack = (FluidStack) ((Entry)recipe).getValue();
				TConSmeltingEntry entry = new TConSmeltingEntry(ModHelpers.decodeTConList(ilist),fstack);
				ItemStack output;
				
				Block bid = fstack.getFluid().getBlock();
				if (bid == null) {
					output = AVRegistry.createFluidStack(fstack.getFluid(), fstack.amount);
				} else {
					output = new ItemStack(bid,fstack.amount,0);
				}
				
				List uid = UidUtils.getUID(output);
				if (recipeList.get(uid) == null) {
					recipeList.put(uid, new ArrayList());
				}
				((ArrayList) recipeList.get(uid)).add(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ItemStack decodeTConList(List<Integer> list) {
		return null;
	}
}
