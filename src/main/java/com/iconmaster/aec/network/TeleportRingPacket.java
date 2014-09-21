package com.iconmaster.aec.network;

import com.iconmaster.aec.item.ItemTeleportRing;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class TeleportRingPacket implements IMessage, IMessageHandler<TeleportRingPacket, IMessage> {
	private boolean shifting = false;
	
	public TeleportRingPacket() {
		
	}
	
	public TeleportRingPacket(boolean mode) {
		this.shifting  = mode;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		shifting = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeBoolean(shifting);
	}

	@Override
	public IMessage onMessage(TeleportRingPacket message, MessageContext ctx) {
		if (ctx.getServerHandler().playerEntity.getHeldItem()!= null && ctx.getServerHandler().playerEntity.getHeldItem().getItem() instanceof ItemTeleportRing) {
			ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItem();
			if (message.shifting) {
				ItemTeleportRing.registerWaypoint(stack, ctx.getServerHandler().playerEntity);
			} else {
				ItemTeleportRing.teleportToWaypoint(stack, ctx.getServerHandler().playerEntity);
			}
		} else {
			System.out.println("[AEC] There was an error in teleporting!");
		}

		return null;
	}
	
}