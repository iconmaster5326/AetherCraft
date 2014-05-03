package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;

public class RequestSyncPacket extends AetherCraftPacket {
	private int x,y,z = 0;
	
	public RequestSyncPacket() {
		
	}
	
	public RequestSyncPacket(int x,int y,int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		System.out.println("[AEC PACKET] REQ SERVER "+x+" "+y+" "+z);
		AetherCraftTileEntity te = (AetherCraftTileEntity) player.worldObj.getTileEntity(x, y, z);
		if (te == null) {
			System.out.println("[AEC PACKET]SERVER ERROR: TE was null!");
			return;
		}
		AetherCraft.packetHandler.sendToAll(new DeviceSyncPacket(x,y,z,te.getAether()));
	}

}
