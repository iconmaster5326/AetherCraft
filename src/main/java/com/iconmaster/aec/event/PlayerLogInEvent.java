package com.iconmaster.aec.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.network.TransferConfigsPacket;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class PlayerLogInEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventHandler(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		
		AetherCraft.packetHandler.sendTo(new TransferConfigsPacket(), (EntityPlayerMP) player);
	}
}
