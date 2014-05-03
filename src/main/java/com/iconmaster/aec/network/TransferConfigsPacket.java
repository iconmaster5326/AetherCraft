package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.config.AVConfigHandler;

public class TransferConfigsPacket extends AetherCraftPacket {
	
	public TransferConfigsPacket() {
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeByte(AetherCraft.PACKET_TTID_CONFIG);
			oos.writeObject(AVConfigHandler.getNetworkConfigMap());
			oos.writeObject(AetherCraft.getOptionsMap());
			oos.writeInt(2);
			oos.flush();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buffer.writeBytes(bos.toByteArray());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			ObjectInputStream dis = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
			byte transferTypeID = dis.readByte();
			switch (transferTypeID) {
			case AetherCraft.PACKET_TTID_CONFIG:
				HashMap stringValues = (HashMap) dis.readObject();
				AetherCraft.setOptionsMap((HashMap<String, String>) dis.readObject());
				
				AVRegistry.reloadClientValues(stringValues);
				
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}

}
