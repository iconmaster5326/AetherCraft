package com.iconmaster.aec.aether.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.iconmaster.aec.util.UidUtils;

public class OreDictionaryHandler implements IDynamicAVRecipeHandler {

	@Override
	public ArrayList getInputs(Object recipe) {
		return ((OreDictionaryEntry)recipe).getInputs();
	}

	@Override
	public ItemStack getOutput(Object recipe) {
		return ((OreDictionaryEntry)recipe).getOutput();
	}
	
	@Override
	public void populateRecipeList(HashMap recipeList) {
		for (String ore : OreDictionary.getOreNames()) {
			for (Object input : OreDictionary.getOres(ore)) {
				for (Object output : OreDictionary.getOres(ore)) {
					if (input != output) {
						List uid = UidUtils.getUID((ItemStack) output);
						if (recipeList.get(uid) == null) {
							recipeList.put(uid, new ArrayList());
						}
						((ArrayList) recipeList.get(uid)).add(new OreDictionaryEntry((ItemStack)input,(ItemStack)output));
					}
				}
			}
		}
	}
}
