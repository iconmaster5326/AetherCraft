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

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.inventory.ContainerAetherManipulator;
import com.iconmaster.aec.tileentity.TileEntityAetherManipulator;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetherManipulator extends AetherCraftGui {
	
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetherManipulatorGui.png");
	private TileEntityAetherManipulator te;

	public GuiAetherManipulator(InventoryPlayer player,
			TileEntityAetherManipulator tileEntity) {
		super(new ContainerAetherManipulator(player, tileEntity));
		
		standardTooltip = false;
		consumeTooltip = true;
		produceTooltip = true;
		
		this.xSize = 176;
		this.ySize = 256;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		this.fontRenderer.drawString("Aether Manipulator", 70, 3, 0x404040);

//		this.fontRenderer.drawStringWithShadow("Internal Aether:", 9, 26,
//				0xFF0000);
//		this.fontRenderer.drawStringWithShadow(
//				NumberUtils.display(this.te.getAether()), 95, 26, 0x00FF00);
		
		this.fontRenderer.drawStringWithShadow("AV: "+NumberUtils.display(te.getAether())+"/"+NumberUtils.display(te.max), 9, 26,0x00FF00);

//		this.fontRenderer.drawStringWithShadow("Consume Precision:", 9, 38,
//				0xFF0000);
//		this.fontRenderer.drawStringWithShadow(
//				(int) (Double.parseDouble(AetherCraft
//						.getOptions("consumeprecision"))) + "%", 112, 38,
//				0x00FF00);
		
		te.calcLimit();
		this.fontRenderer.drawStringWithShadow("Limit: "+NumberUtils.display(te.limit), 100, 38,0x00FF00);
		
		te.calcMax();
		this.fontRenderer.drawStringWithShadow("Precision: "+((int)(Double.parseDouble(AetherCraft.getOptions("consumeprecision"))))+"%", 9, 38,0x00FF00);

		this.drawGradientRect(68, 11, 68 + this.te.getProgress(), 17,
				0x00404040, 0xFF2CCDB1);
		this.drawGradientRect(68, 17, 68 + this.te.getProgress(), 22,
				0xFF16FF00,0x990EA600);
		
		float av = 0;
		if (this.te.getStackInSlot(0)!=null) {
			if (this.te.getStackInSlot(0).getItem() instanceof IProduceBehavior) {
				av = ((IProduceBehavior)this.te.getStackInSlot(0).getItem()).getProduceAV(this.te.getStackInSlot(0));
			} else {
				av = AVRegistry.getAV(this.te.getStackInSlot(0));
			}
		}

		this.drawCenteredString(
				this.fontRenderer,
				NumberUtils.display(this.te.getPossibleAether())
						+ " / "
						+ NumberUtils.display(av), 118, 13, 0x55FF55);

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
			this.drawGradientRect(x+8, y+6, x+8+16, y+6+16,0x88FF0000,0x88FF0000);
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