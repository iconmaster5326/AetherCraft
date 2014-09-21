package com.iconmaster.aec.event;

import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.TransferConfigsPacket;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerLogInEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventHandler(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;

		AetherCraftPacketHandler.HANDLER.sendTo(new TransferConfigsPacket().setState(),(EntityPlayerMP)event.player);
	}
}
