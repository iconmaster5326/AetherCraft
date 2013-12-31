package com.iconmaster.aec.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import codechicken.nei.forge.IContainerTooltipHandler;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.client.gui.AetherCraftGui;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.NumberUtils;
import com.iconmaster.aec.util.TooltipUtils;

public class TooltipHandler implements IContainerTooltipHandler {
	@Override
	public List<String> handleTooltipFirst(GuiContainer gui, int mousex,
			int mousey, List<String> currenttip) {
		return currenttip;
	}

	@Override
	public List<String> handleItemTooltip(GuiContainer gui, ItemStack stack,
			List<String> currenttip) {
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
		return currenttip;
	}
}
