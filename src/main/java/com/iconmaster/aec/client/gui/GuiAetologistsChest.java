package com.iconmaster.aec.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.inventory.ContainerAetologistsChest;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.ChangeEditModePacket;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAetologistsChest extends AetherCraftGui<TileEntityAetologistsChest> {
	
	private static final ResourceLocation gui_texture = new ResourceLocation(
			"aec", "textures/gui/aetoChestGui.png");
	public GuiButton bindButton;

	public GuiAetologistsChest(InventoryPlayer player,
			TileEntityAetologistsChest tileEntity) {
		super(new ContainerAetologistsChest(player, tileEntity));
		
		this.xSize = 176;
		this.ySize = 166;
		this.te = tileEntity;
		this.requestSync();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);

		this.fontRendererObj.drawStringWithShadow("AV: "+NumberUtils.display(AetherNetwork.getStoredAV(te.getWorldObj(), te.xCoord,  te.yCoord,  te.zCoord)), 6, 6,0x00FF00);

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
	public void initGui() {
		super.initGui();
		//make buttons:		id, x, y, width, height, text
		bindButton = new GuiButton(1,((width - xSize) / 2)+108,((height - ySize) / 2)+4,56,12,"Bind Items");
		updateButtonText();
		buttonList.add(bindButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton==bindButton) {
			te.editMode = !te.editMode;
			AetherCraftPacketHandler.HANDLER.sendToServer(new ChangeEditModePacket(te.xCoord,te.yCoord,te.zCoord,te.editMode));
			updateButtonText();
		}
	}

	public void updateButtonText() {
		if (te.editMode) {
			bindButton.displayString = "Confirm";
		} else {
			bindButton.displayString = "Bind Items";
		}
	}

}