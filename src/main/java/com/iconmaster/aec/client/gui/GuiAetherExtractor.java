package com.iconmaster.aec.client.gui;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.inventory.ContainerAetherExtractor;
import com.iconmaster.aec.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.util.NumberUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAetherExtractor extends AetherCraftGui<TileEntityAetherExtractor> {
	
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetherExtractorGui.png");

	public GuiAetherExtractor(InventoryPlayer player,
			TileEntityAetherExtractor tileEntity) {
		super(new ContainerAetherExtractor(player, tileEntity));
		
		standardTooltip = false;
		consumeTooltip = true;
		produceTooltip = false;
		
		this.xSize = 176;
		this.ySize = 166;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		this.fontRendererObj.drawString("Aether Extractor", 84, 4, 0x404040);

		this.fontRendererObj.drawStringWithShadow("AV: "+NumberUtils.display(this.te.getAether())+"/"+NumberUtils.display(te.getMax()), 54, 58,0x00FF00);

		this.fontRendererObj.drawStringWithShadow("Precision: "+(int) (AetherCraft.options.getFloat("consumeprecision")) + "%", 54,68,0x00FF00);
		
		te.getLimit();
		this.fontRendererObj.drawStringWithShadow("Limit: "+NumberUtils.display(te.getLimit()), 54, 20,0x00FF00);
		
		int a = 68 - 16;
		int progress = (int) Math.min(100,(te.getAether()/te.getMax())*100);
		float b = (100 - progress) / 100.0f;
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

}