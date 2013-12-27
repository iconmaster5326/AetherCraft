package com.iconmaster.aec.client;

import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.CommonProxy;
import com.iconmaster.aec.common.event.FlyingRingEventReciever;
import com.iconmaster.aec.common.handler.tick.FlyingRingTickHandler;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderInformation() {
	}

	@Override
	public void registerTickHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			TickRegistry.registerScheduledTickHandler(
					new FlyingRingTickHandler(), Side.CLIENT);
		}
	}

	@Override
	public void registerEventHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			MinecraftForge.EVENT_BUS.register(new FlyingRingEventReciever());
		}
	}
}