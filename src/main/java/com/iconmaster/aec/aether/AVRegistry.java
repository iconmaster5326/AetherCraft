package com.iconmaster.aec.aether;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.config.AVConfigHandler;
import com.iconmaster.aec.util.UidUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A class that holds item-AV pairings for all items.
 * @author iconmaster
 *
 */
public class AVRegistry {
	private static HashMap values ;
	private static HashMap hardcoded = new HashMap();
	private static int usedMetas = 0;
	private static HashMap fluids = new HashMap();
	
	/**
	 * Sets the AV of a given ItemStack.
	 * @param item
	 * @param av
	 */
	public static void setAV(ItemStack item,float av) {
		values.put(UidUtils.getUID(item),av);
	}

	/**
	 * Sets the AV of an item given by it's configuration file (i.e. unlocalized) name.
	 * @param item
	 * @param av
	 */
	public static void setAV(String item,float av) {
		ItemStack stack = getItemFromString(item);
		if (stack == null) {return;}
		setAV(stack,av);
	}
	
	/**
	 * A variant of setAV used only in setting the AV of things defined in config files. Makes sure that these values are not overriden by the dynamic register.
	 * @param item
	 * @param av
	 */
	public static void setConfigAV(String item,float av) {
		//System.out.println("Setting config of "+item+" to "+av);
		setConfigAV(getItemFromString(item),av);
	}
	
	/**
	 * A variant of setAV used only in setting the AV of things defined in config files. Makes sure that these values are not overriden by the dynamic register.
	 * @param item
	 * @param av
	 */
	public static void setConfigAV(ItemStack item, Float av) {
		if (item== null) {return;}
		setAV(item,av);
		hardcoded.put(UidUtils.getUID(item),av);
	}
	
	/**
	 * Given an ItemStack, returns its AV. Returns 0 if there's no pairing for the item.
	 * @param item
	 * @return
	 */
	public static float getAV(ItemStack item) {
		if (item==null) {return 0;}
		if (Item.getIdFromItem(item.getItem())==Item.getIdFromItem(Items.book)) {
			return ((Float)values.get(Arrays.asList(Item.getIdFromItem(Items.book),0))); //really hackish fix for books, which continue to act in a silly manner
		}
		Float av = (Float)values.get(UidUtils.getUID(item));
		if (av == null) {
			return 0;
		} else {
			if (item.getItem().isItemTool(item)) {
				av*=1-((float)item.getItemDamage()/item.getMaxDamage());
			}
			return av;
		}
	}
	
	/**
	 * Given an ItemStack, returns its AV. Does not take into account tool durability. Returns 0 if there's no pairing for the item.
	 * @param item
	 * @return
	 */
	public static float getAbsoluteAV(ItemStack item) {
		Float av = (Float)values.get(UidUtils.getUID(item));
		if (av == null) {
			return 0;
		} else {
			return av;
		}
	}
	
	/**
	 * Returns true if an item has an AV pairing.
	 * @param item
	 * @return
	 */
	public static boolean isEntry(ItemStack item) {
		return values.get(UidUtils.getUID(item))!=null;
	}
	
	/**
	 * Returns true if an item was given an AV via config file.
	 * @param item
	 * @return
	 */
	public static boolean isHardcoded(ItemStack item) {
		return hardcoded.get(UidUtils.getUID(item))!=null;
	}
	
	/**
	 * Returns the map of AVs. Keys are item UIDs, values are floats.
	 * @return
	 */
	public static HashMap getValueMap() {
		return values;
	}
	
	/**
	 * Returns the map of hardcoded items. Keys are item UIDs, values do not matter.
	 * @return
	 */
	public static HashMap getValueHardcodedMap() {
		return hardcoded;
	}
	
	/**
	 * Given a string, returns the ItemStack that the item represents. Used in configuration files.
	 * @param s
	 * @return
	 */
	public static ItemStack getItemFromString(String s) {
		Integer meta = null;
		
		if (s.contains("::")) {
			String[] subs = Pattern.compile(Pattern.quote("::")).split(s);
			s = subs[0];
			meta = Integer.parseInt(subs[1]);
			//System.out.println("[AEC] :: case detected. String is"+s+". Meta is "+meta);
		}
		
		if (Item.itemRegistry.getObject(s)!=null) {
			Item result = (Item) Item.itemRegistry.getObject(s);
			if (meta != null) {
				return new ItemStack(result,1,meta);
			} else {
				return new ItemStack(result,1);
			}
		}
		return null;
	}

	/**
	 * Sets the AV value map. Please don't mess with this.
	 * @param map
	 */
	public static void setValueMap(HashMap map) {
		values = map;
	}
	
	/**Re-evaluates the entire value system, checking dynamic recipes and config files alike.
	 */
	public static void reloadAllValues() {
		values = new HashMap();
		
		AVConfigHandler.loadAllConfigFiles();
		//DynamicAVRegister.addDynamicValues();
		
	}
	
	public static void reloadClientValues(HashMap stringValues) {
		values = new HashMap();
		
		AVConfigHandler.loadNetworkConfigFile(stringValues);
		DynamicAVRegister.addDynamicValues();
		
	}
	
	/**
	 * Returns a new dummy item worth the given AV. Used in some dynamic calculations.
	 */
	public static ItemStack newDummy(float av) {
		ItemStack dummy = newDummy();
		setAV(dummy,av);
		return dummy;
	}
	
	public static ItemStack newDummy() {
		ItemStack dummy = new ItemStack(AetherCraft.dummy,1,usedMetas);
		usedMetas++;
		return dummy;
	}
	
	public static void setAV(Fluid fluid,float av) {
		ItemStack stack = newDummy(av);
		fluids.put(fluid, stack);
		setAV(stack,av);
	}
	
	public static float getAV(Fluid fluid) {
		ItemStack stack = (ItemStack) fluids.get(fluid);
		return getAV(stack);
	}
	
	public static ItemStack createFluidStack(Fluid fluid,int mb) {
		ItemStack stack = (ItemStack) fluids.get(fluid);
		if (stack==null) {
			stack = newDummy();
			fluids.put(fluid,stack);
		}
		ItemStack newStack = new ItemStack(stack.getItem(),mb,stack.getItemDamage());
		return newStack;
	}

}
