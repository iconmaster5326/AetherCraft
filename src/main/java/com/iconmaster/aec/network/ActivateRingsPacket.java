package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class ActivateRingsPacket implements IMessage, IMessageHandler<ActivateRingsPacket, IMessage> {
	public ActivateRingsPacket() {
		
	}

	@Override
	public void fromBytes(ByteBuf buffer) {

	}

	@Override
	public void toBytes(ByteBuf buffer) {

	}

	@Override
	public IMessage onMessage(ActivateRingsPacket message, MessageContext ctx) {
		if (ctx.side==Side.CLIENT) {
			InventoryUtils.activateRings(Minecraft.getMinecraft().thePlayer);
		} else {
			InventoryUtils.activateRings(ctx.getServerHandler().playerEntity);
		}
		return null;
	}
	
}
