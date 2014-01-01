package com.iconmaster.aec.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.iconmaster.aec.aether.IAetherStorageItem;
import com.iconmaster.aec.common.AetherCraft;

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
}
