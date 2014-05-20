package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.iconmaster.aec.util.InventoryUtils;

public class ActivateRingsPacket extends AetherCraftPacket {
	public ActivateRingsPacket() {
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//System.out.println("[AEC PACKET] ACTIVATE PACKET SIDE");
		
		InventoryUtils.activateRings(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//System.out.println("[AEC PACKET] ACTIVATE PACKET SIDE");
		
		InventoryUtils.activateRings(player);
	}
}
