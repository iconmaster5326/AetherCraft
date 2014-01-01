package com.iconmaster.aec.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.iconmaster.aec.util.TooltipUtils;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * This is the base class for AetherCraft GUIs. Extend this class to get AV tooltip functionality in your GUI.
 * @author iconmaster
 *
 */
public class AetherCraftGui extends GuiContainer {
	
	/**
	 * Whether or not it displays standard AV tooltips. Set this to false if you're using another tooltip type.
	 */
	public boolean standardTooltip = true;
	/**
	 * Whether or not it displays consume AV tooltips. Set this to true if your device consumes items.
	 */
	public boolean produceTooltip = false;
	/**
	 * Whether or not it displays produce AV tooltips. Set this to true if your device produces items.
	 */
	public boolean consumeTooltip = false;

	public AetherCraftGui(Container par1Container) {
		super(par1Container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

	}

	@Override
	protected void drawItemStackTooltip(ItemStack stack, int par2, int par3) {
		List list = stack.getTooltip(this.mc.thePlayer,
				this.mc.gameSettings.advancedItemTooltips);

		for (int k = 0; k < list.size(); ++k) {
			if (k == 0) {
				list.set(
						k,
						"\u00a7"
								+ Integer.toHexString(stack.getRarity().rarityColor)
								+ (String) list.get(k));
			} else {
				list.set(k, "\u00a77" + (String) list.get(k));
			}
		}

		if (standardTooltip) {
			TooltipUtils.displayStandardTooltip(stack, list);
		}
		if (consumeTooltip) {
			TooltipUtils.displayConsumeTooltip(stack, list);
		}
		if (produceTooltip) {
			TooltipUtils.displayProduceTooltip(stack, list);
		}

		if (list != null && stack != null) {
			FontRenderer font = stack.getItem().getFontRenderer(stack);
			this.drawHoveringText(list, par2, par3,
					(font == null ? fontRenderer : font));
		}
	}

	protected void drawHoveringText(List par1List, int par2, int par3,
			FontRenderer font) {
		if (!par1List.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = par1List.iterator();
	
			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);
	
				if (l > k) {
					k = l;
				}
			}
	
			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;
	
			if (par1List.size() > 1) {
				k1 += 2 + (par1List.size() - 1) * 10;
			}
	
			if (i1 + k > this.width) {
				i1 -= 28 + k;
			}
	
			if (j1 + k1 + 6 > this.height) {
				j1 = this.height - k1 - 6;
			}
	
			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4,
					l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1,
					l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3,
					l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3
					- 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1
					+ 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2,
					i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3,
					j2, j2);
	
			for (int k2 = 0; k2 < par1List.size(); ++k2) {
				String s1 = (String) par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);
	
				if (k2 == 0) {
					j1 += 2;
				}
	
				j1 += 10;
			}
	
			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
	
}
