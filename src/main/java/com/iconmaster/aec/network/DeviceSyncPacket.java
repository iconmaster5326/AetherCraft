package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;

public class DeviceSyncPacket extends AetherCraftPacket {
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
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeFloat(av1);
		buffer.writeFloat(av2);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		av1 = buffer.readFloat();
		av2 = buffer.readFloat();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		System.out.println("[AEC PACKET] SYNC CLIENT "+x+" "+y+" "+z+" "+av1+" "+av2);
		AetherCraftTileEntity te = (AetherCraftTileEntity) player.worldObj.getTileEntity(x, y, z);
		if (te == null) {
			System.out.println("[AEC PACKET]CLIENT ERROR: TE was null!");
			return;
		}
		if (te instanceof TileEntityAetherInfuser) {
			((TileEntityAetherInfuser)te).recieveSync(av1,av2);
		} else {
			te.recieveSync(av1);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}

}
