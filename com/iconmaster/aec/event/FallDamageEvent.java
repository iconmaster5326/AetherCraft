package com.iconmaster.aec.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import com.iconmaster.aec.AetherCraft;

public class FallDamageEvent {
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void LivingHurtEventHandler(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.inventory.hasItemStack(new ItemStack(AetherCraft.itemFlyingRing.itemID,1,1)) && event.isCancelable()) {
					event.setCanceled(true);
			}
		}
	}
}
