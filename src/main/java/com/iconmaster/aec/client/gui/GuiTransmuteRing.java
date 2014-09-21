package com.iconmaster.aec.client.gui;

import com.iconmaster.aec.inventory.ContainerTransmuteRing;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTransmuteRing extends GuiContainer {
	
	public GuiTransmuteRing(Container par1Container) {
		super(par1Container);
	}
	
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/transmuteRingGui.png");

	public GuiTransmuteRing(InventoryPlayer player, ItemStack ring) {
		super(new ContainerTransmuteRing(player, ring));
		
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);

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
