package com.iconmaster.avlib;

import java.util.ArrayList;

/**
 * Created by iconmaster on 9/23/2014.
 */
public interface IRecipeHandler<T> {
	public RecipeData toRecipe(T input);
	public ArrayList getRecipes();
}
