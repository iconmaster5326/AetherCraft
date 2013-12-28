package com.iconmaster.aec.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.client.gui.GuiAetherContainer;
import com.iconmaster.aec.client.gui.GuiAetherManipulator;
import com.iconmaster.aec.common.event.FallDamageEvent;
import com.iconmaster.aec.common.gui.ContainerAetherContainer;
import com.iconmaster.aec.common.gui.ContainerAetherManipulator;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.common.tileentity.TileEntityAetherManipulator;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy implements IGuiHandler {
	public void registerRenderInformation() {
	}

	public void registerTiles() {
		GameRegistry.registerTileEntity(TileEntityAetherManipulator.class,
				"gra.tileentityem");
		GameRegistry.registerTileEntity(TileEntityAetherContainer.class,
				"gra.tileentityec");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockAetherManipulator,
				"energyManipulator");
		GameRegistry.registerBlock(AetherCraft.blockAetherContainer,
				"energyContainer");
		GameRegistry.registerItem(AetherCraft.itemAetherBattery,
				"energyBattery");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			GameRegistry.registerItem(AetherCraft.itemFlyingRing,
					"flyingRing");
		}
	}

	public void addNames() {
		LanguageRegistry.addName(AetherCraft.blockAetherManipulator,
				"Aether Manipulator");
		LanguageRegistry.addName(AetherCraft.blockAetherContainer,
				"Aether Container");
		LanguageRegistry.addName(AetherCraft.itemAetherBattery,
				"Aether Battery");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			LanguageRegistry.addName(AetherCraft.itemFlyingRing,
					"Flying Ring");
		}
	}

	public void addRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator), "aba","cdc","efe",'a',Item.diamond,'b',Item.emerald,'c',Block.obsidian,'d',Block.glowStone,'e',Item.redstone,'f',Item.cauldron);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer), "aba","cdc","efe",'a',Item.diamond,'b',Block.obsidian,'c',Block.obsidian,'d',Block.glowStone,'e',Item.diamond,'f',Block.obsidian);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery), "aaa","bcb","aaa",'a',Item.ingotGold,'b',Block.glass,'c',Item.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemFlyingRing), "aba","c c","ada",'a',Item.ingotGold,'b',Item.netherStar,'c',Item.emerald,'d',Item.feather);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityAetherManipulator
				&& ID == AetherCraft.GUI_ID_EM) {
			return new ContainerAetherManipulator(player.inventory,
					(TileEntityAetherManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherContainer
				&& ID == AetherCraft.GUI_ID_EC) {
			return new ContainerAetherContainer(player.inventory,
					(TileEntityAetherContainer) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityAetherManipulator
				&& ID == AetherCraft.GUI_ID_EM) {
			return new GuiAetherManipulator(player.inventory,
					(TileEntityAetherManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherContainer
				&& ID == AetherCraft.GUI_ID_EC) {
			return new GuiAetherContainer(player.inventory,
					(TileEntityAetherContainer) tileEntity);
		}
		return null;
	}

	public void registerHandlers() {

	}

	public void registerEventHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			MinecraftForge.EVENT_BUS.register(new FallDamageEvent());
		}
	}
}
