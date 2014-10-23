package com.iconmaster.avlib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iconmaster on 9/23/2014.
 */
public abstract class AVInterpreter<T> {

	public HashMap<ItemData,T> interpret(HashMap<ItemData, List<RecipeData>> recipes) {
		int lastHash = 0;
		HashMap<ItemData,T> map = new HashMap<ItemData,T>();
		while (lastHash!=map.hashCode()) {
			pass(map,recipes,false);
			lastHash = map.hashCode();
		}
		pass(map,recipes,true);
		return map;
	}

	protected void pass(HashMap<ItemData,T> map, HashMap<ItemData, List<RecipeData>> recipes,boolean lastPass) {
		for (Map.Entry<ItemData, List<RecipeData>> entry : recipes.entrySet()) {
			T data = getValue(map,entry.getValue(),lastPass);
			if (data!=null) {
				map.put(entry.getKey(),data);
			}
		}
	}

	public abstract T getValue(HashMap<ItemData,T> map, List<RecipeData> recipe, boolean lastPass);
}
