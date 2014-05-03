package com.iconmaster.aec.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FallDamageEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void LivingHurtEventHandler(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.inventory.hasItemStack(new ItemStack(AetherCraft.itemFlyingRing,1,1)) && event.isCancelable()) {
					event.setCanceled(true);
			}
		}
	}
}
