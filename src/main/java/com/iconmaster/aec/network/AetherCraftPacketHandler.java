package com.iconmaster.aec.network;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class AetherCraftPacketHandler {
	public static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(AetherCraft.MODID);
	
	public static void init() {
		HANDLER.registerMessage(DeviceSyncPacket.class, DeviceSyncPacket.class, 0, Side.CLIENT);
		HANDLER.registerMessage(TransferConfigsPacket.class, TransferConfigsPacket.class, 1, Side.CLIENT);
		HANDLER.registerMessage(ActivateRingsPacket.class, ActivateRingsPacket.class, 2, Side.CLIENT);
		HANDLER.registerMessage(DeactivateRingsPacket.class, DeactivateRingsPacket.class, 3, Side.CLIENT);
		HANDLER.registerMessage(PumpFacePacket.class, PumpFacePacket.class, 4, Side.CLIENT);
		
		HANDLER.registerMessage(RequestSyncPacket.class, RequestSyncPacket.class, 5, Side.SERVER);
		HANDLER.registerMessage(ActivateRingsPacket.class, ActivateRingsPacket.class, 6, Side.SERVER);
		HANDLER.registerMessage(DeactivateRingsPacket.class, DeactivateRingsPacket.class, 7, Side.SERVER);
	}
}
