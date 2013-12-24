package com.iconmaster.aec.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.iconmaster.aec.client.gui.GuiAetherManipulator;
import com.iconmaster.aec.common.AetherCraft;

import codechicken.nei.forge.IContainerTooltipHandler;

public class TooltipHandler implements IContainerTooltipHandler {
	@Override
	public List<String> handleTooltipFirst(GuiContainer gui, int mousex,
			int mousey, List<String> currenttip) {
		return currenttip;
	}

	@Override
	public List<String> handleItemTooltip(GuiContainer gui, ItemStack stack,
			List<String> currenttip) {
		if (gui instanceof GuiAetherManipulator) {
			boolean showAV = true;
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("showevalways"))) {
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
						&& !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					showAV = false;
				}
			}
			if (showAV
					&& stack != null
					&& stack.itemID != AetherCraft.itemAetherBattery.itemID) {
				int ev = AetherCraft.getAetherValueByItemStack(stack);
				int ev1 = (int) (((float) ev)
						* ((float) Integer.parseInt(AetherCraft
								.getOptions("consumeprecission"))) / 100.0f);

				currenttip.add("\u00a72" + "TRANSMUTE AV: " + Integer.toString(ev));
				currenttip.add("\u00a79" + "CONSUME    AV: " + Integer.toString(ev1));
			}
		} else {
			boolean showAV = true;
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("showevalways"))) {
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
						&& !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					showAV = false;
				}
			}
			if (showAV
					&& stack != null
					&& stack.itemID != AetherCraft.itemAetherBattery.itemID) {
				int ev = AetherCraft.getAetherValueByItemStack(stack);
				currenttip.add("\u00a72" + "AV: " + Integer.toString(ev));
			}
		}
		return currenttip;
	}
}
