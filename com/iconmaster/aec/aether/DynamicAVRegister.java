package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.UidUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DynamicAVRegister {
	private static HashMap recipeList  = new HashMap();
	private static int recursions; //really hackish crash preventer

	public static void addDynamicValues() {
		//Put all the recipes in a map keyed by item ID. Value is an ArrayList of recipes that produce the item.
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			try
			{
				if (isValidRecipe(recipe)) {
					ItemStack output = getOutput(recipe);
					List uid = UidUtils.getUID(output);
					if (recipeList.get(uid) == null) {
						recipeList.put(uid, new ArrayList());
					}
					((ArrayList) recipeList.get(uid)).add(recipe);
				} else {
					System.out.println("Found new recipe class: "+recipe.getClass());
				}
			} catch (NullPointerException e) {
				//System.out.println("Invalid recipe state!");
				//avoiding crashes with MCPC+ since 2013!
			}
		}
		//put smelting recipes on the list too...
		Iterator it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	AVSmeltingRecipe recipe = new AVSmeltingRecipe((Integer)pairs.getKey(),(ItemStack)pairs.getValue());
			ItemStack output = getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
        }
        
        // AND meta-smelting. Hoo boy.
		it = FurnaceRecipes.smelting().getMetaSmeltingList().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	AVSmeltingRecipe recipe = new AVSmeltingRecipe(UidUtils.getStackFromUid((List)pairs.getKey()),(ItemStack)pairs.getValue());
			ItemStack output = getOutput(recipe);
			List uid = UidUtils.getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
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

	private static boolean isValidRecipe(Object recipe) {
		return recipe instanceof ShapedRecipes || recipe instanceof ShapelessRecipes || recipe instanceof ShapedOreRecipe || recipe instanceof ShapelessOreRecipe || recipe instanceof AVSmeltingRecipe;
	}
	
	private static ArrayList getInputs(Object recipe) {
		if (recipe instanceof ShapedRecipes) {
			return new ArrayList(Arrays.asList(((ShapedRecipes)recipe).recipeItems));
		} else if (recipe instanceof ShapelessRecipes) {
			return new ArrayList(((ShapelessRecipes)recipe).recipeItems);
		} else if (recipe instanceof ShapedOreRecipe) {
			return flattenInputs(new ArrayList(Arrays.asList(((ShapedOreRecipe)recipe).getInput())));
		} else if (recipe instanceof ShapelessOreRecipe) {
			return flattenInputs(((ShapelessOreRecipe)recipe).getInput());
		} else if (recipe instanceof IDynamicAVRecipe) {
			return ((IDynamicAVRecipe)recipe).getInputs();
		}
		return null;
	}
	
	private static ItemStack getOutput(Object recipe) {
		if (recipe instanceof ShapedRecipes) {
			return ((ShapedRecipes)recipe).getRecipeOutput();
		} else if (recipe instanceof ShapelessRecipes) {
			return ((ShapelessRecipes)recipe).getRecipeOutput();
		} else if (recipe instanceof ShapedOreRecipe) {
			return ((ShapedOreRecipe)recipe).getRecipeOutput();
		} else if (recipe instanceof ShapelessOreRecipe) {
			return ((ShapelessOreRecipe)recipe).getRecipeOutput();
		} else if (recipe instanceof IDynamicAVRecipe) {
			return ((IDynamicAVRecipe)recipe).getOutput();
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
}
