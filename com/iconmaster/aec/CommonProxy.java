package com.iconmaster.aec;

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
import com.iconmaster.aec.client.gui.GuiAetherReconstructor;
import com.iconmaster.aec.event.AetherSwordEvent;
import com.iconmaster.aec.event.DisableRingInContainerEvent;
import com.iconmaster.aec.event.FallDamageEvent;
import com.iconmaster.aec.inventory.ContainerAetherCondenser;
import com.iconmaster.aec.inventory.ContainerAetherContainer;
import com.iconmaster.aec.inventory.ContainerAetherExtractor;
import com.iconmaster.aec.inventory.ContainerAetherInfuser;
import com.iconmaster.aec.inventory.ContainerAetherManipulator;
import com.iconmaster.aec.inventory.ContainerAetherReconstructor;
import com.iconmaster.aec.item.ItemAetherCraftBlock;
import com.iconmaster.aec.item.ItemBlockInfused;
import com.iconmaster.aec.tileentity.TileEntityAetherCondenser;
import com.iconmaster.aec.tileentity.TileEntityAetherContainer;
import com.iconmaster.aec.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;
import com.iconmaster.aec.tileentity.TileEntityAetherManipulator;
import com.iconmaster.aec.tileentity.TileEntityAetherReconstructor;

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
		GameRegistry.registerTileEntity(TileEntityAetherReconstructor.class,
				"aec.reconstructor");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockAetherManipulator,ItemAetherCraftBlock.class,"aetherManipulator");
		GameRegistry.registerBlock(AetherCraft.blockAetherContainer,ItemAetherCraftBlock.class,"aetherContainer");
		GameRegistry.registerBlock(AetherCraft.blockAetherConduit,ItemAetherCraftBlock.class,"aetherConduit");
		GameRegistry.registerBlock(AetherCraft.blockAetherExtractor,ItemAetherCraftBlock.class,"aetherExtractor");
		GameRegistry.registerBlock(AetherCraft.blockAetherCondenser,ItemAetherCraftBlock.class,"aetherCondenser");
		GameRegistry.registerBlock(AetherCraft.blockAetherInfuser,ItemAetherCraftBlock.class,"aetherInfuser");
		GameRegistry.registerBlock(AetherCraft.blockAetherReconstructor,ItemAetherCraftBlock.class,"aetherReconstructor");
		GameRegistry.registerBlock(AetherCraft.blockInfused,ItemBlockInfused.class,"blockInfused");
		
		GameRegistry.registerItem(AetherCraft.itemAetherBattery,"aetherBattery");
		GameRegistry.registerItem(AetherCraft.itemInfused,"infusedItem");
		GameRegistry.registerItem(AetherCraft.dummy,"dummy");
		
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			GameRegistry.registerItem(AetherCraft.itemFlyingRing,"flyingRing");
		}
		GameRegistry.registerItem(AetherCraft.itemRepairRing,"repairRing");
		
		GameRegistry.registerItem(AetherCraft.aetherPickaxe,"aetherPickaxe");
		GameRegistry.registerItem(AetherCraft.aetherAxe,"aetherAxe");
		GameRegistry.registerItem(AetherCraft.aetherShovel,"aetherShovel");
		GameRegistry.registerItem(AetherCraft.aetherSword,"aetherSword");
		GameRegistry.registerItem(AetherCraft.aetherHammer,"aetherHammer");
		GameRegistry.registerItem(AetherCraft.aetherShears,"aetherShears");
	}

	public void addNames() {
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherManipulator,1,0), "Aether Manipulator");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherContainer,1,0), "Aether Container");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherConduit,1,0), "Aether Conduit");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherExtractor,1,0),"Aether Extractor");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherCondenser,1,0),"Aether Condenser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherInfuser,1,0),"Aether Infuser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherReconstructor,1,0),"Aether Reconstructor");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,0),"Infused Block");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,1),"Infused Brick");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherManipulator,1,1), "Infused Manipulator");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherContainer,1,1), "Infused Container");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherConduit,1,1), "Infused Conduit");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherExtractor,1,1),"Infused Extractor");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherCondenser,1,1),"Infused Condenser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherInfuser,1,1),"Aether Catalyzer");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,2),"Gilded Block");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockInfused,1,3),"Gilded Brick");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherManipulator,1,2), "Gilded Manipulator");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherContainer,1,2), "Gilded Container");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherConduit,1,2), "Gilded Conduit");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherExtractor,1,2),"Gilded Extractor");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherCondenser,1,2),"Gilded Condenser");
		LanguageRegistry.addName(new ItemStack(AetherCraft.blockAetherInfuser,1,2),"Gilded Infuser");
		
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemAetherBattery,1,0),"Aether Cell");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemAetherBattery,1,1),"Aether Battery");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,0),"Infused Ingot");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,1),"Aetheral Foci");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,2),"Infused Rod");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,3),"Wrought Band");
		LanguageRegistry.addName(new ItemStack(AetherCraft.itemInfused,1,4),"Gilded Ingot");
		
		LanguageRegistry.addName(AetherCraft.aetherPickaxe,"Aether Pickaxe");
		LanguageRegistry.addName(AetherCraft.aetherAxe,"Aether Axe");
		LanguageRegistry.addName(AetherCraft.aetherShovel,"Aether Shovel");
		LanguageRegistry.addName(AetherCraft.aetherSword,"Aether Sword");
		LanguageRegistry.addName(AetherCraft.aetherHammer,"Aether Hammer");
		LanguageRegistry.addName(AetherCraft.aetherShears,"Aether Shears");
		
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			LanguageRegistry.addName(AetherCraft.itemFlyingRing,"Ring of Flight");
		}
		LanguageRegistry.addName(AetherCraft.itemRepairRing,"Ring of Repair");
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
		ItemStack infusedRod = new ItemStack(AetherCraft.itemInfused,1,2);
		ItemStack band = new ItemStack(AetherCraft.itemInfused,1,3);
		ItemStack gildedIngot = new ItemStack(AetherCraft.itemInfused,1,4);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,1), "aaa","bcd","aaa",'a',infusedIngot,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,1),'c',new ItemStack(AetherCraft.blockAetherConduit,1,1),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,1));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,1), "aba","cdc","efe",'a',Item.emerald,'b',infusedIngot,'c',infusedIngot,'d',Block.glowStone,'e',Item.glowstone,'f',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,8,1), "aaa","bbb","aaa",'a',infusedIngot,'b',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,1), "aba","cdc","aaa",'a',infusedIngot,'c',Item.emerald,'b',Block.blockLapis,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',aetheralFoci,'c',Block.blockLapis,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',Item.emerald,'c',aetheralFoci,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherReconstructor,1,0), "aba","cdc","aaa",'a',Block.obsidian,'b',aetheralFoci,'c',infusedIngot,'d',Block.glowStone);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,1,0), "aaa","aaa","aaa",'a',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,1), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,1));
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,2), "aaa","bcd","aaa",'a',gildedIngot,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,2),'c',new ItemStack(AetherCraft.blockAetherConduit,1,2),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,2));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,2), "aba","cdc","efe",'a',aetheralFoci,'b',infusedIngot,'c',gildedIngot,'d',Block.glowStone,'e',infusedIngot,'f',gildedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,8,2), "aaa","bbb","aaa",'a',gildedIngot,'b',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,2), "aba","cdc","aaa",'a',gildedIngot,'c',aetheralFoci,'b',Item.emerald,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,2), "aba","cdc","aaa",'a',gildedIngot,'b',aetheralFoci,'c',Item.emerald,'d',Block.glowStone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,2), "aba","cdc","aaa",'a',gildedIngot,'b',aetheralFoci,'c',aetheralFoci,'d',Block.glowStone);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,1,2), "aaa","aaa","aaa",'a',gildedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,3), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,4), new ItemStack(AetherCraft.blockInfused,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,4), new ItemStack(AetherCraft.blockInfused,1,3));
		
		GameRegistry.addShapedRecipe(infusedRod, " a "," b "," a ",'a',Item.ingotGold,'b',infusedIngot);
		GameRegistry.addShapedRecipe(band, "aaa","aba","aaa",'a',Item.ingotGold,'b',Item.bucketLava);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,0), "aaa","bcb","aaa",'a',Item.ingotGold,'b',Block.glass,'c',Item.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,1),"aba","bcb","aba",'a',Block.glass,'b',new ItemStack(AetherCraft.itemAetherBattery,1,0),'c',infusedIngot);
		
		if(AetherCraft.itemFlyingRing != null) {
			GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemFlyingRing,1,0), "eae","bcb","ede",'a',aetheralFoci,'b',Item.feather,'c',band,'d',Item.emerald,'e',infusedIngot);
		}
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemRepairRing,1,0), " a ","bcb"," d ",'a',AetherCraft.blockAetherReconstructor,'b',Item.ingotIron,'c',band,'d',Item.diamond);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherPickaxe), "aaa"," b "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherAxe), "aa ","ab "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherShovel), " a "," b "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherSword), " a "," a "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherHammer), "aaa","aba"," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherShears), "a "," a",'a',infusedIngot);
		
		InfuserRegistry.addRecipe(new ItemStack(Item.ingotGold), infusedIngot);
		InfuserRegistry.addRecipe(new ItemStack(Item.diamond), aetheralFoci);
		InfuserRegistry.addRecipe(infusedIngot, gildedIngot);
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
		} else if (tileEntity instanceof TileEntityAetherReconstructor && ID == AetherCraft.GUI_ID_RECONSTRUCTOR) {
			return new ContainerAetherReconstructor(player.inventory,(TileEntityAetherReconstructor) tileEntity);
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
		} else if (tileEntity instanceof TileEntityAetherReconstructor && ID == AetherCraft.GUI_ID_RECONSTRUCTOR) {
			return new GuiAetherReconstructor(player.inventory,(TileEntityAetherReconstructor) tileEntity);
		}
		return null;
	}

	public void registerHandlers() {

	}

	public void registerEventHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			MinecraftForge.EVENT_BUS.register(new FallDamageEvent());
		}
		MinecraftForge.EVENT_BUS.register(new AetherSwordEvent());
	}
}
