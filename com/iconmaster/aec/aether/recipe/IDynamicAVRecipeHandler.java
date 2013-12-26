package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public interface IDynamicAVRecipeHandler {
	
	public ArrayList getInputs(Object recipe);
	
	public ItemStack getOutput(Object recipe);
	
}
