package com.iconmaster.aec.aether;

import java.util.HashMap;

import com.iconmaster.aec.util.UidUtils;

import net.minecraft.item.ItemStack;

/**
 * This class stores the reipes for the Aether Infuser. Use addRecipe to add your own Infuser recipes.
 * @author iconmaster
 *
 */
public class InfuserRegistry {
	private static HashMap recipes = new HashMap();
	
	/**
	 * Returns the list of Aether Infuser recipes. The map's keys are the UID of the input, and the values are the ItemStack output.
	 * @return
	 */
	public static HashMap getRecipes() {
		return recipes;
	}
	
	/**
	 * Adds a recipe to the Aether Infuser.
	 * @param input
	 * @param output
	 */
	public static void addRecipe(ItemStack input, ItemStack output) {
		recipes.put(UidUtils.getUID(input), output);
	}
	
	/**
	 * Given an input, returns the infused form or null if there is no recipe.
	 * @param input
	 * @return
	 */
	public static ItemStack getOutput(ItemStack input) {
		return (ItemStack) recipes.get(UidUtils.getUID(input));
	}
	
	/**
	 * For any given input, returns the amount of AV it needs to infuse it, NOT the output's AV. Returns 0 if there is no recipe for the input.
	 * @param input
	 * @return
	 */
	public static float getOutputAV(ItemStack input) {
		return AVRegistry.getAV((ItemStack) recipes.get(UidUtils.getUID(input)))*3;
	}
}
