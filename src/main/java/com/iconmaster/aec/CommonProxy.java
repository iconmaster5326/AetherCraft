package com.iconmaster.aec;

import com.iconmaster.aec.client.gui.*;
import com.iconmaster.aec.event.*;
import com.iconmaster.aec.inventory.*;
import com.iconmaster.aec.item.ItemAetherCraftBlock;
import com.iconmaster.aec.item.ItemBlockInfused;
import com.iconmaster.aec.tileentity.*;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IGuiHandler {
	public static int pumpRenderType = -1;
	public static int conduitRenderType = -1;
	public static int flameRenderType = -1;

	public void registerRenderInformation() {
	}

	public void registerTiles() {
		GameRegistry.registerTileEntity(TileEntityAetherManipulator.class,
				"aec.manipulator");
		GameRegistry.registerTileEntity(TileEntityAetherContainer.class,
				"aec.container");
		GameRegistry.registerTileEntity(TileEntityAetherExtractor.class,
				"aec.extractor");
		GameRegistry.registerTileEntity(TileEntityAetherCondenser.class,
				"aec.condenser");
		GameRegistry.registerTileEntity(TileEntityAetherInfuser.class,
				"aec.infuser");
		GameRegistry.registerTileEntity(TileEntityAetherReconstructor.class,
				"aec.reconstructor");
		GameRegistry.registerTileEntity(TileEntityAetherPump.class,
				"aec.pump");
		GameRegistry.registerTileEntity(TileEntityAetologistsChest.class,
				"aec.chest");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockAetherManipulator,ItemAetherCraftBlock.class,"aetherManipulator");
		GameRegistry.registerBlock(AetherCraft.blockAetherContainer,ItemAetherCraftBlock.class,"aetherContainer");
		GameRegistry.registerBlock(AetherCraft.blockAetherConduit,ItemAetherCraftBlock.class,"aetherConduit");
		GameRegistry.registerBlock(AetherCraft.blockAetherExtractor,ItemAetherCraftBlock.class,"aetherExtractor");
		GameRegistry.registerBlock(AetherCraft.blockAetherCondenser,ItemAetherCraftBlock.class,"aetherCondenser");
		GameRegistry.registerBlock(AetherCraft.blockAetherInfuser,ItemAetherCraftBlock.class,"aetherInfuser");
		GameRegistry.registerBlock(AetherCraft.blockAetherReconstructor,ItemAetherCraftBlock.class,"aetherReconstructor");
		GameRegistry.registerBlock(AetherCraft.blockAetherPump,ItemAetherCraftBlock.class,"aetherPump");
		GameRegistry.registerBlock(AetherCraft.blockInfused,ItemBlockInfused.class,"blockInfused");
		GameRegistry.registerBlock(AetherCraft.blockAetoChest,"aetoChest");
		
		GameRegistry.registerItem(AetherCraft.itemAetherBattery,"aetherBattery");
		GameRegistry.registerItem(AetherCraft.itemInfused,"infusedItem");
		GameRegistry.registerItem(AetherCraft.dummy,"dummy");
		
		if (AetherCraft.options.getBoolean("enableflyring")) {
			GameRegistry.registerItem(AetherCraft.itemFlyingRing,"flyingRing");
		}
		GameRegistry.registerItem(AetherCraft.itemRepairRing,"repairRing");
		GameRegistry.registerItem(AetherCraft.itemRegnerationRing,"regnerationRing");
		GameRegistry.registerItem(AetherCraft.itemPhasingRing,"phasingRing");
		GameRegistry.registerItem(AetherCraft.itemLightRing,"lightRing");
		GameRegistry.registerBlock(AetherCraft.blockAetherFlame,"aetherFlame");
		GameRegistry.registerItem(AetherCraft.itemTeleportRing,"teleRing");
		GameRegistry.registerItem(AetherCraft.itemTransmuteRing,"transmuteRing");
		
		GameRegistry.registerItem(AetherCraft.aetherPickaxe,"aetherPickaxe");
		GameRegistry.registerItem(AetherCraft.aetherAxe,"aetherAxe");
		GameRegistry.registerItem(AetherCraft.aetherShovel,"aetherShovel");
		GameRegistry.registerItem(AetherCraft.aetherSword,"aetherSword");
		GameRegistry.registerItem(AetherCraft.aetherHammer,"aetherHammer");
		GameRegistry.registerItem(AetherCraft.aetherShears,"aetherShears");
		
		GameRegistry.registerItem(AetherCraft.aetherHelmet,"aetherHelmet");
		GameRegistry.registerItem(AetherCraft.aetherChestplate,"aetherChestplate");
		GameRegistry.registerItem(AetherCraft.aetherLeggings,"aetherLeggings");
		GameRegistry.registerItem(AetherCraft.aetherBoots,"aetherBoots");
		
		GameRegistry.registerItem(AetherCraft.aetometer,"aetometer");
	}

	public void addRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,0), "aaa","bcd","aaa",'a',Blocks.obsidian,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,0),'c',new ItemStack(AetherCraft.blockAetherConduit,1,0),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,0));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,0), "aba","cdc","efe",'a',Items.diamond,'b',Blocks.obsidian,'c',Blocks.obsidian,'d',Blocks.glowstone,'e',Items.redstone,'f',Blocks.obsidian);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,4,0), "aaa","bbb","aaa",'a',Blocks.obsidian,'b',new ItemStack(Items.dye,1,4));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,0), "aba","cdc","aaa",'a',Blocks.obsidian,'c',Items.diamond,'b',new ItemStack(Items.dye,1,4),'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,0), "aba","cdc","aaa",'a',Blocks.obsidian,'b',Items.emerald,'c',new ItemStack(Items.dye,1,4),'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,0), "aba","cdc","aaa",'a',Blocks.obsidian,'b',Items.diamond,'c',Items.emerald,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherPump,1,0), "aaa","bcd","aaa",'a',Blocks.obsidian,'b',Items.diamond,'c',Blocks.glowstone,'d',Blocks.redstone_block);
		
		ItemStack infusedIngot = new ItemStack(AetherCraft.itemInfused,1,0);
		ItemStack aetheralFoci = new ItemStack(AetherCraft.itemInfused,1,1);
		ItemStack infusedRod = new ItemStack(AetherCraft.itemInfused,1,2);
		ItemStack band = new ItemStack(AetherCraft.itemInfused,1,3);
		ItemStack gildedIngot = new ItemStack(AetherCraft.itemInfused,1,4);
		ItemStack goldMatter = new ItemStack(AetherCraft.itemInfused,1,5);
		ItemStack singularity = new ItemStack(AetherCraft.itemInfused,1,6);
		ItemStack purePower = new ItemStack(AetherCraft.itemInfused,1,7);
		ItemStack eternalIngot = new ItemStack(AetherCraft.itemInfused,1,8);
		ItemStack pureCrystal = new ItemStack(AetherCraft.itemInfused,1,9);
		ItemStack cSingularity = new ItemStack(AetherCraft.itemInfused,1,10);
		
		ItemStack infusedBlock = new ItemStack(AetherCraft.blockInfused,1,0);
		ItemStack gildedBlock = new ItemStack(AetherCraft.blockInfused,1,2);
		ItemStack eternalBlock = new ItemStack(AetherCraft.blockInfused,1,4);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,1), "aaa","bcd","aaa",'a',infusedIngot,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,1),'c',new ItemStack(AetherCraft.blockAetherConduit,1,1),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,1));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,1), "aba","cdc","efe",'a',Items.emerald,'b',infusedIngot,'c',infusedIngot,'d',Blocks.glowstone,'e',Items.glowstone_dust,'f',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,8,1), "aaa","bbb","aaa",'a',infusedIngot,'b',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,1), "aba","cdc","aaa",'a',infusedIngot,'c',Items.emerald,'b',Blocks.lapis_block,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',aetheralFoci,'c',Blocks.lapis_block,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,1), "aba","cdc","aaa",'a',infusedIngot,'b',Items.emerald,'c',aetheralFoci,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherReconstructor,1,0), "aba","cdc","aaa",'a',Blocks.obsidian,'b',aetheralFoci,'c',infusedIngot,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherPump,1,1), "aaa","bcd","aaa",'a',infusedIngot,'b',Items.emerald,'c',Blocks.glowstone,'d',Blocks.redstone_block);
		
		GameRegistry.addShapedRecipe(infusedBlock, "aaa","aaa","aaa",'a',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,1), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,0), new ItemStack(AetherCraft.blockInfused,1,1));
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherManipulator,1,2), "aaa","bcd","aaa",'a',gildedIngot,'b',new ItemStack(AetherCraft.blockAetherExtractor,1,2),'c',new ItemStack(AetherCraft.blockAetherConduit,1,2),'d',new ItemStack(AetherCraft.blockAetherCondenser,1,2));
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherContainer,1,2), "aba","cdc","efe",'a',aetheralFoci,'b',infusedIngot,'c',gildedIngot,'d',Blocks.glowstone,'e',infusedIngot,'f',gildedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherConduit,16,2), "aaa","bbb","aaa",'a',gildedIngot,'b',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherExtractor,1,2), "aba","cdc","aaa",'a',gildedIngot,'b',aetheralFoci,'c',goldMatter,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherCondenser,1,2), "aba","cdc","aaa",'a',gildedIngot,'b',goldMatter,'c',aetheralFoci,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherInfuser,1,2), "aba","cdc","aaa",'a',gildedIngot,'b',goldMatter,'c',infusedIngot,'d',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetherPump,1,2), "aaa","bcd","aaa",'a',gildedIngot,'b',aetheralFoci,'c',Blocks.glowstone,'d',Blocks.redstone_block);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockAetoChest,1), "aba","cdc","aca",'a',Blocks.obsidian,'b',singularity,'c',gildedIngot,'d',Blocks.chest);
		
		GameRegistry.addShapedRecipe(gildedBlock, "aaa","aaa","aaa",'a',gildedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,3), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,4), new ItemStack(AetherCraft.blockInfused,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,4), new ItemStack(AetherCraft.blockInfused,1,3));
		
		GameRegistry.addShapedRecipe(infusedRod, " a "," b "," a ",'a',Items.gold_ingot,'b',infusedIngot);
		GameRegistry.addShapedRecipe(band, "aaa","aba","aaa",'a',Items.gold_ingot,'b',Items.lava_bucket);
		GameRegistry.addShapedRecipe(goldMatter, "cac","aba","cac",'a',Blocks.gold_block,'b',aetheralFoci,'c',Items.gold_nugget);
		GameRegistry.addShapedRecipe(purePower, "aba","bcb","aba",'a',Items.nether_star,'b',infusedBlock,'c',gildedBlock);
		GameRegistry.addShapedRecipe(pureCrystal, "aaa","aba","aaa",'a',purePower,'b',aetheralFoci);
		GameRegistry.addShapedRecipe(cSingularity, "aaa","aba","aaa",'a',singularity,'b',purePower);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,0), "aaa","bcb","aaa",'a',Items.gold_ingot,'b',Blocks.glass,'c',Items.glowstone_dust);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,1),"aba","bcb","aba",'a',Blocks.glass,'b',new ItemStack(AetherCraft.itemAetherBattery,1,0),'c',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemAetherBattery,1,2),"aba","bcb","aba",'a',Blocks.glass,'b',new ItemStack(AetherCraft.itemAetherBattery,1,1),'c',infusedIngot);
		
		if(AetherCraft.itemFlyingRing != null) {
			GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemFlyingRing,1,0), "eae","bcb","ede",'a',aetheralFoci,'b',Items.feather,'c',band,'d',Items.emerald,'e',gildedIngot);
		}
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemRepairRing,1,0), " a ","bcb"," d ",'a',AetherCraft.blockAetherReconstructor,'b',Items.iron_ingot,'c',band,'d',Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemRegnerationRing,1,0), "aba","dcf","aea",'a',infusedIngot,'b',goldMatter,'c',band,'d',Items.nether_wart,'e',Items.potionitem,'f',Items.ghast_tear);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemPhasingRing,1,0), " a ","dbd"," c ",'a',singularity,'b',band,'c',Blocks.glass,'d',Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemLightRing,1,0), " a ","cbc"," c ",'a',infusedIngot,'b',band,'c',Blocks.glowstone);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.itemTeleportRing,1,0), " a ","cbc"," d ",'a',singularity,'b',band,'c',Items.ender_pearl,'d',Items.ender_eye);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherPickaxe), "aaa"," b "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherAxe), "aa ","ab "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherShovel), " a "," b "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherSword), " a "," a "," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherHammer), "aaa","aba"," b ",'a',infusedIngot,'b',infusedRod);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherShears), "a "," a",'a',infusedIngot);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherHelmet),"   ","aaa","a a",'a',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherChestplate), "a a","aaa","aaa",'a',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherLeggings), "aaa","a a","a a",'a',infusedIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetherBoots), "   ","a a","a a",'a',infusedIngot);
		
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.aetometer), "b b","bab"," b ",'a',infusedIngot,'b',Blocks.obsidian);
		
		GameRegistry.addShapedRecipe(eternalBlock, "aaa","aaa","aaa",'a',eternalIngot);
		GameRegistry.addShapedRecipe(new ItemStack(AetherCraft.blockInfused,4,5), "aa","aa",'a',new ItemStack(AetherCraft.blockInfused,1,4));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,8), new ItemStack(AetherCraft.blockInfused,1,4));
		GameRegistry.addShapelessRecipe(new ItemStack(AetherCraft.itemInfused,9,8), new ItemStack(AetherCraft.blockInfused,1,5));
	}
	
	public static final int BLOCK_GUI_ID  = 0;
	public static final int RING_TRANSMUTE_GUI_ID  = 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID==BLOCK_GUI_ID) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityAetherManipulator) {
				return new ContainerAetherManipulator(player.inventory,(TileEntityAetherManipulator) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherContainer) {
				return new ContainerAetherContainer(player.inventory,(TileEntityAetherContainer) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherExtractor) {
				return new ContainerAetherExtractor(player.inventory,(TileEntityAetherExtractor) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherCondenser) {
				return new ContainerAetherCondenser(player.inventory,(TileEntityAetherCondenser) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherInfuser) {
				return new ContainerAetherInfuser(player.inventory,(TileEntityAetherInfuser) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherReconstructor) {
				return new ContainerAetherReconstructor(player.inventory,(TileEntityAetherReconstructor) tileEntity);
			} else if (tileEntity instanceof TileEntityAetologistsChest) {
				return new ContainerAetologistsChest(player.inventory,(TileEntityAetologistsChest) tileEntity);
			}
		} else if (ID == RING_TRANSMUTE_GUI_ID) {
			return new ContainerTransmuteRing(player.inventory,player.getHeldItem());
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID==BLOCK_GUI_ID) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityAetherManipulator) {
				return new GuiAetherManipulator(player.inventory,(TileEntityAetherManipulator) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherContainer) {
				return new GuiAetherContainer(player.inventory,(TileEntityAetherContainer) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherExtractor) {
				return new GuiAetherExtractor(player.inventory,(TileEntityAetherExtractor) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherCondenser) {
				return new GuiAetherCondenser(player.inventory,(TileEntityAetherCondenser) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherInfuser) {
				return new GuiAetherInfuser(player.inventory,(TileEntityAetherInfuser) tileEntity);
			} else if (tileEntity instanceof TileEntityAetherReconstructor) {
				return new GuiAetherReconstructor(player.inventory,(TileEntityAetherReconstructor) tileEntity);
			} else if (tileEntity instanceof TileEntityAetologistsChest) {
				return new GuiAetologistsChest(player.inventory,(TileEntityAetologistsChest) tileEntity);
			}
		} else if (ID == RING_TRANSMUTE_GUI_ID) {
			return new GuiTransmuteRing(player.inventory,player.getHeldItem());
		}
		
		return null;
	}

	public void registerHandlers() {

	}

	public void registerEventHandlers() {
		if (AetherCraft.options.getBoolean("enableflyring")) {
			MinecraftForge.EVENT_BUS.register(new FallDamageEvent());
		}
		MinecraftForge.EVENT_BUS.register(new AetherSwordEvent());
		FMLCommonHandler.instance().bus().register(new PlayerLogInEvent());
		MinecraftForge.EVENT_BUS.register(new DisableRingInContainerEvent());
		MinecraftForge.EVENT_BUS.register(new AetherArmorDamageEvent());
	}
	
	//@SideOnly(Side.SERVER)
	public void activateRings(Object ctx) {
		InventoryUtils.activateRings(((MessageContext)ctx).getServerHandler().playerEntity);
	}
	
	//@SideOnly(Side.SERVER)
	public void deactivateRings(Object ctx) {
		InventoryUtils.deactivateRings(((MessageContext)ctx).getServerHandler().playerEntity);
	}

	public void setCustomRenderers() {
		
	}
}
