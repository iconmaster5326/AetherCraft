package com.iconmaster.aec.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.common.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.common.tileentity.TileEntityAetherManipulator;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		EntityPlayer sender = (EntityPlayer) player;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(
				packet.data));

		if (packet.channel.equals("Aec")) {
			handlePacket(packet, sender);
		}
		if (packet.channel.equals("AecTrans")) {
			handleTransferPacket(packet, sender);
		}
	}

	private void handlePacket(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				packet.data));
		try {
			byte energyBlockType = dis.readByte();
			switch (energyBlockType) {
			case AetherCraft.GUI_ID_MANIPULATOR:
				TileEntityAetherManipulator tem = (TileEntityAetherManipulator) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tem != null) {
					tem.recieveSync(dis.readFloat());
				}
				break;
			case AetherCraft.GUI_ID_CONTAINER:
				TileEntityAetherContainer tec = (TileEntityAetherContainer) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tec != null) {
					tec.recieveSync(dis.readFloat());
				}
				break;
			case AetherCraft.GUI_ID_EXTRACTOR:
				TileEntityAetherExtractor tea = (TileEntityAetherExtractor) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tea != null) {
					tea.recieveSync(dis.readFloat());
				}
				break;
			case AetherCraft.GUI_ID_CONDENSER:
				TileEntityAetherCondenser tec2 = (TileEntityAetherCondenser) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tec2 != null) {
					tec2.recieveSync(dis.readFloat());
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleTransferPacket(Packet250CustomPayload packet,
			EntityPlayer player) {
		try {
			ObjectInputStream dis = new ObjectInputStream(new ByteArrayInputStream(
					packet.data));
			byte transferTypeID = dis.readByte();
			switch (transferTypeID) {
			case AetherCraft.PACKET_TTID_CONFIG:
				AVRegistry.setValueMap(((HashMap) dis.readObject()));
				AetherCraft.setOptionsMap((HashMap<String, String>) dis.readObject());
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
