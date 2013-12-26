package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.iconmaster.aec.aether.AVRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreRecipeHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return flattenInputs(((ShapelessOreRecipe)recipe).getInput());
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((ShapelessOreRecipe)recipe).getRecipeOutput();
	}

	private static ArrayList flattenInputs(ArrayList inputs) {
		ArrayList ret = new ArrayList();
		for (Object input : inputs) {
			if (input instanceof ArrayList) {
				ArrayList s = (ArrayList)input;
				if (s.size() == 0) {return null;}
				float lav = Float.MAX_VALUE;
				Object lentry = s.get(0);
				for (Object entry : s) {
					float av = AVRegistry.getAV((ItemStack)entry);
					if (av != 0 && av < lav) {
						lav = av;
						lentry = entry;
					}
				}
				ret.add(lentry);
			} else {
				ret.add(input);
			}
		}
		return ret;
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {}
}
