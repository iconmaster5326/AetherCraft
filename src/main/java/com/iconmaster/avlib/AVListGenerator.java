package com.iconmaster.avlib;

import com.iconmaster.avlib.flatten.ItemStackFlattener;
import com.iconmaster.avlib.recipe.ShapedRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class AVListGenerator {
	private HashMap<ItemData,List<RecipeData>> map = new HashMap<ItemData, List<RecipeData>>();

	private List rawRecipes = new ArrayList();

	private HashMap<Class,IRecipeHandler> handlers = new HashMap<Class,IRecipeHandler>();
	private HashMap<Class,IRecipeFlattener> flatteners = new HashMap<Class,IRecipeFlattener>();

	public AVListGenerator() {
		registerHandler(ShapedRecipes.class,new ShapedRecipeHandler());

		registerFlattener(ItemStack.class,new ItemStackFlattener());
	}

	public void registerHandler(Class clas, IRecipeHandler handler) {
		handlers.put(clas,handler);
	}

	public void registerFlattener(Class clas, IRecipeFlattener handler) {
		flatteners.put(clas,handler);
	}

	public HashMap<ItemData, List<RecipeData>> getMap() {
		for (Map.Entry<Class,IRecipeHandler> handler : handlers.entrySet()) {
			List recipes = handler.getValue().getRecipes();
			rawRecipes.addAll(recipes);
		}

		for (Object recipe : rawRecipes) {
			if (handlers.get(recipe.getClass())!=null) {
				RecipeData data = handlers.get(recipe.getClass()).toRecipe(recipe);
				if (data != null) {
					if (map.get(data.output) == null) {
						map.put(data.output, new ArrayList());
					}
					map.get(data.output).add(data);
				}
			}
		}

		for (Map.Entry<ItemData, List<RecipeData>> entry : map.entrySet()) {
			for (RecipeData recipe : entry.getValue()) {
				recipe.inputs = flatten(recipe.inputs);
			}
		}

		return map;
	}

	public List flatten(List list) {
		List a = new ArrayList();
		for (Object item : list) {
			if (item==null) {
				//System.out.println("Item was null!");
			} else if (item instanceof ItemData) {
				a.add(item);
			} else if (item instanceof List) {
				a.add(flatten((List) item));
			} else if (flatteners.get(item.getClass())!=null) {
				Object data = flatteners.get(item.getClass()).flatten(item);
				if (data!=null) {
					a.add(data);
				}
			} else {
				System.out.println("Unknown data type: "+item.getClass().getSimpleName());
			}
		}
		return a;
	}
}
