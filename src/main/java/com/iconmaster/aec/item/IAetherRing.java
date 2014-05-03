package com.iconmaster.aec.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAetherRing {
	public void activateRing(ItemStack stack,EntityPlayer player);
	public void deactivateRing(ItemStack stack,EntityPlayer player);
	
	public boolean isActive(ItemStack stack);
}
