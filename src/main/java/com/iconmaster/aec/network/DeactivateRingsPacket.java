package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DeactivateRingsPacket implements IMessage, IMessageHandler<DeactivateRingsPacket, IMessage> {
	public DeactivateRingsPacket() {
		
	}

	@Override
	public void fromBytes(ByteBuf buffer) {

	}

	@Override
	public void toBytes(ByteBuf buffer) {

	}

	@Override
	public IMessage onMessage(DeactivateRingsPacket message, MessageContext ctx) {
		AetherCraft.proxy.deactivateRings(ctx);
		return null;
	}
	
}
