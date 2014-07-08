//package com.iconmaster.aec.network;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
//import java.util.HashMap;
//
//import net.minecraft.entity.player.EntityPlayer;
//
//import com.iconmaster.aec.AetherCraft;
//import com.iconmaster.aec.aether.AVRegistry;
//import com.iconmaster.aec.aether.InfuserRegistry;
//import com.iconmaster.aec.config.AVConfigHandler;
//
//public class TransferConfigsPacket extends AetherCraftPacket {
//

//
//	@Override
//	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

//	}
//
//	@Override
//	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

//	}
//
//	@Override
//	public void handleClientSide(EntityPlayer player) {	

//	}
//
//	@Override
//	public void handleServerSide(EntityPlayer player) {
//
//	}
//
//}

package com.iconmaster.aec.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.config.AVConfigHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TransferConfigsPacket implements IMessage, IMessageHandler<TransferConfigsPacket, IMessage> {
	
	private HashMap values;
	private HashMap options;

	public TransferConfigsPacket() {
		
	}
	
	public TransferConfigsPacket setState() {
		this.values = AVConfigHandler.getNetworkConfigMap();
		this.options = AetherCraft.getOptionsMap();
		
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		try {
			ObjectInputStream dis = new ObjectInputStream(new ByteArrayInputStream(buffer.array(),buffer.arrayOffset(),buffer.capacity()));
			
			HashMap[] array = (HashMap[]) dis.readObject();

			values = array[0];
			options = array[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(new HashMap[] {values,options});
			oos.flush();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		buffer.writeBytes(bos.toByteArray());
	}

	@Override
	public IMessage onMessage(TransferConfigsPacket message, MessageContext ctx) {
		AetherCraft.setOptionsMap(message.options);
		InfuserRegistry.addDefaultRecipes();
		AVRegistry.reloadClientValues(message.values);
		return null;
	}
	
}
