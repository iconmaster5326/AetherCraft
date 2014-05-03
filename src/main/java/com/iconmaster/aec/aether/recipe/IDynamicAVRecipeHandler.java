package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;

/**
 * Make your dynamic recipe handlers implement this.
 * @author iconmaster
 *
 */
public interface IDynamicAVRecipeHandler {
	
	/**
	 * Given a recipe, returns an ArrayList of inputs used to make it.
	 * @param recipe
	 * @return
	 */
	public ArrayList getInputs(Object recipe);
	
	/**
	 * Given a recipe, returns an ItemStack that is the recipe's output.
	 * @param recipe
	 * @return
	 */
	public ItemStack getOutput(Object recipe);
	
	/**
	 * Called when the reipe list is populated. Add your recipes to the ArrayLists in recipeList keyed by item UID.
	 * @param recipeList
	 */
	public void populateRecipeList(HashMap recipeList);
	
}
