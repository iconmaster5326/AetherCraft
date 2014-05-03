package com.iconmaster.aec.util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.IAetherStorageItem;

/**
 * A collection of utilities used in finding stored AV in inventories.
 * @author iconmaster
 *
 */
public class InventoryUtils {
	
	/**
	 * Given an inventory, returns the amount of AV stored in the inventory's AV storing items.
	 * @param inv
	 * @return
	 */
	public static float getAVInInventory(IInventory inv) {
		float av = 0;
		for (int i=0;i<=inv.getSizeInventory()-1;i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (item != null && (item.getItem() instanceof IAetherStorageItem)) {
				av += ((IAetherStorageItem)item.getItem()).getAether(item);
			}
		}
		return av;
	}
	
	/**
	 * Takes an amount of AV from Aether-storing items in the inventory. Returns how much AV was actually retrieved.
	 * @param inv
	 * @param av
	 * @return
	 */
	public static float drainAVFromInventory(IInventory inv,float av) {
		float left = av;
		for (int i=0;i<=inv.getSizeInventory()-1;i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (left > 0 && item != null && (item.getItem() instanceof IAetherStorageItem)) {
				left -= ((IAetherStorageItem)item.getItem()).extractAether(item,left);
			}
		}
		return av-left;
	}

	public static ItemStack findItemStack(InventoryPlayer inventory, ItemStack item) {
		for (int i=0;i<inventory.getSizeInventory();i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (ItemStack.areItemStacksEqual(item, stack)) {
				return stack;
			}
		}
		return null;
	}
}
