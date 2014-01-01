package com.iconmaster.aec.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.iconmaster.aec.common.tileentity.AetherCraftTileEntity;
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
				default:
				AetherCraftTileEntity te = (AetherCraftTileEntity) player.worldObj.getBlockTileEntity(dis.readInt(), dis.readInt(),dis.readInt());
				if (te != null) {
					te.sync();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
