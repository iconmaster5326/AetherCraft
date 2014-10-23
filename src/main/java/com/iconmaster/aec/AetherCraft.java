package com.iconmaster.aec;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.block.*;
import com.iconmaster.aec.command.ConfigCommand;
import com.iconmaster.aec.config.Options;
import com.iconmaster.aec.item.*;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.avlib.AVLib;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.EnumHelper;

import java.io.File;

@Mod(modid = AetherCraft.MODID, version = AetherCraft.VERSION,guiFactory="com.iconmaster.aec.config.ConfigGui")
public class AetherCraft {
	
    public static final String MODID = "AetherCraft";

    //public static final String VERSION = "test"; //for testing server/client connections
    public static final String VERSION = "@VERSION@";

	public static final String DEFAULT_CONFIG_FILE = "default.cfg";
	
	@Instance("AetherCraft")
	public static AetherCraft instance = new AetherCraft();

	@SidedProxy(clientSide = "com.iconmaster.aec.client.ClientProxy", serverSide = "com.iconmaster.aec.CommonProxy")
	public static CommonProxy proxy;

	public static Block blockAetherManipulator;
	public static Block blockAetherContainer;
	public static Block blockAetherConduit;
	public static Block blockAetherExtractor;
	public static Block blockAetherCondenser;
	public static Block blockAetherInfuser;
	public static Block blockAetherReconstructor;
	public static Block blockAetherPump;
	public static Block blockInfused;
	public static Block blockAetoChest;
	public static Item itemAetherBattery;
	public static Item itemFlyingRing;
	public static Item itemRepairRing;
	public static Item itemRegnerationRing;
	public static int regnerationPotionId;
	public static Potion regnerationPotion;
	public static Item itemPhasingRing;
	public static Item itemLightRing;
	public static Item itemTeleportRing;
	public static Item itemTransmuteRing;
	public static Item itemInfused;
	public static Item dummy;
	public static Item aetherPickaxe;
	public static Item aetherAxe;
	public static Item aetherShovel;
	public static Item aetherSword;
	public static Item aetherHammer;
	public static Item aetherShears;
	public static Item aetherHelmet;
	public static Item aetherChestplate;
	public static Item aetherLeggings;
	public static Item aetherBoots;
	public static Item aetometer;
	public static Block blockAetherFlame;

	private static File configDir;

	private static File forgeConfigFile;

	public static Options options = new Options();
	
	 public static CreativeTabs tabAetherCraft  = new CreativeTabs("aetherCraft") {
         public ItemStack getIconItemStack() {
                 return new ItemStack(blockAetherManipulator, 1, 0);
         }

		@Override
		public Item getTabIconItem() {
			return null;
		}
	 };
	 
	 public static ToolMaterial aetherMaterial = EnumHelper.addToolMaterial("aether", 3, 1250, 6.0F, 2.0F, 18);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configDir = new File(event.getModConfigurationDirectory(), "aec-values/");
		getConfigDir().mkdir();
		forgeConfigFile = event.getSuggestedConfigurationFile();

		options.loadConfig(forgeConfigFile);
		options.addDefaultOptions();
		options.readConfig();

		options.writeConfig();
		options.saveConfig();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		// Blocks
		blockAetherManipulator  = new BlockAetherManipulator(Material.rock,"Manipulator").setLightLevel(0.3f);
		blockAetherContainer  = new BlockAetherContainer(Material.rock,"Container").setLightLevel(0.3f);
		blockAetherConduit  = new BlockAetherConduit(Material.rock).setLightLevel(0.2f);
		blockAetherExtractor = new BlockAetherExtractor(Material.rock,"Extractor").setLightLevel(0.3f);
		blockAetherCondenser = new BlockAetherCondenser(Material.rock,"Condenser").setLightLevel(0.3f);
		blockAetherInfuser = new BlockAetherInfuser(Material.rock,"Infuser").setLightLevel(0.3f);
		blockAetherReconstructor = new BlockAetherReconstructor(Material.rock,"Reconstructor").setLightLevel(0.3f);
		blockAetherPump= new BlockAetherPump(Material.rock,"Pump").setLightLevel(0.3f);
		blockInfused = new BlockInfused(Material.rock).setLightLevel(0.7f);
		blockAetoChest  = new BlockAetologistsChest(Material.rock).setLightLevel(0.3f);
		// Items
		itemAetherBattery = new ItemAetherBattery();
		itemInfused = new ItemInfused();
		dummy = new ItemDummy();
		aetometer = new ItemAetometer();
		//Tools
		aetherPickaxe = new ItemAetherPickaxe();
		aetherAxe = new ItemAetherAxe();
		aetherShovel = new ItemAetherShovel();
		aetherSword = new ItemAetherSword();
		aetherHammer = new ItemAetherHammer();
		aetherShears = new ItemAetherShears();
		
		//Armor
		aetherHelmet = new ItemAetherArmor(0);
		aetherChestplate = new ItemAetherArmor(1);
		aetherLeggings= new ItemAetherArmor(2);
		aetherBoots = new ItemAetherArmor(3);
		
		//Rings
		if (options.getBoolean("enableflyring")) {
			itemFlyingRing = new ItemFlyingRing();
		}
		itemRepairRing = new ItemRepairRing();
		this.regnerationPotionId = options.getInt("regnerationid");
		itemRegnerationRing = new ItemRegnerationRing();
		this.regnerationPotion = new PotionRegneration(this.regnerationPotionId);
		Potion.potionTypes[this.regnerationPotionId] = this.regnerationPotion;
		itemPhasingRing = new ItemPhasingRing();
		itemLightRing = new ItemLightRing();
		blockAetherFlame = new BlockAetherFlame(Material.rock);
		itemTeleportRing = new ItemTeleportRing();
		itemTransmuteRing = new ItemTransmuteRing();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		
		AetherCraftPacketHandler.init();
		
		proxy.registerTiles();
		proxy.registerBlocks();
		proxy.addRecipes();
		proxy.registerHandlers();
		proxy.registerEventHandlers();
		proxy.setCustomRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		//AVRegistry.reloadAllValues();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		InfuserRegistry.addDefaultRecipes();
		AVRegistry.reloadAllValues(); //Just to make sure some mods last-minute registered stuff gets added
		event.registerServerCommand(new ConfigCommand());

		AVLib.testLib();
	}

	public static File getConfigDir() {
		return configDir;
	}
}