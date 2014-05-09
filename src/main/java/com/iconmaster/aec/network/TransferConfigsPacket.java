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

	private HashMap values;
	private HashMap<String, String> options;

	public TransferConfigsPacket() {
		//System.out.println("TRANS Saving values!");
		this.values = AVConfigHandler.getNetworkConfigMap();
		this.options = AetherCraft.getOptionsMap();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("TRANS Encoding maps!");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(values);
			oos.writeObject(options);
			oos.flush();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buffer.writeBytes(bos.toByteArray());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("TRANS Decoding maps!");
		try {
			ObjectInputStream dis = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));

			values = (HashMap) dis.readObject();
			options = (HashMap<String, String>) dis.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//System.out.println("TRANS CLIENT!");
		
		AetherCraft.setOptionsMap(options);
		AVRegistry.reloadClientValues(values);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//System.out.println("TRANS SERVER!");
	}

}
