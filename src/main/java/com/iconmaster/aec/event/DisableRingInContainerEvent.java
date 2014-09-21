package com.iconmaster.aec.event;

import com.iconmaster.aec.network.ActivateRingsPacket;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.DeactivateRingsPacket;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class DisableRingInContainerEvent {
	public static boolean inContainer = false;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventHandler(PlayerOpenContainerEvent event) {
		EntityPlayer player = event.entityPlayer;
		
		//System.out.println("[AEC] In container"+player.openContainer+", is in chest: "+inContainer);
		
		if (player.openContainer instanceof ContainerPlayer) {
			if (inContainer) {
				//activate rings
				if (player instanceof EntityPlayerMP) {
					//System.out.println("Sending to player");
					AetherCraftPacketHandler.HANDLER.sendTo(new ActivateRingsPacket(),(EntityPlayerMP) player);
				} else {
					//System.out.println("Sending to server");
					AetherCraftPacketHandler.HANDLER.sendToServer(new ActivateRingsPacket());
				}
				//System.out.println("[AEC PACKET] ACTIVATE EVENT SIDE");
				InventoryUtils.activateRings(player);
				inContainer = false;
			} else {
				return;
			}
		} else {
			if (inContainer) {
				return;
			} else {
				//deactivate rings
				if (player instanceof EntityPlayerMP) {
					//System.out.println("Sending to player");
					AetherCraftPacketHandler.HANDLER.sendTo(new DeactivateRingsPacket(),(EntityPlayerMP) player);
				} else {
					//System.out.println("Sending to server");
					AetherCraftPacketHandler.HANDLER.sendToServer(new DeactivateRingsPacket());
				}
				//System.out.println("[AEC PACKET] DEACTIVATE EVENT SIDE");
				InventoryUtils.deactivateRings(player);
				inContainer = true;
			}
		}
	}
}
