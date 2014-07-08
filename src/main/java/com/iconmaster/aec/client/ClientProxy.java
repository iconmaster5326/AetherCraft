package com.iconmaster.aec.client;

import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;
import com.iconmaster.aec.client.render.RenderAetherConduit;
import com.iconmaster.aec.event.AetherSwordEvent;
import com.iconmaster.aec.event.DisableRingInContainerEvent;
import com.iconmaster.aec.event.FallDamageEvent;
import com.iconmaster.aec.event.TooltipEvent;
import com.iconmaster.aec.network.ActivateRingsClientPacket;
import com.iconmaster.aec.network.ActivateRingsPacket;
import com.iconmaster.aec.network.DeactivateRingsClientPacket;
import com.iconmaster.aec.network.DeactivateRingsPacket;

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
		MinecraftForge.EVENT_BUS.register(new DisableRingInContainerEvent());
		MinecraftForge.EVENT_BUS.register(new TooltipEvent());
	}
	
	@Override
	public ActivateRingsPacket getActivatePacket() {
		return new ActivateRingsClientPacket();
	}
	
	@Override
	public DeactivateRingsPacket getDeactivatePacket() {
		return new DeactivateRingsClientPacket();
	}
}