package com.iconmaster.aec.aether;

import java.util.HashMap;

import com.iconmaster.aec.util.UidUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AVRegistry {
	private static HashMap values = new HashMap();
	private static HashMap hardcoded = new HashMap();
	private static HashMap blacklist = new HashMap();
	private static HashMap unlocalizedNames = getAllNames();
	
	public static void setAV(ItemStack item,long av) {
		values.put(UidUtils.getUID(item),av);
	}
	
	/**
	 * Returns a hash with keys of unlocalized names and values of itemstacks. Used in looking up items.
	 * @return
	 */
	private static HashMap getAllNames() {
		HashMap map = new HashMap();
		for (int i=0;i<32000;i++) {
			Item item = Item.itemsList[i];
			if (Item.itemsList[i]!=null && item.getUnlocalizedName()!=null) {
				map.put(item.getUnlocalizedName(),new ItemStack(item));
			}
		}
		return map;
	}

	public static void setAV(String item,long av) {
		ItemStack stack = getItemFromString(item);
		if (stack == null) {return;}
		setAV(stack,av);
	}
	
	public static void setConfigAV(String item,long av) {
		setAV(item,av);
		ItemStack stack = getItemFromString(item);
		if (stack == null) {return;}
		hardcoded.put(UidUtils.getUID(stack),av);
		if (av==0L) {
			blacklist.put(UidUtils.getUID(stack),true);
		}
	}
	
	public static long getAV(ItemStack item) {
		Long av = (Long)values.get(UidUtils.getUID(item));
		if (av == null) {
			return 0;
		} else {
			return av;
		}
	}
	
	public static boolean isEntry(ItemStack item) {
		return values.get(UidUtils.getUID(item))==null;
	}
	
	public static boolean isHardcoded(ItemStack item) {
		return hardcoded.get(UidUtils.getUID(item))!=null;
	}
	
	public static boolean isBlacklisted(ItemStack item) {
		return blacklist.get(UidUtils.getUID(item))!=null;
	}
	
	public HashMap getValueMap() {
		return values;
	}
	
	public HashMap getValueHardcodedMap() {
		return hardcoded;
	}
	
	public HashMap getBlacklistMap() {
		return blacklist;
	}
	
	public static ItemStack getItemFromString(String s) {
		return (ItemStack) unlocalizedNames.get(s);
	}
}
