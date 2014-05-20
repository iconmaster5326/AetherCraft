package com.iconmaster.aec.util;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S09PacketHeldItemChange;

import com.iconmaster.aec.aether.IAetherStorageItem;
import com.iconmaster.aec.item.IAetherRing;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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
	
	public static  void activateRings(EntityPlayer player) {
		for (int i=0;i<player.inventory.getSizeInventory();i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IAetherRing) {
				//System.out.println("found ring "+InventoryUtils.ringsSupressed);
					if (InventoryUtils.ringsSupressed.get(stack.getItem())!=null) {
						//System.out.println("activating ring");
						//InventoryUtils.ringsSupressed.remove(stack.getItem());
						((IAetherRing)stack.getItem()).activateRing(stack, player);
					}
			}
		}
	}
	
	public static  void deactivateRings(EntityPlayer player) {
		ringsSupressed = new HashMap<Item, Object>();
		for (int i=0;i<player.inventory.getSizeInventory();i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IAetherRing) {
				//System.out.println("found ring");
					if (((IAetherRing)stack.getItem()).isActive(stack)) {
						//System.out.println("deactivating ring");
						((IAetherRing)stack.getItem()).deactivateRing(stack, player);
						InventoryUtils.ringsSupressed.put(stack.getItem(), new Object());
						//System.out.println(InventoryUtils.ringsSupressed);
					}
			}
		}
	}

	private static HashMap<Item,Object> ringsSupressed = new HashMap<Item, Object>();
}
