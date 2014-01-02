package com.iconmaster.aec.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.client.gui.GuiAetherCondenser;
import com.iconmaster.aec.client.gui.GuiAetherContainer;
import com.iconmaster.aec.client.gui.GuiAetherExtractor;
import com.iconmaster.aec.client.gui.GuiAetherInfuser;
import com.iconmaster.aec.client.gui.GuiAetherManipulator;
import com.iconmaster.aec.common.event.FallDamageEvent;
import com.iconmaster.aec.common.gui.ContainerAetherCondenser;
import com.iconmaster.aec.common.gui.ContainerAetherContainer;
import com.iconmaster.aec.common.gui.ContainerAetherExtractor;
import com.iconmaster.aec.common.gui.ContainerAetherInfuser;
import com.iconmaster.aec.common.gui.ContainerAetherManipulator;
import com.iconmaster.aec.common.item.ItemAetherCraftBlock;
import com.iconmaster.aec.common.item.ItemBlockInfused;
import com.iconmaster.aec.common.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.common.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.common.tileentity.TileEntityAetherInfuser;
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
		GameRegistry.registerTileEntity(TileEntityAetherInfuser.class,
				"aec.infuser");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockAetherManipulator,ItemAetherCraftBlock.class,"aetherManipulator");
		GameRegistry.registerBlock(AetherCraft.blockAetherContainer,ItemAetherCraftBlock.class,"aetherContainer");
		GameRegistry.registerBlock(AetherCraft.blockAetherConduit,"aetherConduit");
		GameRegistry.registerBlock(AetherCraft.blockAetherExtractor,ItemAetherCraftBlock.class,"aetherExtractor");
		GameRegistry.registerBlock(AetherCraft.blockAetherCondenser,ItemAetherCraftBlock.class,"aetherCondenser");
		GameRegistry.registerBlock(AetherCraft.blockAetherInfuser,ItemAetherCraftBlock.class,"aetherInfuser");
		GameRegistry.registerBlock(AetherCraft.blockInfused,ItemBlockInfused.class,"blockInfused");
		GameRegistry.registerItem(AetherCraft.itemAetherBattery,"aetherBattery");
		GameRegistry.registerItem(AetherCraft.itemInfused,"infusedItem");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			GameRegistry.registerItem(AetherCraft.itemFlyingRing,"flyingRing");
		}
	}

	public void addNames() {
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherManipulator,1,0), "Aether Manipulator");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherContainer,1,0), "Aether Container");
		LanguageRegistry.addName(AetherCraft.blockAetherConduit, "Aether Conduit");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherExtractor,1,0),"Aether Extractor");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherCondenser,1,0),"Aether Condenser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherInfuser,1,0),"Aether Infuser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,0),"Infused Block");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,1),"Infused Brick");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherManipulator,1,1), "Infused Manipulator");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherContainer,1,1), "Infused Container");
		//LanguageRegistry.addName(AetherCraft.blockAetherConduit, "Infused Conduit");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherExtractor,1,1),"Infused Extractor");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherCondenser,1,1),"Infused Condenser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherInfuser,1,1),"Aether Catalyzer");
		
		LanguageRegistry.addName(AetherCraft.itemAetherBattery,"Aether Battery");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,0),"Infused Ingot");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,1),"Aetheral Foci");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			LanguageRegistry.addName(AetherCraft.itemFlyingRing,"Flying Ring");
		}
	}

	public void addRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,0), "aaa","bcd","aaa",'a',Block.obsidian,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,0),'c',new ItemStack(AetherCraft.blockAetherConduit,1,0),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,0));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,0), "aba","cdc","efe",'a',Item.diamond,'b',Block.obsidian,'c',Block.obsidian,'d',Block.glowStone,'e',Item.redstone,'f',Block.obsidian);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,4,0), "aaa","bbb","aaa",'a',Block.obsidian,'b',new ItemStack(Item.dyePowder,1,4));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,0), "aba","cdc","aaa",'a',Block.obsidian,'c',Item.diamond,'b',new ItemStack(Item.dyePowder,1,4),'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,0), "aba","cdc","aaa",'a',Block.obsidian,'b',Item.emerald,'c',new ItemStack(Item.dyePowder,1,4),'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,0), "aba","cdc","aaa",'a',Block.obsidian,'b',Item.diamond,'c',Item.emerald,'d',Block.glowStone);
		
		ItemStack infusedIngot = new ItemStack(AetherCraft.itemInfused,1,0);
		ItemStack aetheralFoci = new ItemStack(AetherCraft.itemInfused,1,1);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,1), "aaa","bcd","aaa",'a',infusedIngot,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,1),'c',new ItemStack(AetherCraft.blockAetherConduit,1,0),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,1));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,1), "aba","cdc","efe",'a',Item.emerald,'b',infusedIngot,'c',infusedIngot,'d',Block.glowStone,'e',Item.glowstone,'f',infusedIngot);
		//GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,4,1), "aaa","bbb","aaa",'a',infusedIngot,'b',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,1), "aba","cdc","aaa",'a',infusedIngot,'c',Item.emerald,'b',Block.blockLapis,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',aetheralFoci,'c',Block.blockLapis,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',Item.emerald,'c',aetheralFoci,'d',Block.glowStone);

		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,1,0), "aaa","aaa","aaa",'a',new ItemStack(AetherCraft.itemInfused,1,0));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,1), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,1));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,0), "aaa","bcb","aaa",'a',Item.ingotGold,'b',Block.glass,'c',Item.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemFlyingRing,1,0), "aba","c c","ada",'a',Item.ingotGold,'b',Item.netherStar,'c',Item.emerald,'d',Item.feather);

		InfuserRegistry.addRecipe(new ItemStack(Item.ingotGold), new ItemStack(AetherCraft.itemInfused,1,0));
		InfuserRegistry.addRecipe(new ItemStack(Item.diamond), new ItemStack(AetherCraft.itemInfused,1,1));
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
		} else if (tileEntity instanceof TileEntityAetherInfuser && ID == AetherCraft.GUI_ID_INFUSER) {
			return new ContainerAetherInfuser(player.inventory,(TileEntityAetherInfuser) tileEntity);
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
		} else if (tileEntity instanceof TileEntityAetherInfuser && ID == AetherCraft.GUI_ID_INFUSER) {
			return new GuiAetherInfuser(player.inventory,(TileEntityAetherInfuser) tileEntity);
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
