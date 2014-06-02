package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.iconmaster.aec.aether.flattener.ArrayFlattener;
import com.iconmaster.aec.aether.flattener.BlockFlattener;
import com.iconmaster.aec.aether.flattener.IC2InputFlattener;
import com.iconmaster.aec.aether.flattener.IFlattenerHandler;
import com.iconmaster.aec.aether.flattener.IntegerFlattener;
import com.iconmaster.aec.aether.flattener.ItemFlattener;
import com.iconmaster.aec.aether.flattener.ItemStackFlattener;
import com.iconmaster.aec.aether.flattener.ListFlattener;
import com.iconmaster.aec.aether.flattener.StringFlattener;
import com.iconmaster.aec.aether.recipe.EIOAlloySmelterHandler;
import com.iconmaster.aec.aether.recipe.EssenseRefinerHandler;
import com.iconmaster.aec.aether.recipe.FluidContainerHandler;
import com.iconmaster.aec.aether.recipe.IDynamicAVRecipeHandler;
import com.iconmaster.aec.aether.recipe.InfuserHandler;
import com.iconmaster.aec.aether.recipe.InfuserRecipe;
import com.iconmaster.aec.aether.recipe.OreDictionaryEntry;
import com.iconmaster.aec.aether.recipe.OreDictionaryHandler;
import com.iconmaster.aec.aether.recipe.ShapedOreRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapedRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapelessOreRecipeHandler;
import com.iconmaster.aec.aether.recipe.ShapelessRecipeHandler;
import com.iconmaster.aec.aether.recipe.SmeltingRecipe;
import com.iconmaster.aec.aether.recipe.SmeltingRecipeHandler;
import com.iconmaster.aec.aether.recipe.ae2.AEGrindingHandler;
import com.iconmaster.aec.aether.recipe.ae2.AEInscribingHandler;
import com.iconmaster.aec.aether.recipe.ae2.AEShapedCraftingHandler;
import com.iconmaster.aec.aether.recipe.ae2.AEShapelessCraftingHandler;
import com.iconmaster.aec.aether.recipe.bc.AssemblyRecipeHandler;
import com.iconmaster.aec.aether.recipe.bc.RefineryHandler;
import com.iconmaster.aec.aether.recipe.dartcraft.DartCraftingHandler;
import com.iconmaster.aec.aether.recipe.dartcraft.DartGrindingHandler;
import com.iconmaster.aec.aether.recipe.dartcraft.DartShapelessCraftingHandler;
import com.iconmaster.aec.aether.recipe.forestry.BottlerHandler;
import com.iconmaster.aec.aether.recipe.forestry.CarpenterHandler;
import com.iconmaster.aec.aether.recipe.forestry.FabricatorHandler;
import com.iconmaster.aec.aether.recipe.forestry.FabricatorSmeltingHandler;
import com.iconmaster.aec.aether.recipe.forestry.FermenterHandler;
import com.iconmaster.aec.aether.recipe.forestry.ForestryCraftingRecipeHandler;
import com.iconmaster.aec.aether.recipe.forestry.MoistenerHandler;
import com.iconmaster.aec.aether.recipe.forestry.StillHandler;
import com.iconmaster.aec.aether.recipe.ic2.IC2CraftingHandler;
import com.iconmaster.aec.aether.recipe.ic2.SimpleIC2Recipe;
import com.iconmaster.aec.aether.recipe.ic2.SimpleIC2RecipeHandler;
import com.iconmaster.aec.aether.recipe.railcraft.BlastFurnaceHandler;
import com.iconmaster.aec.aether.recipe.railcraft.CokeOvenHandler;
import com.iconmaster.aec.aether.recipe.railcraft.RollingMachineHandler;
import com.iconmaster.aec.aether.recipe.tcon.AlloyHandler;
import com.iconmaster.aec.aether.recipe.tcon.CastingHandler;
import com.iconmaster.aec.aether.recipe.tcon.DetailingHandler;
import com.iconmaster.aec.aether.recipe.tcon.TConSmeltingHandler;
import com.iconmaster.aec.aether.recipe.te3.CrucibleHandler;
import com.iconmaster.aec.aether.recipe.te3.InductionSmelterHandler;
import com.iconmaster.aec.aether.recipe.te3.PulverizerHandler;
import com.iconmaster.aec.aether.recipe.te3.TransposerHandler;
import com.iconmaster.aec.util.ModHelpers;
import com.iconmaster.aec.util.UidUtils;

import cpw.mods.fml.common.Loader;

/**
 * This class is called when dynamic AV values are assigned.
 * @author iconmaster
 *
 */
public class DynamicAVRegister {
	private static HashMap<List,ArrayList> recipeList  = new HashMap<List, ArrayList>();
	private static HashMap<Class,IDynamicAVRecipeHandler> handlers = getDefaultHandlers();
	private static HashMap<Class,IFlattenerHandler> flatteners = getDefaultFlatteners();

	/**
	 * Assigns all items with recipes dynamic AVs.
	 */
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
        	HashMap looked = new HashMap();
        	Map.Entry pairs = (Map.Entry)it.next();
        	getItemAV(UidUtils.getStackFromUid((List) pairs.getKey()),looked);
        }
        
	}
	
	private static float getItemAV(ItemStack output,HashMap looked) {
		if (AVRegistry.isHardcoded(output)) {
			return AVRegistry.getAbsoluteAV(output);
		}
		ArrayList list = (ArrayList) recipeList.get(UidUtils.getUID(output));
		if (list == null || list.size() == 0) {
//			System.out.println("Failed: no input list!");
			return 0;
		}
		float av = Float.MAX_VALUE;
    	for (Object recipe : list) {
    		float rav = getRecipeAV(recipe,looked);
    		if (rav >= 0) {
    			av = Math.min(av,rav);
    		}
    	}
    	return av;
	}

	private static float getRecipeAV(Object recipe,HashMap looked) {
		ItemStack output = getOutput(recipe);
		if (output == null) {
//			System.out.println("Failed: no output!");
			return 0;
		}
//		System.out.println("Getting AV of "+getDebugName(output));
		List uid = UidUtils.getUID(output);
		ArrayList inputs = getInputs(recipe);
		if (inputs == null || inputs.size() == 0) {
//			System.out.println("	Failed: Inputs were empty!");
			return 0;
		}
		
		float sum = 0;
		for (Object input : inputs) {
			
			if (input != null && input instanceof ItemStack) {
				ItemStack item = (ItemStack) input;
//				System.out.println("	Found component "+getDebugName(item));
				
				float av = 0;
				if (!AVRegistry.isEntry(item)) {
//					System.out.println("		Looking up subrecipe... {");
					
					if (looked.get(UidUtils.getUID(item))!=null) {
//						System.out.println("	Failed: Automatic recursion detection triggered!");
						return 0;
					}
					looked.put(UidUtils.getUID(item),true);
					av = getItemAV(item,looked)*item.stackSize;
					
					if (av==0 && !AVRegistry.isEntry(item)) {
//						System.out.println("		}");
//						System.out.println("	Failed: Geting subrecipe failed!");
						return 0;
					}
					//System.out.println("		}");
				}
				if (av<=0) {av = AVRegistry.getAbsoluteAV(item)*item.stackSize;}
				if (av<=0) {
//					System.out.println("Failed: No AV at all!"); 
					return 0;
				}
//				System.out.println("		Has AV of "+av);
				sum += av;
			}
		}
		if (output.stackSize == 0) {output.stackSize = 1;}
		if (!AVRegistry.isEntry(output) || AVRegistry.getAbsoluteAV(output)==0) {
//			System.out.println("	Adding an AV value of "+sum/output.stackSize);
			AVRegistry.setAV(output,sum/output.stackSize);
		} else {
			float oav = AVRegistry.getAbsoluteAV(output);	
//			System.out.println("	Adding an AV value of "+Math.min(oav,sum/output.stackSize));
			AVRegistry.setAV(output,Math.min(oav,sum/output.stackSize));
		}
		return sum/output.stackSize;
	}

	public static boolean isValidRecipe(Object recipe) {
		if (handlers.get(recipe.getClass()) != null) {return true;}
		Iterator it = handlers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (((Class) pairs.getKey()).isAssignableFrom(recipe.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given a recipe, returns the ArrayList of inputs that were used to make it.
	 * @param recipe
	 * @return
	 */
	public static ArrayList<ItemStack> getInputs(Object recipe) {
		//System.out.println("recipe: "+recipe+" for "+String.valueOf(getOutput(recipe)));
		IDynamicAVRecipeHandler handler = getHandler(recipe);
		if (handler==null) {
			//System.out.println("	was null!");
			return null;
		} else {
			//System.out.println("	was "+handler.getClass());
			return flattenInputs(handler.getInputs(recipe));
		}
	}
	
	/**
	 * Given a recipe, returns the item that is its output.
	 * @param recipe
	 * @return
	 */
	public static ItemStack getOutput(Object recipe) {
		IDynamicAVRecipeHandler handler = getHandler(recipe);
		if (handler==null) {
			return null;
		} else {
			return handler.getOutput(recipe);
		}
	}
	
	/**
	 *  Registers a new dynamic recipe handler, given a handler object and the class that the handler should be called on when the register encounters a recipe of given class.
	 * @param handler
	 * @param recipeType
	 */
	public static void registerHandler(IDynamicAVRecipeHandler handler,Class recipeType) {
		registerHandler(handlers,handler,recipeType);
	}
	
	public static void registerHandler(HashMap map,IDynamicAVRecipeHandler handler,Class recipeType) {
		map.put(recipeType,handler);
	}
	
	public static HashMap<Class,IDynamicAVRecipeHandler> getDefaultHandlers() {
		HashMap<Class,IDynamicAVRecipeHandler> map = new HashMap<Class,IDynamicAVRecipeHandler>();
		registerHandler(map,new ShapedRecipeHandler(),ShapedRecipes.class);
		registerHandler(map,new ShapelessRecipeHandler(),ShapelessRecipes.class);
		registerHandler(map,new ShapedOreRecipeHandler(),ShapedOreRecipe.class);
		registerHandler(map,new ShapelessOreRecipeHandler(),ShapelessOreRecipe.class);
		registerHandler(map,new SmeltingRecipeHandler(),SmeltingRecipe.class);
		registerHandler(map,new OreDictionaryHandler(),OreDictionaryEntry.class);
		registerHandler(map,new FluidContainerHandler(),FluidContainerData.class);
		registerHandler(map,new InfuserHandler(),InfuserRecipe.class);
		if(Loader.isModLoaded("ThermalExpansion")) {
			try {
				registerHandler(map,new PulverizerHandler(),ModHelpers.getTERecipeObject("Pulverizer"));
				registerHandler(map,new InductionSmelterHandler(),ModHelpers.getTERecipeObject("Smelter"));
				registerHandler(map,new CrucibleHandler(),ModHelpers.getTERecipeObject("Crucible"));
				registerHandler(map,new TransposerHandler(),ModHelpers.getTERecipeObject("Transposer"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("IC2")) {
			registerHandler(map,new SimpleIC2RecipeHandler(),SimpleIC2Recipe.class);
			try {
				registerHandler(map,new IC2CraftingHandler("ic2.core.AdvRecipe"),Class.forName("ic2.core.AdvRecipe"));
				registerHandler(map,new IC2CraftingHandler("ic2.core.AdvShapelessRecipe"),Class.forName("ic2.core.AdvShapelessRecipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("appliedenergistics2")) {
			try {
				registerHandler(map,new AEShapedCraftingHandler(),Class.forName("appeng.recipes.game.ShapedRecipe"));
				registerHandler(map,new AEShapelessCraftingHandler(),Class.forName("appeng.recipes.game.ShapelessRecipe"));
				registerHandler(map,new AEGrindingHandler(),Class.forName("appeng.api.features.IGrinderEntry"));
				registerHandler(map,new AEInscribingHandler(),Class.forName("appeng.recipes.handlers.Inscribe$InscriberRecipe"));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("BuildCraft|Core")) {
			try {
				registerHandler(map,new AssemblyRecipeHandler(),Class.forName("buildcraft.api.recipes.IAssemblyRecipeManager$IAssemblyRecipe"));
				registerHandler(map,new RefineryHandler(),Class.forName("buildcraft.api.recipes.IRefineryRecipeManager$IRefineryRecipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("Forestry")) {
			try {
				registerHandler(map,new ForestryCraftingRecipeHandler(),Class.forName("forestry.core.interfaces.IDescriptiveRecipe"));
				
				registerHandler(map,new BottlerHandler(),Class.forName("forestry.factory.gadgets.MachineBottler$Recipe"));
				registerHandler(map,new CarpenterHandler(),Class.forName("forestry.factory.gadgets.MachineCarpenter$Recipe"));
				registerHandler(map,new FabricatorHandler(),Class.forName("forestry.factory.gadgets.MachineFabricator$Recipe"));
				registerHandler(map,new FabricatorSmeltingHandler(),Class.forName("forestry.factory.gadgets.MachineFabricator$Smelting"));
				registerHandler(map,new FermenterHandler(),Class.forName("forestry.factory.gadgets.MachineFermenter$Recipe"));
				registerHandler(map,new StillHandler(),Class.forName("forestry.factory.gadgets.MachineStill$Recipe"));
				registerHandler(map,new MoistenerHandler(),Class.forName("forestry.factory.gadgets.MachineMoistener$Recipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("Railcraft")) {
			try {
				registerHandler(map,new BlastFurnaceHandler(),Class.forName("mods.railcraft.api.crafting.IBlastFurnaceRecipe"));
				registerHandler(map,new CokeOvenHandler(),Class.forName("mods.railcraft.api.crafting.ICokeOvenRecipe"));
				registerHandler(map,new RollingMachineHandler(),RollingMachineHandler.class); //the second argument is a dummy; rolling machine recipes extends ShapedRecipe
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("DartCraft")) {
			try {
				registerHandler(map,new DartCraftingHandler(),Class.forName("bluedart.core.recipes.ShapedDartCrafting"));
				registerHandler(map,new DartShapelessCraftingHandler(),Class.forName("bluedart.core.recipes.MisshapenDartCrafting"));
				registerHandler(map,new DartGrindingHandler(),Class.forName("bluedart.api.recipe.IForceGrindRecipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("arsmagica2")) {
			try {
				registerHandler(map,new EssenseRefinerHandler(),Class.forName("am2.items.RecipeArsMagica"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("TConstruct")) {
			try {
				ModHelpers.setupTConMap();
				
				registerHandler(map,new TConSmeltingHandler(),TConSmeltingHandler.TConSmeltingEntry.class);
				registerHandler(map,new AlloyHandler(),Class.forName("tconstruct.library.crafting.AlloyMix"));
				registerHandler(map,new CastingHandler(),Class.forName("tconstruct.library.crafting.CastingRecipe"));
				registerHandler(map,new DetailingHandler(),Class.forName("tconstruct.library.crafting.Detailing$DetailInput"));
				//registerHandler(map,new DryingHandler(),Class.forName("tconstruct.library.crafting.DryingRackRecipes$DryingRecipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Loader.isModLoaded("EnderIO")) {
			try {
				registerHandler(map,new EIOAlloySmelterHandler(),Class.forName("crazypants.enderio.machine.alloy.IAlloyRecipe"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 *  Registers a new object-to-ItemStack converter, given a handler object and the class that the handler should be called on when a certain non-ItemStack is found in an input list.
	 * @param handler
	 * @param inputType
	 */
	public static void registerFlattener(IFlattenerHandler handler,Class inputType) {
		registerFlattener(flatteners,handler,inputType);
	}
	
	public static void registerFlattener(HashMap<Class,IFlattenerHandler> map,IFlattenerHandler handler,Class inputType) {
		map.put(inputType,handler);
	}
	
	public static HashMap<Class,IFlattenerHandler> getDefaultFlatteners() {
		HashMap<Class,IFlattenerHandler> map = new HashMap<Class,IFlattenerHandler>();
		
		registerFlattener(map,new ItemStackFlattener(),ItemStack.class);
		registerFlattener(map,new ListFlattener(),List.class);
		registerFlattener(map,new StringFlattener(),String.class);
		registerFlattener(map,new ItemFlattener(),Item.class);
		registerFlattener(map,new BlockFlattener(),Block.class);
		registerFlattener(map,new IntegerFlattener(),Integer.class);
		registerFlattener(map,new ArrayFlattener(),Object[].class);
		
		if (Loader.isModLoaded("IC2")) {
			try {
				registerFlattener(map,new IC2InputFlattener(),Class.forName("ic2.api.recipe.IRecipeInput"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}
	
	/**
	 * Takes an input ArrayList and replaces the OreDict references with something more manageable.
	 * @param inputs
	 * @return
	 */
	public static ArrayList<ItemStack> flattenInputs(Collection inputs) {
		if (inputs==null) {return null;}
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		for (Object input : inputs) {
			if (input != null) {
				IFlattenerHandler handler = getFlattener(input);
				if (handler != null) {
					ItemStack stack = handler.flatten(input);
					if (stack!=null) {
						ret.add(stack);
					} else {
						//Can't finish recipe, handler failed
						//System.out.println("	Handler "+handler.getClass()+" failed!");
						return null;
					}
				} else {
					//Can't finish recipe, has unhandled item
					System.out.println("[AEC] Input type "+input.getClass()+" not recognised!");
					return null;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Like flattenInputs, but won't return null if an item isn't flattenable.
	 * @param input
	 * @return
	 */
	public static ArrayList<ItemStack> flattenInputsPassively(Collection inputs) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if (inputs==null) {return ret;}
		for (Object input : inputs) {
			if (input != null) {
				IFlattenerHandler handler = getFlattener(input);
				if (handler != null) {
					ItemStack stack = handler.flatten(input);
					if (stack!=null) {
						ret.add(stack);
					} else {
						//handler failed
						//System.out.println("	Passive: Handler "+handler.getClass()+" failed!");
					}
				} else {
					//has unhandled item
					System.out.println("[AEC] Input type "+input.getClass()+" not recognised!");
				}
			}
		}
		return ret;
	}
	
	public static IDynamicAVRecipeHandler getHandler(Object recipe) {
		IDynamicAVRecipeHandler handler = handlers.get(recipe.getClass());
		if (handler != null) {
			return handler;
		} else {
			Iterator it = handlers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				if (((Class) pairs.getKey()).isAssignableFrom(recipe.getClass())) {
					return ((IDynamicAVRecipeHandler)pairs.getValue());
				}
			}
		}
		return null;
	}
	
	public static IFlattenerHandler getFlattener(Object input) {
		IFlattenerHandler handler = flatteners.get(input.getClass());
		if (handler != null) {
			return handler;
		} else {
			Iterator it = flatteners.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				if (((Class) pairs.getKey()).isAssignableFrom(input.getClass())) {
					return ((IFlattenerHandler)pairs.getValue());
				}
			}
		}
		return null;
	}
}
