package com.iconmaster.avlib;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iconmaster on 9/23/2014.
 */
public abstract class NumericInterpreter extends AVInterpreter<Float> {
	@Override
	public Float getValue(HashMap<ItemData,Float> map, List<RecipeData> recipe, boolean lastPass) {
		float lowest = Float.MAX_VALUE;
		for (RecipeData data : recipe) {
			if (getItemValue(data.output)!=null) {
				return getItemValue(data.output);
			}
			Float n = getNumber(map,data,lastPass);
			if (n==null) {
				if (!lastPass) {
					return null;
				}
			} else {
				if (n<lowest) {
					lowest = n;
				}
			}
		}
		return lowest;
	}

	public Float getNumber(HashMap<ItemData,Float> map, RecipeData recipe, boolean lastPass) {
		return getNumber(map,recipe.inputs,lastPass);
	}

	public Float getNumber(HashMap<ItemData,Float> map, List recipe, boolean lastPass) {
		float sum = 0;
		for (Object input : recipe) {
			Float n = null;

			if (input instanceof List) {
				n = getNumber(map, (List)input, lastPass);
			} else if (input instanceof ItemData) {
				n = map.get((ItemData) input);
			}

			if (n!=null) {
				sum+=n;
			} else if (!lastPass) {
				return null;
			}
		}
		return sum;
	}

	public abstract Float getItemValue(ItemData item);
}
