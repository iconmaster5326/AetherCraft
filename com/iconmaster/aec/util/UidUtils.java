package com.iconmaster.aec.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

public class UidUtils {
	/**
	 * Gets the List you can use to put a ItemStack in a hash.
	 * @param item
	 * @return
	 */
	public static List getUID(ItemStack item) {
		return Arrays.asList(item.itemID,item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0);
	}
	
	/**
	 * Gets the ItemStack that a uid encodes for.
	 * @param uid
	 * @return
	 */
	public static ItemStack getStackFromUid(List uid) {
		return new ItemStack((Integer)uid.get(0),1,(Integer)uid.get(1));
	}
}