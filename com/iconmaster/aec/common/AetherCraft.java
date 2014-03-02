package com.iconmaster.aec.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.client.ClientPacketHandler;
import com.iconmaster.aec.client.ClientProxy;
import com.iconmaster.aec.command.ConfigCommand;
import com.iconmaster.aec.common.block.BlockAetherCondenser;
import com.iconmaster.aec.common.block.BlockAetherConduit;
import com.iconmaster.aec.common.block.BlockAetherContainer;
import com.iconmaster.aec.common.block.BlockAetherExtractor;
import com.iconmaster.aec.common.block.BlockAetherInfuser;
import com.iconmaster.aec.common.block.BlockAetherManipulator;
import com.iconmaster.aec.common.block.BlockAetherReconstructor;
import com.iconmaster.aec.common.block.BlockInfused;
import com.iconmaster.aec.common.handler.ConnectionHandler;
import com.iconmaster.aec.common.item.ItemAetherBattery;
import com.iconmaster.aec.common.item.ItemFlyingRing;
import com.iconmaster.aec.common.item.ItemInfused;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "AetherCraft", name = "AetherCraft", version = "@VERSION@")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = {
		"Aec", "AecReq", "AecTrans" }, packetHandler = ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = {
		"Aec", "AecReq", "AecTrans" }, packetHandler = ServerPacketHandler.class))
public class AetherCraft {
	// For NEI Config!
	public static final String VERSION = "@VERSION@";

	public static final String DEFAULT_CONFIG_FILE = "default.cfg";
	public static final int GUI_ID_MANIPULATOR = 0;
	public static final int GUI_ID_CONTAINER = 1;
	public static final int GUI_ID_EXTRACTOR = 2;
	public static final int GUI_ID_CONDENSER = 3;
	public static final int GUI_ID_INFUSER = 4;
	public static final int GUI_ID_RECONSTRUCTOR = 5;
	
	public static final byte PACKET_TTID_CONFIG = 0;

	@Instance("AetherCraft")
	public static AetherCraft instance = new AetherCraft();

	@SidedProxy(clientSide = "com.iconmaster.aec.client.ClientProxy", serverSide = "com.iconmaster.aec.common.CommonProxy")
	public static CommonProxy proxy;

	public static Block blockAetherManipulator;
	public static Block blockAetherContainer;
	public static Block blockAetherConduit;
	public static Block blockAetherExtractor;
	public static Block blockAetherCondenser;
	public static Block blockAetherInfuser;
	public static Block blockAetherReconstructor;
	public static Block blockInfused;
	public static Item itemAetherBattery;
	public static Item itemFlyingRing;
	public static Item itemInfused;

	static ArrayList<Integer> blockIds = new ArrayList<Integer>();
	private static ArrayList<Integer> itemIds = new ArrayList<Integer>();

	private static HashMap<String, Float> currentConfigAV = new HashMap<String, Float>();
	private static HashMap<String, String> options = new HashMap<String, String>();

	private static File configDir;

	private static File forgeConfigFile;

	private static File currentAVConfig;
	
	 public static CreativeTabs tabAetherCraft  = new CreativeTabs("aetherCraft") {
         public ItemStack getIconItemStack() {
                 return new ItemStack(blockAetherManipulator, 1, 0);
         }
	 };

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//configDir = event.getSuggestedConfigurationFile().getParentFile();
		configDir = new File(event.getModConfigurationDirectory(), "aec-values/");
		getConfigDir().mkdir();
		forgeConfigFile = event.getSuggestedConfigurationFile();

		this.reloadConfigFiles();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		// Blocks
		blockAetherManipulator  = new BlockAetherManipulator(blockIds.get(0),
				Material.rock,"Manipulator").setLightValue(0.3f);
		blockAetherContainer  = new BlockAetherContainer(blockIds.get(1),
				Material.rock,"Container").setLightValue(0.3f);
		blockAetherConduit  = new BlockAetherConduit(blockIds.get(2),
				Material.rock).setLightValue(0.2f);
		blockAetherExtractor = new BlockAetherExtractor(blockIds.get(3),
				Material.rock,"Extractor").setLightValue(0.3f);
		blockAetherCondenser = new BlockAetherCondenser(blockIds.get(4),
				Material.rock,"Condenser").setLightValue(0.3f);
		blockAetherInfuser = new BlockAetherInfuser(blockIds.get(5),
				Material.rock,"Infuser").setLightValue(0.3f);
		blockAetherReconstructor = new BlockAetherReconstructor(blockIds.get(7),
				Material.rock,"Reconstructor").setLightValue(0.3f);
		blockInfused = new BlockInfused(blockIds.get(6),
				Material.rock).setLightValue(0.7f);
		// Items
		itemAetherBattery = new ItemAetherBattery(itemIds.get(0));
		
		itemInfused = new ItemInfused(itemIds.get(2));

		// Rings
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			itemFlyingRing = new ItemFlyingRing(itemIds.get(1));
		}

		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		NetworkRegistry.instance().registerConnectionHandler(
				new ConnectionHandler());
		proxy.registerTiles();
		proxy.registerBlocks();
		proxy.addNames();
		proxy.addRecipes();
		proxy.registerHandlers();
		proxy.registerEventHandlers();
		ClientProxy.setCustomRenderers();
		LanguageRegistry.instance().addStringLocalization("itemGroup.aetherCraft", "en_US", "AetherCraft");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		//AVRegistry.reloadAllValues();
		
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		AVRegistry.reloadAllValues(); //Just to make sure some mods last-minute registered stuff gets added
		event.registerServerCommand(new ConfigCommand());
	}



	public static String getOptions(String option) {
		if (option != null && options.containsKey(option)) {
			return options.get(option);
		} else {
			return null;
		}
	}

	public static void reloadConfigFiles() {
		
		// ------------------- CONFIG -------------------
		Configuration forgeConfig = new Configuration(forgeConfigFile);
		forgeConfig.load();

		// ------------------- CONFIG OPTIONS -------------------

		// INTEGER AND DOUBLE
		options.put(
				"consumeprecision",
				Float.toString(forgeConfig
						.get("options",
								"consumeprecision",
								100,
								"How much of the AV an Aether Manipulator will consume (in percentage without the %, range 0-100)")
						.getInt()));

		options.put(
				"avmultiplier",
				Double.toString(forgeConfig
						.get("options",
								"avmultiplier",
								1.0,
								"Easily ramp up the AVs with this multiplier, added to comphensate for consumeprecision changing values for stone and such to 0 (decimal number)")
						.getDouble(1.0)));

		options.put("ammaxstorage", Integer.toString(forgeConfig.get("options",
				"ammaxstorage", 32768,
				"How much aether an aether manipulator can store").getInt()));

		options.put("acmaxstorage", Float.toString(forgeConfig.get("options",
				"acmaxstorage", 65536,
				"How much aether an aether container can store").getInt()));

		options.put("abatterymaxstorage", Float.toString(forgeConfig.get(
				"options", "aabatterymaxstorage", 16384,
				"How much aether an aether battery can store").getInt()));

		options.put("flycostpersecond", Float.toString(forgeConfig.get(
				"options", "flycostpersecond", 8,
				"How much AV the flying ring will drain per second.").getInt()));
		
		options.put("chargerate", Integer.toString(forgeConfig.get("options",
				"chargerate", 128,
				"How fast aether batteries placed in an aether container charge").getInt()));
		
		options.put("flowrate", Integer.toString(forgeConfig.get("options",
				"flowrate", 8192,
				"How much AV a basic Conduit can transfer maximum.").getInt()));
		
		options.put("rfperav", Double.toString(forgeConfig.get("options",
				"rfperav", 0.0,
				"How much RF aether extractors use to extract one AV.").getDouble(0.0)));
		
		options.put("maxrf", Integer.toString(forgeConfig.get("options",
				"maxrf", 32000,
				"How much RF aether extractors can contain.").getInt()));
		
		// BOOLEAN
		options.put(
				"instantconsume",
				Boolean.toString(forgeConfig
						.get("options",
								"instantconsume",
								false,
								"Whether stacks will get get transmuted/consumed instantly or not")
						.getBoolean(false)));

		options.put(
				"debug",
				Boolean.toString(forgeConfig.get("options", "debug", false,
						"").getBoolean(false)));

		options.put(
				"enableflyring",
				Boolean.toString(forgeConfig.get("options", "enableflyring",
						true, "").getBoolean(true)));

		options.put(
				"showavalways",
				Boolean.toString(forgeConfig.get("options", "showavalways",
						false, "").getBoolean(false)));


		// ------------------- REGISTER BLOCK/ITEM IDs -------------------
		blockIds.add(forgeConfig.getBlock("aethermanipulator", 2690).getInt());
		blockIds.add(forgeConfig.getBlock("aethercontainer", 2691).getInt());
		blockIds.add(forgeConfig.getBlock("aetherconduit", 2692).getInt());
		blockIds.add(forgeConfig.getBlock("aetherextractor", 2693).getInt());
		blockIds.add(forgeConfig.getBlock("aethercondenser", 2694).getInt());
		blockIds.add(forgeConfig.getBlock("aetherinfuser", 2695).getInt());
		blockIds.add(forgeConfig.getBlock("infusedblock", 2696).getInt());
		blockIds.add(forgeConfig.getBlock("aetherreconstructor", 2697).getInt());
		itemIds.add(forgeConfig.getItem("aetherbattery", 2700).getInt());
		itemIds.add(forgeConfig.getItem("flyingring", 2701).getInt());
		itemIds.add(forgeConfig.getItem("infuseditem", 2702).getInt());
		forgeConfig.save();
	}

	public static HashMap<String, String> getOptionsMap() {
		return options;
	}

	public static void setOptionsMap(HashMap<String, String> om) {
		options = om;
	}

	public static File getConfigDir() {
		return configDir;
	}
}