package com.iconmaster.aec.aether.recipe.ic2;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
				item.stackSize = input.getAmount();
				a.add(item);
			}
			return a;
		}
}
