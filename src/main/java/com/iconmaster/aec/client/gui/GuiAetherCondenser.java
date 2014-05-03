package com.iconmaster.aec.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.inventory.ContainerAetherCondenser;
import com.iconmaster.aec.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetherCondenser extends AetherCraftGui<TileEntityAetherCondenser> {

	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetherCondenserGui.png");

	public GuiAetherCondenser(InventoryPlayer player,TileEntityAetherCondenser tileEntity) {
		super(new ContainerAetherCondenser(player, tileEntity));
		
		standardTooltip = false;
		consumeTooltip = false;
		produceTooltip = true;
		
		this.xSize = 176;
		this.ySize = 166;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		this.fontRendererObj.drawString("Aether Condenser", 9, 4, 0x404040);

		this.fontRendererObj.drawStringWithShadow("AV: "+NumberUtils.display(te.getAether())+"/"+NumberUtils.display(te.max), 30, 58,0x00FF00);
		
		te.calcLimit();
		this.fontRendererObj.drawStringWithShadow("Limit: "+NumberUtils.display(te.limit), 30, 70,0x00FF00);
		
		float av = 0;
		if (this.te.getStackInSlot(0)!=null) {
			if (this.te.getStackInSlot(0).getItem() instanceof IProduceBehavior) {
				av = ((IProduceBehavior)this.te.getStackInSlot(0).getItem()).getProduceAV(this.te.getStackInSlot(0));
			} else {
				av = AVRegistry.getAV(this.te.getStackInSlot(0));
			}
		}
		
		if (AVRegistry.getAV(this.te.getStackInSlot(0)) != 0) {
			int percent = Math.min((int) ((te.getPossibleAether() / av)*100),100);
			this.drawRect(30, 16, 30 + percent, 24,0xFF32FF00);
		}
		
		this.drawCenteredString(
				this.fontRendererObj,
				NumberUtils.display(this.te.getPossibleAether())
						+ " / "
						+ NumberUtils.display(av), 30+50, 16, 0x55FF55);

		
		int barHeight = 68 - 16;
		float barPercent = (100 - this.te.getProgress()) / 100.0f;
		//this.drawGradientRect(10, (int) (16+barHeight*barPercent), 26, 68,
		//		0xFF16FF00,0x990EA600);
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
		
		ItemStack topStack = te.getStackInSlot(0);
		float av = AVRegistry.getAV(topStack);
		te.calcLimit();
		if (topStack != null && (av<=0 || av>te.limit)) {
			this.drawGradientRect(x+38, y+33, x+38+16, y+33+16,0x88FF0000,0x88FF0000);
		}
	}


}