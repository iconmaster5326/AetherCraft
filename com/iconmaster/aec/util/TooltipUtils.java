package com.iconmaster.aec.util;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.common.AetherCraft;

/**
 * This class is a set of utilities having to do with tooltips. Call from GUIs to get AV tooltips.
 * @author iconmaster
 *
 */
public class TooltipUtils {
	
	/**
	 * Returns whether or not a tooltip should be shown at this time.
	 * @return
	 */
	public static boolean showTip() {
		boolean showAV = true;
		if (!Boolean.parseBoolean(AetherCraft.getOptions("showavalways"))) {
			if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				showAV = false;
			}
		}
		return showAV;
	}
	
	public static void displayStandardTooltip(ItemStack stack, List tip) {
		if (stack == null || !showTip()) {return;}
		float ev;
		if (stack.getItem() instanceof IConsumeBehavior) {
			ev = ((IConsumeBehavior)stack.getItem()).getConsumeAV(stack);
		} else {
			ev = AVRegistry.getAV(stack);
		}
		tip.add("\u00a72" + "AV: " + NumberUtils.display(ev));
	}
	
	public static void displayConsumeTooltip(ItemStack stack, List tip) {
		if (stack == null || !showTip()) {return;}
		float ev1;
		if (stack.getItem() instanceof IConsumeBehavior) {
			ev1 = ((IConsumeBehavior)stack.getItem()).getConsumeAV(stack);
		} else {
			ev1 = AVRegistry.getAV(stack);
		}
		ev1 *= ((float) Float.parseFloat(AetherCraft.getOptions("consumeprecision"))) / 100.0f;

		tip.add("\u00a79" + "CONSUME AV: " + NumberUtils.display(ev1));
	}
	
	public static void displayProduceTooltip(ItemStack stack, List tip) {
		if (stack == null || !showTip()) {return;}
		float ev;
		if (stack.getItem() instanceof IProduceBehavior) {
			ev = ((IProduceBehavior)stack.getItem()).getProduceAV(stack);
		} else {
			ev = AVRegistry.getAV(stack);
		}
		tip.add("\u00a72" + "PRODUCE AV: " + NumberUtils.display(ev));
	}
}
