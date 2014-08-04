package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DeviceSyncPacket implements IMessage, IMessageHandler<DeviceSyncPacket, IMessage> {
	private int x,y,z = 0;
	private float av1,av2 = 0;
	
	public DeviceSyncPacket() {
		
	}
	
	public DeviceSyncPacket(int x,int y,int z,float energy) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.av1 =energy;
	}

	public DeviceSyncPacket(int x,int y,int z,float av1,float av2) {
		this(x,y,z,av1);
		this.av2 = av2;
	}
	
	//more hackish use of syncpacket! yay!
	public DeviceSyncPacket(int x,int y,int z,float av1,boolean opt) {
		this(x,y,z,av1);
		this.av2 = opt ? 1 : 0;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		av1 = buffer.readFloat();
		av2 = buffer.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeFloat(av1);
		buffer.writeFloat(av2);
	}

	@Override
	public IMessage onMessage(DeviceSyncPacket message, MessageContext ctx) {
		//System.out.println("[AEC PACKET] SYNC CLIENT "+message.x+" "+message.y+" "+message.z+" "+message.av1+" "+message.av2);
		AetherCraftTileEntity te = (AetherCraftTileEntity) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
		if (te == null) {
			//System.out.println("[AEC PACKET]CLIENT ERROR: TE was null!");
			return null;
		}
		if (te instanceof TileEntityAetherInfuser) {
			((TileEntityAetherInfuser)te).recieveSync(message.av1,message.av2);
		} else if (te instanceof TileEntityAetologistsChest) {
			((TileEntityAetologistsChest)te).recieveSync(message.av1,message.av2==1);
		} else {
			te.recieveSync(message.av1);
		}
		return null;
	}
	
}