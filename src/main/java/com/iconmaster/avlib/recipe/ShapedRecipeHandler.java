package com.iconmaster.avlib.recipe;

import com.iconmaster.avlib.IRecipeHandler;
import com.iconmaster.avlib.RecipeData;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class ShapedRecipeHandler implements IRecipeHandler<ShapedRecipes> {
	@Override
	public RecipeData toRecipe(ShapedRecipes input) {
		return new RecipeData(input.getRecipeOutput(), Arrays.asList(input.recipeItems));
	}

	@Override
	public ArrayList getRecipes() {
		ArrayList a = new ArrayList<IRecipe>();
		for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
			a.add(recipe);
		}
		return a;
	}
}
