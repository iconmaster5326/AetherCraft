package com.iconmaster.aec.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.common.gui.ContainerAetherReconstructor;
import com.iconmaster.aec.common.tileentity.TileEntityAetherReconstructor;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetherReconstructor extends AetherCraftGui {
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
		this.fontRenderer.drawString("Aether Reconstructor", 44, 5, 0x404040);
		this.fontRenderer.drawString("Inventory", 7, 72, 0x404040);

		this.fontRenderer.drawStringWithShadow("Aether: "+NumberUtils.display(this.te.getAether()), 30, 58,0x00FF00);
		
		float cost = 0;
		if (te.getStackInSlot(0) != null && te.getStackInSlot(0).isItemDamaged()) {
			cost = AVRegistry.getAbsoluteAV(te.getStackInSlot(0))/te.getStackInSlot(0).getMaxDamage();
		}
		
		this.fontRenderer.drawStringWithShadow("Cost Per Point: "+NumberUtils.display(cost), 30, 16,0x00FF00);

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