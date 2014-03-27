package com.iconmaster.aec.client;

import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.client.render.RenderAetherConduit;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.CommonProxy;
import com.iconmaster.aec.common.event.AetherSwordEvent;
import com.iconmaster.aec.common.event.FallDamageEvent;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	public static int renderPass;
    public static int conduitRenderType;
    
    public static void setCustomRenderers()
    {
            conduitRenderType = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new RenderAetherConduit());
    }
	
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
		MinecraftForge.EVENT_BUS.register(new AetherSwordEvent());
	}
}