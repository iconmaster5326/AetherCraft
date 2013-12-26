package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.iconmaster.aec.aether.recipe.OreDictionaryEntry;
import com.iconmaster.aec.aether.recipe.OreDictionaryHandler;
import com.iconmaster.aec.aether.recipe.SmeltingRecipe;
import com.iconmaster.aec.aether.recipe.SmeltingRecipeHandler;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapedOreRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapedRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapelessOreRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapelessRecipeHandler;
import com.iconmaster.aec.util.UidUtils;

public class DynamicAVRegister {
	private static HashMap recipeList  = new HashMap();
	private static int recursions; //really hackish crash preventer
	private static HashMap handlers = getDefaultHandlers();

	public static void addDynamicValues() {
		//Put all the recipes in a map keyed by item ID. Value is an ArrayList of recipes that produce the item.
		Iterator it = handlers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			((IDynamicAVRecipeHandler)pairs.getValue()).populateRecipeList(recipeList);
		}
		
		//Begin to add recipes to list. Recursively calls getRecipeAV.
		it = recipeList.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	recursions = 0;
        	getItemAV((ArrayList)pairs.getValue());
        }
        
	}
	
	private static float getItemAV(ArrayList list) {
		if (list == null || list.size() == 0) {
			//System.out.println("Failed: no input list!");
			return 0;
		}
		ItemStack output = getOutput(list.get(0));
		if (AVRegistry.isHardcoded(output)) {
			return AVRegistry.getAbsoluteAV(output);
		}
		float av = Float.MAX_VALUE;
		HashMap lookedUp = new HashMap();
    	for (Object recipe : list) {
    		float rav = getRecipeAV(recipe,lookedUp);
    		if (rav >= 0) {
    			av = Math.min(av,rav);
    		}
    	}
    	return av;
	}

	private static float getRecipeAV(Object recipe, HashMap lookedOver) {
		ItemStack output = getOutput(recipe);
		if (output == null) { /*System.out.println("Failed: no output!");*/ return 0; }
		//System.out.println("Getting AV of "+getDebugName(output));
		List uid = UidUtils.getUID(output);
		ArrayList inputs = getInputs(recipe);
		if (inputs == null || inputs.size() == 0) { /*System.out.println("	Failed: Inputs were empty!");*/ return 0; }
//		if (lookedOver.get(uid) != null) {
//			//it's an infinite recursive recipe
//			System.out.println("	Failed: recursive recipe output!");
//			return 0;
//		}
		
		lookedOver.put(uid,true);
		
		float sum = 0;
		for (Object input : inputs) {
			ItemStack item = (ItemStack) input;
			if (item != null) {
				//System.out.println("	Found component "+getDebugName(item));
				
//				if (lookedOver.get(UidUtils.getUID(item)) != null) {
//					//it's an infinite recursive recipe
//					System.out.println("	Failed: recursive recipe input!");
//					return 0;
//				}
				float av = 0;
				if (!AVRegistry.isEntry(item)) {
					//System.out.println("		Looking up subrecipe... {");
					
					recursions++;
					if (recursions > 20) {
						//System.out.println("		}");
						//System.out.println("	Failed: Automatic recursion detection triggered!");
						return 0;
					}
					
					av = getItemAV((ArrayList)recipeList.get(UidUtils.getUID(item)));
					
					if (av==0 && !AVRegistry.isEntry(item)) {
						//System.out.println("		}");
						//System.out.println("	Failed: Geting subrecipe failed!");
						return 0;
					}
					//System.out.println("		}");
				}
				if (av==0) {av = AVRegistry.getAbsoluteAV(item);}
				//System.out.println("		Has AV of "+av);
				sum += av;
			}
		}
		if (output.stackSize == 0) {output.stackSize = 1;}
		if (!AVRegistry.isEntry(output) || AVRegistry.getAbsoluteAV(output)==0) {
			//System.out.println("	Adding an AV value of "+sum/output.stackSize);
			AVRegistry.setAV(output,sum/output.stackSize);
		} else {
			float oav = AVRegistry.getAbsoluteAV(output);	
			//System.out.println("	Adding an AV value of "+Math.min(oav,sum/output.stackSize));
			AVRegistry.setAV(output,Math.min(oav,sum/output.stackSize));
		}
		return sum/output.stackSize;
	}

	public static boolean isValidRecipe(Object recipe) {
		return handlers.get(recipe.getClass()) != null;
	}
	
	public static ArrayList getInputs(Object recipe) {
		IDynamicAVRecipeHandler handler = (IDynamicAVRecipeHandler) handlers.get(recipe.getClass());
		if (handler != null) {
			//System.out.println("[AEC] inputs from "+recipe.getClass());
			return handler.getInputs(recipe);
		}
		return null;
	}
	
	public static ItemStack getOutput(Object recipe) {
		IDynamicAVRecipeHandler handler = (IDynamicAVRecipeHandler) handlers.get(recipe.getClass());
		if (handler != null) {
			return handler.getOutput(recipe);
		}
		return null;
	}
	
	/**
	 * Takes an input ArrayList and replaces the OreDict references with something more manageable.
	 * @param inputs
	 * @return
	 */
	private static ArrayList flattenInputs(ArrayList inputs) {
		ArrayList ret = new ArrayList();
		for (Object input : inputs) {
			if (input instanceof ArrayList) {
				ArrayList s = (ArrayList)input;
				if (s.size() == 0) {return null;}
				float lav = Float.MAX_VALUE;
				Object lentry = s.get(0);
				for (Object entry : s) {
					float av = AVRegistry.getAV((ItemStack)entry);
					if (av != 0 && av < lav) {
						lav = av;
						lentry = entry;
					}
				}
				ret.add(lentry);
			} else {
				ret.add(input);
			}
		}
		return ret;
	}
	
	private static String getDebugName(ItemStack item) {
		if (item.getUnlocalizedName() == null) {
			return "ID "+((Integer)item.itemID).toString();
		} else {
			return item.getUnlocalizedName();
		}
	}
	
	public static void registerHandler(IDynamicAVRecipeHandler handler,Class recipeType) {
		registerHandler(handlers,handler,recipeType);
	}
	
	public static void registerHandler(HashMap map,IDynamicAVRecipeHandler handler,Class recipeType) {
		map.put(recipeType,handler);
	}
	
	public static HashMap getDefaultHandlers() {
		HashMap map = new HashMap();
		registerHandler(map,new ShapedRecipeHandler(),ShapedRecipes.class);
		registerHandler(map,new ShapelessRecipeHandler(),ShapelessRecipes.class);
		registerHandler(map,new ShapedOreRecipeHandler(),ShapedOreRecipe.class);
		registerHandler(map,new ShapelessOreRecipeHandler(),ShapelessOreRecipe.class);
		registerHandler(map,new SmeltingRecipeHandler(),SmeltingRecipe.class);
		registerHandler(map,new OreDictionaryHandler(),OreDictionaryEntry.class);
		return map;
	}
}
