package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public  class ActivateRingsClientPacket extends ActivateRingsPacket {

	@Override
	public IMessage onMessage(ActivateRingsPacket message, MessageContext ctx) {
		InventoryUtils.activateRings(Minecraft.getMinecraft().thePlayer);
		return null;
	}
	
}
