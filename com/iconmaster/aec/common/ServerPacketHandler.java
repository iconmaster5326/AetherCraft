package com.iconmaster.aec.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.iconmaster.aec.common.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.common.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.common.tileentity.TileEntityAetherInfuser;
import com.iconmaster.aec.common.tileentity.TileEntityAetherManipulator;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		EntityPlayer sender = (EntityPlayer) player;

		if (packet.channel.equals("AecReq")) {
			handlePacketRequest(packet, sender);
		}
	}

	private void handlePacketRequest(Packet250CustomPayload packet,
			EntityPlayer player) {
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
					tem.sync();
				}
				break;
			case AetherCraft.GUI_ID_CONTAINER:
				TileEntityAetherContainer tec = (TileEntityAetherContainer) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tec != null) {
					tec.sync();
				}
			case AetherCraft.GUI_ID_EXTRACTOR:
				TileEntityAetherExtractor tea = (TileEntityAetherExtractor) player.worldObj
				.getBlockTileEntity(dis.readInt(), dis.readInt(),dis.readInt());
					if (tea != null) {
						tea.sync();
					}
			case AetherCraft.GUI_ID_CONDENSER:
				TileEntityAetherCondenser tec2 = (TileEntityAetherCondenser) player.worldObj
				.getBlockTileEntity(dis.readInt(), dis.readInt(),dis.readInt());
					if (tec2 != null) {
						tec2.sync();
					}
			case AetherCraft.GUI_ID_INFUSER:
				TileEntityAetherInfuser tei = (TileEntityAetherInfuser) player.worldObj
				.getBlockTileEntity(dis.readInt(), dis.readInt(),dis.readInt());
					if (tei != null) {
						tei.sync();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
