package com.iconmaster.aec.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ModHelpers {
	//oh my god this is such a stupid hack, I hate TCon right now
	private static HashMap<Integer,Item> reverseHashRegistry = new HashMap<Integer, Item>();
	
	public static Class getTERecipeObject(String type) {
		try {
			Class manc = Class.forName("thermalexpansion.util.crafting."+type+"Manager");
			Class[] decs = manc.getDeclaredClasses();
			Class man = null;
			for (Class c : decs) {
				//System.out.println("[AEC TE] "+c.getName());
				if (c.getName().contains("Recipe"+type)) {
					man = c;
				}
			}
			return man;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setupTConMap() {
		for (Object key : Item.itemRegistry.getKeys()) {
			Item item = (Item) Item.itemRegistry.getObject(key);
			reverseHashRegistry.put(item.hashCode(), item);
		}
	}
	
	public static ItemStack decodeTConList(List<Integer> list) {
		Integer hash = list.get(0);
		Integer damage = list.get(1);
		
		Item item = reverseHashRegistry.get(hash);
		return new ItemStack(item,1,damage);
	}
	
	public static List<Integer> encodeTConList(ItemStack stack) {
		return Arrays.asList(stack.getItem().hashCode(),stack.getItemDamage());
	}
}
