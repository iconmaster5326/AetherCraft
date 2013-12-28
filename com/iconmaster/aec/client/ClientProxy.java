package com.iconmaster.aec.client;

import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.CommonProxy;
import com.iconmaster.aec.common.event.FallDamageEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderInformation() {
	}

	@Override
	public void registerHandlers() {

	}

	@Override
	public void registerEventHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			MinecraftForge.EVENT_BUS.register(new FallDamageEvent());
		}
	}
}