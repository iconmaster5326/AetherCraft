package com.iconmaster.aec.aether.recipe;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

	public class SimpleIC2Recipe {
		private IRecipeInput input;
		private RecipeOutput output;
		
		public SimpleIC2Recipe(IRecipeInput recipeInput,RecipeOutput recipeOutput) {
			this.input = recipeInput;
			this.output = recipeOutput;
		}
		
		public ItemStack getOutput() {
			List list = input.getInputs();
			if (list.size() != 0) {
				ItemStack item = (ItemStack) list.get(0);
				if (output == null) {
					//System.out.println("[AEC] Input was null!");
					return null;
				}
				return output.items.get(0);
			}
			return null;
		}

		public ArrayList getInputs() {
			ArrayList a = new ArrayList();
			List list = input.getInputs();
			if (list.size() != 0) {
				ItemStack item = (ItemStack) list.get(0);
				for (int i=1;i<=input.getAmount();i++) {
					a.add(item);
				}
			}
			return a;
		}
}
