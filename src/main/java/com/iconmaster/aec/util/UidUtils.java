package com.iconmaster.aec.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
/**
 * Use this class to encode/decode ItemStacks for HashMap use.
 * @author iconmaster
 *
 */
public class UidUtils {
	/**
	 * Gets the List you can use to put a ItemStack in a hash.
	 * @param item
	 * @return
	 */
	public static List getUID(ItemStack item) {
		if (item==null || item.getItem() == null) {return null;}
		return Arrays.asList(Item.itemRegistry.getIDForObject(item.getItem()),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0);
	}
	
	/**
	 * Gets the ItemStack that a uid encodes for.
	 * @param uid
	 * @return
	 */
	public static ItemStack getStackFromUid(List uid) {
		if (uid == null) {return null;}
		return new ItemStack((Item)Item.itemRegistry.getObjectById((Integer)uid.get(0)),1,(Integer)uid.get(1));
	}
}
