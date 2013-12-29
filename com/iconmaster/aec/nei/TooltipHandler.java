package com.iconmaster.aec.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import codechicken.nei.forge.IContainerTooltipHandler;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.client.gui.GuiAetherManipulator;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.NumberUtils;

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
					.getOptions("showavalways"))) {
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
						&& !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					showAV = false;
				}
			}
			if (showAV
					&& stack != null) {
				float ev,ev1;
				if (stack.getItem() instanceof IConsumeBehavior) {
					ev1 = ((IConsumeBehavior)stack.getItem()).getConsumeAV(stack);
				} else {
					ev1 = AVRegistry.getAV(stack);
				}
				if (stack.getItem() instanceof IProduceBehavior) {
					ev = ((IProduceBehavior)stack.getItem()).getProduceAV(stack);
				} else {
					ev = AVRegistry.getAV(stack);
				}
				ev1 *= ((float) Float.parseFloat(AetherCraft.getOptions("consumeprecision"))) / 100.0f;

				currenttip.add("\u00a72" + "PRODUCE AV: " + NumberUtils.display(ev));
				currenttip.add("\u00a79" + "CONSUME AV: " + NumberUtils.display(ev1));
			}
		} else {
			boolean showAV = true;
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("showavalways"))) {
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
						&& !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					showAV = false;
				}
			}
			if (showAV
					&& stack != null) {
				float ev;
				if (stack.getItem() instanceof IConsumeBehavior) {
					ev = ((IConsumeBehavior)stack.getItem()).getConsumeAV(stack);
				} else {
					ev = AVRegistry.getAV(stack);
				}
				currenttip.add("\u00a72" + "AV: " + NumberUtils.display(ev));
			}
		}
		return currenttip;
	}
}
