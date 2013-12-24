package com.iconmaster.aec.aether;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public interface IDynamicAVRecipe {
	
	public ArrayList getInputs();
	
	public ItemStack getOutput();
	
}
