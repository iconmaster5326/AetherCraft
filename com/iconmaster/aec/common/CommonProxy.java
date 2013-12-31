package com.iconmaster.aec.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.client.gui.GuiAetherCondenser;
import com.iconmaster.aec.client.gui.GuiAetherContainer;
import com.iconmaster.aec.client.gui.GuiAetherExtractor;
import com.iconmaster.aec.client.gui.GuiAetherManipulator;
import com.iconmaster.aec.common.event.FallDamageEvent;
import com.iconmaster.aec.common.gui.ContainerAetherCondenser;
import com.iconmaster.aec.common.gui.ContainerAetherContainer;
import com.iconmaster.aec.common.gui.ContainerAetherExtractor;
import com.iconmaster.aec.common.gui.ContainerAetherManipulator;
import com.iconmaster.aec.common.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.common.tileentity.TileEntityAetherExtractor;
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
		GameRegistry.registerTileEntity(TileEntityAetherExtractor.class,
				"aec.extractor");
		GameRegistry.registerTileEntity(TileEntityAetherCondenser.class,
				"aec.condenser");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockAetherManipulator,
				"aetherManipulator");
		GameRegistry.registerBlock(AetherCraft.blockAetherContainer,
				"aetherContainer");
		GameRegistry.registerBlock(AetherCraft.blockAetherConduit,
				"aetherConduit");
		GameRegistry.registerBlock(AetherCraft.blockAetherExtractor,
				"aetherExtractor");
		GameRegistry.registerBlock(AetherCraft.blockAetherCondenser,
				"aetherCondenser");
		GameRegistry.registerItem(AetherCraft.itemAetherBattery,
				"aetherBattery");
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
		LanguageRegistry.addName(AetherCraft.blockAetherConduit,
				"Aether Conduit");
		LanguageRegistry.addName(AetherCraft.blockAetherExtractor,
				"Aether Extractor");
		LanguageRegistry.addName(AetherCraft.blockAetherCondenser,
				"Aether Condenser");
		LanguageRegistry.addName(AetherCraft.itemAetherBattery,
				"Aether Battery");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			LanguageRegistry.addName(AetherCraft.itemFlyingRing,
					"Flying Ring");
		}
	}

	public void addRecipes() {
		//GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator), "aba","cdc","efe",'a',Item.diamond,'b',Item.emerald,'c',Block.obsidian,'d',Block.glowStone,'e',Item.redstone,'f',Item.cauldron);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator), "aaa","bcd","aaa",'a',Block.obsidian,'b',AetherCraft.blockAetherExtractor,'c',AetherCraft.blockAetherConduit,'d',AetherCraft.blockAetherCondenser);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer), "aba","cdc","efe",'a',Item.diamond,'b',Block.obsidian,'c',Block.obsidian,'d',Block.glowStone,'e',Item.redstone,'f',Block.obsidian);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,4,0), "aaa","bbb","aaa",'a',Block.obsidian,'b',new ItemStack(Item.dyePowder,1,4));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor), "aba","cdc","aaa",'a',Block.obsidian,'c',Item.diamond,'b',new ItemStack(Item.dyePowder,1,4),'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser), "aba","cdc","aaa",'a',Block.obsidian,'b',Item.emerald,'c',new ItemStack(Item.dyePowder,1,4),'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery), "aaa","bcb","aaa",'a',Item.ingotGold,'b',Block.glass,'c',Item.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemFlyingRing), "aba","c c","ada",'a',Item.ingotGold,'b',Item.netherStar,'c',Item.emerald,'d',Item.feather);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityAetherManipulator && ID == AetherCraft.GUI_ID_MANIPULATOR) {
			return new ContainerAetherManipulator(player.inventory,(TileEntityAetherManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherContainer && ID == AetherCraft.GUI_ID_CONTAINER) {
			return new ContainerAetherContainer(player.inventory,(TileEntityAetherContainer) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherExtractor && ID == AetherCraft.GUI_ID_EXTRACTOR) {
			return new ContainerAetherExtractor(player.inventory,(TileEntityAetherExtractor) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherCondenser && ID == AetherCraft.GUI_ID_CONDENSER) {
			return new ContainerAetherCondenser(player.inventory,(TileEntityAetherCondenser) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityAetherManipulator && ID == AetherCraft.GUI_ID_MANIPULATOR) {
			return new GuiAetherManipulator(player.inventory,(TileEntityAetherManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherContainer && ID == AetherCraft.GUI_ID_CONTAINER) {
			return new GuiAetherContainer(player.inventory,(TileEntityAetherContainer) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherExtractor && ID == AetherCraft.GUI_ID_EXTRACTOR) {
			return new GuiAetherExtractor(player.inventory,(TileEntityAetherExtractor) tileEntity);
		} else if (tileEntity instanceof TileEntityAetherCondenser && ID == AetherCraft.GUI_ID_CONDENSER) {
			return new GuiAetherCondenser(player.inventory,(TileEntityAetherCondenser) tileEntity);
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
