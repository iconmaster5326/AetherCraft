package com.iconmaster.aec.event;

import com.iconmaster.aec.client.gui.AetherCraftGui;
import com.iconmaster.aec.util.TooltipUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class TooltipEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventHandler(ItemTooltipEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack stack = event.itemStack;
		List currenttip = event.toolTip;
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		
		//System.out.println(gui);
		
		if (gui instanceof AetherCraftGui) {
			AetherCraftGui agui = (AetherCraftGui)gui;
			if (agui.standardTooltip) {
				TooltipUtils.displayStandardTooltip(stack, currenttip);
			}
			if (agui.consumeTooltip) {
				TooltipUtils.displayConsumeTooltip(stack, currenttip);
			}
			if (agui.produceTooltip) {
				TooltipUtils.displayProduceTooltip(stack, currenttip);
			}
		} else {
			TooltipUtils.displayStandardTooltip(stack, currenttip);
		}
	}
}
