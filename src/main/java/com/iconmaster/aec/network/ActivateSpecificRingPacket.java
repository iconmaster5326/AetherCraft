package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.item.IAetherRing;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ActivateSpecificRingPacket implements IMessage, IMessageHandler<ActivateSpecificRingPacket, IMessage> {
	private int slot = 0;
	
	public ActivateSpecificRingPacket() {
		
	}
	
	public ActivateSpecificRingPacket(int slot) {
		this.slot = slot;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		slot = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(slot);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(ActivateSpecificRingPacket message, MessageContext ctx) {
		//System.out.println("Activating ring in slot "+message.slot);
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		ItemStack stack = player.inventory.getStackInSlot(message.slot);
		if (stack != null && stack.getItem() instanceof IAetherRing) {
			//System.out.println("Found it");
			((IAetherRing)stack.getItem()).activateRing(stack, player);
		}
		
		return null;
	}
	
}