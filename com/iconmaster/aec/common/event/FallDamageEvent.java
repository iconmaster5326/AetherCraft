package com.iconmaster.aec.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import com.iconmaster.aec.common.AetherCraft;

public class FallDamageEvent {
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void LivingHurtEventHandler(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.inventory.hasItem(AetherCraft.itemFlyingRing.itemID) && event.isCancelable()) {
					event.setCanceled(true);
			}
		}
	}
}
