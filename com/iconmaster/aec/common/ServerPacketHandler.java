package com.iconmaster.aec.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.iconmaster.aec.common.tileentity.TileEntityEnergyContainer;
import com.iconmaster.aec.common.tileentity.TileEntityEnergyManipulator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		EntityPlayer sender = (EntityPlayer) player;

		if (packet.channel.equals("GraEnMaReq")) {
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
			case 0:
				TileEntityEnergyManipulator tem = (TileEntityEnergyManipulator) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tem != null) {
					tem.sync();
				}
				break;
			case 1:
				TileEntityEnergyContainer tec = (TileEntityEnergyContainer) player.worldObj
						.getBlockTileEntity(dis.readInt(), dis.readInt(),
								dis.readInt());
				if (tec != null) {
					tec.sync();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
