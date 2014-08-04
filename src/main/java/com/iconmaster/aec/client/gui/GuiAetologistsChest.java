package com.iconmaster.aec.client.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.inventory.ContainerAetologistsChest;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.ChangeEditModePacket;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;
import com.iconmaster.aec.util.NumberUtils;
import com.iconmaster.aec.util.TooltipUtils;

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

		this.fontRendererObj.drawStringWithShadow("AV: "+NumberUtils.display(AetherNetwork.getAbsoluteStoredAV(te.getWorldObj(), te.xCoord,  te.yCoord,  te.zCoord)), 6, 6,0x00FF00);

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
	
	@Override
	protected void renderToolTip(ItemStack stack, int par2, int par3) {
		List list = stack.getTooltip(this.mc.thePlayer,
				this.mc.gameSettings.advancedItemTooltips);

		for (int k = 0; k < list.size(); ++k) {
			if (k == 0) {
				list.set(
						k,
						"\u00a7"
								+ Integer.toHexString(stack.getRarity().rarityColor.ordinal())
								+ (String) list.get(k));
			} else {
				list.set(k, "\u00a77" + (String) list.get(k));
			}
		}

		if (TooltipUtils.showTip()) {
			float av = AVRegistry.getAV(stack);
			if (av>0) {
				float network = AetherNetwork.getAbsoluteStoredAV(te.getWorldObj(), te.xCoord,  te.yCoord,  te.zCoord);
				int qty = (int) (network/av);
				list.add("\u00a72"+"You can make "+"\u00a73"+NumberUtils.display(qty)+"\u00a72"+" of these.");
			}
		}

		if (list != null && stack != null) {
			FontRenderer font = stack.getItem().getFontRenderer(stack);
			this.drawHoveringText(list, par2, par3,
					(font == null ? fontRendererObj : font));
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
			itemRender.zLevel = 300.0F;
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
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

}