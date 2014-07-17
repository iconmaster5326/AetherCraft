package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetherPump;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PumpFacePacket implements IMessage, IMessageHandler<PumpFacePacket, IMessage> {
	private int x,y,z = 0;
	private int face = 0;
	
	public PumpFacePacket() {
		
	}
	
	public PumpFacePacket(int x,int y,int z,int  face) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		face = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(face);
	}

	@Override
	public IMessage onMessage(PumpFacePacket message, MessageContext ctx) {
		System.out.println("PUMP PACKET "+message.face);
		AetherCraftTileEntity te = (AetherCraftTileEntity) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
		if (te == null) {
			System.out.println("[AEC PACKET]CLIENT ERROR: TE was null!");
			return null;
		}
		if (te instanceof TileEntityAetherPump) {
			((TileEntityAetherPump)te).face = message.face;
		}
		return null;
	}
	
}