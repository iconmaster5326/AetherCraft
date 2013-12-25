package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.config.AVConfigHandler;
import com.iconmaster.aec.util.UidUtils;

import cpw.mods.fml.common.registry.GameData;

public class AVRegistry {
	private static HashMap values ;
	private static HashMap hardcoded = new HashMap();
	private static HashMap unlocalizedNames = getAllNames();
	
	/**
	 * Returns a hash with keys of unlocalized names and values of <code>Item</code>s. Used in looking up items by config name.
	 * @return
	 */
	private static HashMap getAllNames() {
		HashMap map = new HashMap();
		for (int i=0;i<32000;i++) {
			try {
				Item item = Item.itemsList[i];
				if (item!=null) {
					if (item.getHasSubtypes() &&!item.isItemTool(new ItemStack(item))) {
						ArrayList li = new ArrayList();
						item.getSubItems(item.itemID, null, li);
						for (int j=0;j<=li.size()-1;j++) {
							//System.out.println("WRITING "+item.getUnlocalizedName(new ItemStack(item,1,j)));
							if (map.get(item.getUnlocalizedName(new ItemStack(item,1,j)))==null) {
								map.put(item.getUnlocalizedName(new ItemStack(item,1,j)),new ItemStack(item,1,j));
							}
						}
					} else if (item.getUnlocalizedName()!=null) {
						//System.out.println("WRITING "+item.getUnlocalizedName());
						map.put(item.getUnlocalizedName(),new ItemStack(item));
					}
				} 
			}catch (Exception e) {
				System.out.println("[AEC] cauugh unandled exception: "+e);	
			}
		}
	return map;
	}
	
	public static void setAV(ItemStack item,float av) {
		values.put(UidUtils.getUID(item),av);
	}

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
	
	public static void setConfigAV(ItemStack item, Float av) {
		if (item== null) {return;}
		setAV(item,av);
		hardcoded.put(UidUtils.getUID(item),av);
	}
	
	public static float getAV(ItemStack item) {
		if (item==null) {return 0;}
		if (item.itemID==Item.book.itemID) {
			return ((Float)values.get(Arrays.asList(Item.book.itemID,0)))*Float.parseFloat(AetherCraft.getOptions("evmultiplier")); //really hackish fix for books, which continue to act in a silly manner
		}
		Float av = (Float)values.get(UidUtils.getUID(item));
		if (av == null) {
			return 0;
		} else {
			if (item.getItem().isItemTool(item)) {
				av*=1-((float)item.getItemDamage()/item.getMaxDamage());
			}
			av*=Float.parseFloat(AetherCraft.getOptions("evmultiplier"));
			return av;
		}
	}
	
	public static float getAbsoluteAV(ItemStack item) {
		Float av = (Float)values.get(UidUtils.getUID(item));
		if (av == null) {
			return 0;
		} else {
			av*=Float.parseFloat(AetherCraft.getOptions("evmultiplier"));
			return av;
		}
	}
	
	public static boolean isEntry(ItemStack item) {
		return values.get(UidUtils.getUID(item))!=null;
	}
	
	public static boolean isHardcoded(ItemStack item) {
		return hardcoded.get(UidUtils.getUID(item))!=null;
	}
	
	public static HashMap getValueMap() {
		return values;
	}
	
	public static HashMap getValueHardcodedMap() {
		return hardcoded;
	}
	
	public static ItemStack getItemFromString(String s) {
		int meta = 0;
		if (s.contains("::")) {
			String[] subs = Pattern.compile("::").split(s);
			s = subs[0];
			meta = Integer.parseInt(subs[1]);
		}
		if (unlocalizedNames.get(s)==null) {
			System.out.println("Did not find name: "+s);
			return null;
		}
		//System.out.println("	FOUND "+s);
		return (ItemStack)unlocalizedNames.get(s);
	}

	public static void setValueMap(HashMap map) {
		values = map;
	}
	
	/**Re-evaluates the entire value system, checking dynamic recipes and config files alike.
	 */
	public static void reloadAllValues() {
		values = new HashMap();
		
		AVConfigHandler.loadAllConfigFiles();
		DynamicAVRegister.addDynamicValues();
		
	}
}
