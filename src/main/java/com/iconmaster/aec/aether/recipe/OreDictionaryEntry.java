package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

	public class OreDictionaryEntry {
		private ItemStack input;
		private ItemStack output;
		
		public OreDictionaryEntry(Item input, ItemStack output) {
			this.input = new ItemStack(input,1,0);
			this.output = output;
		}
		
		public OreDictionaryEntry(ItemStack input, ItemStack output) {
			this.input = input;
			this.output = output;
		}
		
		public ItemStack getOutput() {
			return output;
		}
	
		public ArrayList getInputs() {
			ArrayList a = new ArrayList();
			a.add(input);
			return a;
		}
}
