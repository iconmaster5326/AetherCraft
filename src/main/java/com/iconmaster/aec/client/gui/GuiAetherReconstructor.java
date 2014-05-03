package com.iconmaster.aec.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.inventory.ContainerAetherReconstructor;
import com.iconmaster.aec.tileentity.TileEntityAetherReconstructor;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetherReconstructor extends AetherCraftGui<TileEntityAetherReconstructor> {
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetherReconstructorGui.png");
	private TileEntityAetherReconstructor te;

	public GuiAetherReconstructor(InventoryPlayer player,
			TileEntityAetherReconstructor tileEntity) {
		super(new ContainerAetherReconstructor(player, tileEntity));
		this.xSize = 176;
		this.ySize = 166;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		this.fontRendererObj.drawString("Aether Reconstructor", 44, 5, 0x404040);
		this.fontRendererObj.drawString("Inventory", 7, 72, 0x404040);

		this.fontRendererObj.drawStringWithShadow("AV: "+NumberUtils.display(this.te.getAether())+"/"+NumberUtils.display(te.max), 30, 58,0x00FF00);
		
		te.calcLimit();
		this.fontRendererObj.drawStringWithShadow("Limit: "+NumberUtils.display(te.limit), 110, 58,0x00FF00);
		
		float cost = 0;
		if (te.getStackInSlot(0) != null && te.getStackInSlot(0).isItemDamaged()) {
			cost = AVRegistry.getAbsoluteAV(te.getStackInSlot(0))/te.getStackInSlot(0).getMaxDamage();
		}
		
		this.fontRendererObj.drawStringWithShadow("Cost Per Point: "+NumberUtils.display(cost), 30, 16,0x00FF00);

		int barHeight = 68 - 16;
		float barPercent = (100 - this.te.getProgress()) / 100.0f;
		this.drawGradientRect(10, (int) (16.0f + (barHeight * barPercent)), 26, 68,
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