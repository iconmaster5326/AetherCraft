package com.iconmaster.aec.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iconmaster.aec.common.AetherCraft;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DynamicAVRegister {
	private static HashMap<String, Integer> values  = AetherCraft.getAetherValuesMap();
	private static HashMap recipeList  = new HashMap();

	public static void addDynamicValues() {
		//Put all the recipes in a map keyed by item ID. Value is an ArrayList of recipes that produce the item.
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			try
			{
				if (isValidRecipe(recipe)) {
					ItemStack output = getOutput(recipe);
					List uid = getUID(output);
					if (recipeList.get(uid) == null) {
						recipeList.put(uid, new ArrayList());
					}
					((ArrayList) recipeList.get(uid)).add(recipe);
				}
			} catch (NullPointerException e) {
				System.out.println("Invalid recipe state!");
				//avoiding crashes with MCPC+ since 2013!
			}
		}
		//put smelting recipes on the list too
		Iterator it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	AVSmeltingRecipe recipe = new AVSmeltingRecipe((Integer)pairs.getKey(),(ItemStack)pairs.getValue());
			ItemStack output = getOutput(recipe);
			List uid = getUID(output);
			if (recipeList.get(uid) == null) {
				recipeList.put(uid, new ArrayList());
			}
			((ArrayList) recipeList.get(uid)).add(recipe);
        }
        	
		
		//Begin to add recipes to list. Recursively calls getRecipeAV.
		it = recipeList.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	getItemAV((ArrayList)pairs.getValue());
        }
        
	}
	
	private static int getItemAV(ArrayList list) {
		if (list == null) {
			System.out.println("Failed: no input list!");
			return 0;
		}
		int av = Integer.MAX_VALUE;
    	for (Object recipe : list) {
    		int rav = getRecipeAV(recipe,new HashMap());
    		if (rav != 0) {
    			av = Math.min(av,rav);
    		}
    	}
    	return av;
	}

	private static int getRecipeAV(Object recipe, HashMap lookedOver) {
		ItemStack output = getOutput(recipe);
		if (output == null) { System.out.println("Failed: no output!"); return 0; }
		System.out.println("Getting AV of item id "+getDebugName(output));
		List uid = getUID(output);
		ArrayList inputs = getInputs(recipe);
		if (inputs == null || inputs.size() == 0) { System.out.println("	Failed: Inputs were empty!"); return 0; }
		if (lookedOver.get(uid) != null) {
			//it's an infinite recursive recipe
			System.out.println("	Failed: recursive recipe!");
			return 0;
		}
		
		lookedOver.put(uid,true);
		
		int sum = 0;
		for (Object input : inputs) {
			ItemStack item = (ItemStack) input;
			//if (item==null) { System.out.println("	Failed: input item was null!!"); return false; }
			if (item != null) {
				System.out.println("	Found component "+getDebugName(item));
				
				int av = AetherCraft.getAetherValueByItemStack(item);
				if (av == 0) {
					System.out.println("		Looking up subrecipe... {");
					
					av = getItemAV((ArrayList) recipeList.get(getUID(item)));
					if (av==0) {
						System.out.println("		}");
						System.out.println("	Failed: Geting subrecipe failed!");
						return 0;
					}
					System.out.println("		}");
				}
				System.out.println("		Has AV of "+av);
				sum += av;
			}
		}
		
		int oav = AetherCraft.getAetherValueByItemStack(output);	
		if (oav==0) {
			System.out.println("	Adding an AV value of "+sum/output.stackSize);
			values.put(output.itemID+(output.getHasSubtypes()?":"+output.getItemDamage():""),sum/output.stackSize);
		} else {
			System.out.println("	Adding an AV value of "+Math.min(oav,sum/output.stackSize));
			values.put(output.itemID+(output.getHasSubtypes()?":"+output.getItemDamage():""),Math.min(oav,sum/output.stackSize));
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
		} else if (recipe instanceof AVSmeltingRecipe) {
			ArrayList a = new ArrayList();
			a.add(((AVSmeltingRecipe)recipe).getInput());
			return a;
		}
		return null;
	}
	
	/**
	 * Gets the List you can use to put a ItemStack in a hash.
	 * @param item
	 * @return
	 */
	private static List getUID(ItemStack item) {
		return Arrays.asList(item.itemID,item.getItemDamage());
	}
	
	/**
	 * Gets the ItemStack that a uid encodes for.
	 * @param uid
	 * @return
	 */
	private static ItemStack getStackFromUid(List uid) {
		return new ItemStack((Integer)uid.get(0),1,(Integer)uid.get(1));
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
		} else if (recipe instanceof AVSmeltingRecipe) {
			return ((AVSmeltingRecipe)recipe).getOutput();
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
				ret.add(s.get(0));
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
	/*
 	public static void registerDynamicRecipes() {
		
		recipeList = new HashMap();
		int invalid = 0;
		
		Debug.print("Looking for dynamic grist conversions...");
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			try
			{
				if (recipe instanceof ShapedRecipes) {
					ShapedRecipes newRecipe = (ShapedRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessRecipes) {
					ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapedOreRecipe) {
					ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessOreRecipe) {
					ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else {
					//Debug.print("Found the recipe for unkown format: "+recipe.getClass());
					invalid++;
				}
			}
			catch (NullPointerException e)
			{
				Debug.printf("a null pointer exception was thrown for %s", recipe);
			}
		}
		Debug.print("Found "+recipeList.size()+" valid recipes, and "+invalid+" unknown ones.");
		
		Debug.print("Calculating grist conversion...");
	   	Iterator it = recipeList.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	//Debug.print("Getting recipe with key"+pairs.getKey()+" and value "+pairs.getValue());
        	lookedOver = new HashMap();
        	getRecipe(pairs.getValue());
        }
        
        Debug.print("Done. Added "+returned+" grist conversions.");
	}
	
	private static boolean getRecipe(Object recipe) {
		if (recipe instanceof ShapedRecipes) {
			////Debug.print("found shaped recipe. Output of "+"ITEM");
			ShapedRecipes newRecipe = (ShapedRecipes) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				////Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (ItemStack item : newRecipe.recipeItems) {
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapelessRecipes) {
			//Debug.print("found shapeless recipe. Output of "+"ITEM");
			ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.recipeItems) {
				ItemStack item = (ItemStack) obj;
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapedOreRecipe) {
			//Debug.print("found shaped oredict recipe. Output of "+"ITEM");
			ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj instanceof ArrayList) {
					if (((ArrayList) obj).size() == 0) {
						//Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapelessOreRecipe) {
			//Debug.print("found shapeless oredict recipe. Output of "+"ITEM");
			ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj instanceof ArrayList) {
					if (((ArrayList) obj).size() == 0) {
						//Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else {
			//Debug.print("found other recipe class: "+recipe.getClass());
		}
		
		returned ++;
		return true;
	}
	 */
}
