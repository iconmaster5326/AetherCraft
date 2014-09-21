package com.iconmaster.aec.aether.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Make your dynamic recipe handlers implement this.
 * @author iconmaster
 *
 */
public interface IDynamicAVRecipeHandler<T> {
	
	/**
	 * Given a recipe, returns an ArrayList of inputs used to make it.
	 * @param recipe
	 * @return
	 */
	public ArrayList getInputs(T recipe);
	
	/**
	 * Given a recipe, returns an ItemStack that is the recipe's output.
	 * @param recipe
	 * @return
	 */
	public ItemStack getOutput(T recipe);
	
	/**
	 * Called when the recipe list is populated. Add your recipes to the ArrayLists in recipeList keyed by item UID. 
	 * Note that there needs to be an ArrayList of possible inputs at the value; you'll need to create a new list
	 * if it's null and THEN add your recipe to that list.
	 * @param recipeList
	 */
	public void populateRecipeList(HashMap recipeList);
	
}
