package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.event.DisableRingInContainerEvent;
import com.iconmaster.aec.item.IAetherRing;
import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;
import com.iconmaster.aec.util.InventoryUtils;

public class DeactivateRingsPacket extends AetherCraftPacket {
	public DeactivateRingsPacket() {
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		System.out.println("[AEC PACKET] DEACTIVATE PACKET SIDE");
		
		InventoryUtils.deactivateRings(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		System.out.println("[AEC PACKET] DEACTIVATE PACKET SIDE");
		
		InventoryUtils.deactivateRings(player);
	}
}
