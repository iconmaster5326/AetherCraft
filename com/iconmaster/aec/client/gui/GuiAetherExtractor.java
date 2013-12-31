package com.iconmaster.aec.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.gui.ContainerAetherExtractor;
import com.iconmaster.aec.common.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetherExtractor extends GuiContainer {
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetherExtractorGui.png");
	private TileEntityAetherExtractor te;

	public GuiAetherExtractor(InventoryPlayer player,
			TileEntityAetherExtractor tileEntity) {
		super(new ContainerAetherExtractor(player, tileEntity));
		this.xSize = 176;
		this.ySize = 166;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		this.fontRenderer.drawString("Aether Extractor", 84, 4, 0x404040);

		this.fontRenderer.drawStringWithShadow("Aether: "+NumberUtils.display(this.te.getAether()), 54, 58,0x00FF00);

		this.fontRenderer.drawStringWithShadow("Precision: "+(int) (Double.parseDouble(AetherCraft.getOptions("consumeprecision"))) + "%", 54,68,0x00FF00);
		
		int a = 68 - 16;
		float b = (100 - this.te.getProgress()) / 100.0f;
		this.drawGradientRect(148, (int) (16.0f + (a * b)), 164, 68,
				0xFF16FF00,0x990EA600);

		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float metaTicks, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(this.gui_texture);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
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

		boolean showAV = true;
		if (!Boolean.parseBoolean(AetherCraft.getOptions("showavalways"))) {
			if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
					&& !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				showAV = false;
			}
		}
		if (showAV && stack != null) {
			float ev,ev1;
			System.out.println("Showing tooltip for "+stack.getClass());
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

			list.add("\u00a72" + "PRODUCE AV: " + NumberUtils.display(ev));
			list.add("\u00a79" + "CONSUME AV: " + NumberUtils.display(ev1));
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

	private void requestSync() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);

		try {
			outputStream.writeByte(this.te.energyBlockType);
			outputStream.writeInt(this.te.xCoord);
			outputStream.writeInt(this.te.yCoord);
			outputStream.writeInt(this.te.zCoord);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "AecReq";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToServer(packet);
	}
}