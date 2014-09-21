package com.iconmaster.aec.network;

import com.iconmaster.aec.tileentity.AetherCraftTileEntity;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ChangeEditModePacket implements IMessage, IMessageHandler<ChangeEditModePacket, IMessage> {
	private int x,y,z = 0;
	private boolean mode = false;
	
	public ChangeEditModePacket() {
		
	}
	
	public ChangeEditModePacket(int x,int y,int z,boolean mode) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mode  = mode;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		mode = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeBoolean(mode);
	}

	@Override
	public IMessage onMessage(ChangeEditModePacket message, MessageContext ctx) {
		AetherCraftTileEntity te = (AetherCraftTileEntity) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
		if (te == null) {
			//System.out.println("[AEC PACKET]SERVER ERROR: TE was null!");
			return null;
		}
		if (te instanceof TileEntityAetologistsChest) {
			((TileEntityAetologistsChest)te).editMode = message.mode;
			te.sync();
		}
		return null;
	}
	
}