package com.iconmaster.aec.util;

import java.util.ArrayList;
import java.util.List;

import com.iconmaster.aec.common.AetherCraft;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class AecInvUtils {
	public static ItemStack[] getAllISInInventory(IInventory inventory,
			int itemId) {
		if (inventory == null) {
			return null;
		}
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.itemID == itemId) {
				result.add(stack);
			}
		}
		return result.toArray(new ItemStack[result.size()]);
	}

	public static ItemStack[] getAllISInArray(ItemStack[] inventory, int itemId) {
		if (inventory == null) {
			return null;
		}
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null && stack.itemID == itemId) {
				result.add(stack);
			}
		}
		return result.toArray(new ItemStack[result.size()]);
	}

	public static ItemStack[] getAllBatteriesWithEnergyAboveOrEqual(
			ItemStack[] batteries, int aboveOrEqualEnergy) {
		if (batteries == null) {
			return null;
		}
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (int i = 0; i < batteries.length; i++) {
			ItemStack stack = batteries[i];
			if (stack != null
					&& stack.itemID == AetherCraft.itemAetherBattery.itemID
					&& stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey("EMAV")
						&& tag.getInteger("EMAV") >= aboveOrEqualEnergy) {
					result.add(stack);
				}
			}
		}
		return result.toArray(new ItemStack[result.size()]);
	}
}
