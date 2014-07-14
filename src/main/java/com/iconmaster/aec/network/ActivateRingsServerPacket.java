package com.iconmaster.aec.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.item.IAetherRing;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ActivateRingsServerPacket extends ActivateRingsPacket {

	@Override
	public IMessage onMessage(ActivateRingsPacket message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		for (int i=0;i<player.inventory.getSizeInventory();i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IAetherRing) {
				if (InventoryUtils.ringsSupressed.get(stack.getItem())!=null) {
					//System.out.println("Sending packet for slot "+i);
					AetherCraftPacketHandler.HANDLER.sendTo(new ActivateSpecificRingPacket(i), player);
				}
			}
		}
		
		InventoryUtils.activateRings(player);
		return null;
	}
	
}
