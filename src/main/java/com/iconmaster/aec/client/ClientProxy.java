package com.iconmaster.aec.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;
import com.iconmaster.aec.client.render.RenderAetherConduit;
import com.iconmaster.aec.event.AetherArmorDamageEvent;
import com.iconmaster.aec.event.AetherSwordEvent;
import com.iconmaster.aec.event.DisableRingInContainerEvent;
import com.iconmaster.aec.event.FallDamageEvent;
import com.iconmaster.aec.event.TooltipEvent;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		MinecraftForge.EVENT_BUS.register(new AetherArmorDamageEvent());
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void activateRings(Object ctx) {
		InventoryUtils.activateRings(Minecraft.getMinecraft().thePlayer);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void deactivateRings(Object ctx) {
		InventoryUtils.deactivateRings(Minecraft.getMinecraft().thePlayer);
	}
}