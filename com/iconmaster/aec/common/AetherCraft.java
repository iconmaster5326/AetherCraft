package com.iconmaster.aec.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import com.iconmaster.aec.client.ClientPacketHandler;
import com.iconmaster.aec.command.ConfigCommand;
import com.iconmaster.aec.common.block.BlockAetherContainer;
import com.iconmaster.aec.common.block.BlockAetherManipulator;
import com.iconmaster.aec.common.handler.network.ConnectionHandler;
import com.iconmaster.aec.common.item.ItemAetherBattery;
import com.iconmaster.aec.common.item.ItemFlyingRing;
import com.iconmaster.aec.config.DefaultAetherValuesConfig;
import com.iconmaster.aec.config.AetherManipulatorConfig;
import com.iconmaster.aec.config.DynamicAVRegister;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.Configuration;
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

@Mod(modid = "AetherCraft", name = "AetherCraft", version = "1.2.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = {
		"Aec", "AecReq", "AecTrans" }, packetHandler = ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = {
		"Aec", "AecReq", "AecTrans" }, packetHandler = ServerPacketHandler.class))
public class AetherCraft {
	// For NEI Config!
	public static final String VERSION = "1.2.0";

	public static final String DEFAULT_CONFIG_FILE = "default.cfg";
	public static final int GUI_ID_EM = 0;
	public static final int GUI_ID_EC = 1;
	public static final byte PACKET_TTID_CONFIG = 0;

	@Instance("AetherCraft")
	public static AetherCraft instance = new AetherCraft();

	@SidedProxy(clientSide = "com.iconmaster.aec.client.ClientProxy", serverSide = "com.iconmaster.aec.common.CommonProxy")
	public static CommonProxy proxy;

	public static Block blockAetherManipulator;
	public static Block blockAetherContainer;
	public static Item itemAetherBattery;
	public static Item itemFlyingRing;

	private static ArrayList<Integer> blockIds = new ArrayList<Integer>();
	private static ArrayList<Integer> itemIds = new ArrayList<Integer>();

	private static HashMap<String, Integer> currentConfigAV = new HashMap<String, Integer>();
	private static HashMap<String, Integer> aetherValues = new HashMap<String, Integer>();
	private static HashMap<String, String> options = new HashMap<String, String>();

	private static File configDir, forgeConfigFile, currentAVConfig;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//configDir = event.getSuggestedConfigurationFile().getParentFile();
		configDir = new File(event.getModConfigurationDirectory(), "aec-values/");
		configDir.mkdir();
		forgeConfigFile = event.getSuggestedConfigurationFile();

		this.reloadConfigFiles();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		// Blocks
		blockAetherManipulator = new BlockAetherManipulator(blockIds.get(0),
				Material.rock).setLightValue(0.3f);
		blockAetherContainer = new BlockAetherContainer(blockIds.get(1),
				Material.rock).setLightValue(0.2f);
		// Items
		itemAetherBattery = new ItemAetherBattery(itemIds.get(0));

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
		proxy.registerTickHandlers();
		proxy.registerEventHandlers();
		
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		DynamicAVRegister.addDynamicValues();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new ConfigCommand());
	}

	public static int getAetherValueByItemStack(ItemStack stack) {
		if (stack != null) {
			// Aether Battery
			if (stack.itemID == itemAetherBattery.itemID) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				int ev = 0;
				NBTTagCompound tag = stack.getTagCompound();
				if (tag.hasKey("EMAV")) {
					ev = tag.getInteger("EMAV");
				} else {
					tag.setInteger("EMAV", 0);
				}
				return ev;
			}

			if (stack.getUnlocalizedName() != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(stack.getUnlocalizedName());

				// Unlocalized Name
				if (aetherValues.containsKey(sb.toString())) {
					if (stack.getItem().isItemTool(stack) && stack.itemID != Item.book.itemID) {
						int extraEv = 0;
						if (stack.isItemEnchanted()) {
							NBTTagList tagList = stack.getEnchantmentTagList();
							for (int i = 0; i < tagList.tagCount(); i++) {
								NBTTagCompound tag = (NBTTagCompound) tagList
										.tagAt(i);
								short eid = tag.getShort("id");
								short elvl = tag.getShort("lvl");
								if (aetherValues.containsKey("enchantment_"
										+ eid)) {
									extraEv += aetherValues.get("enchantment_"
											+ eid)
											* elvl;
								}
							}
						}
						float a = (float) ((((float) aetherValues.get(sb
								.toString()) / (float) stack.getMaxDamage())
								* (float) (stack.getMaxDamage() - stack
										.getItemDamage()) + extraEv) * Double
								.parseDouble(getOptions("evmultiplier")));
						if (a <= 0) {
							return 0;
						} else {
							return (int) (a);
						}
					} else {
						Integer result = (int) ((float) aetherValues.get(sb
								.toString()) * Double
								.parseDouble(getOptions("evmultiplier")));
						return result != null ? result : 0;
					}
				}

				// Item ID
				else {
					sb = new StringBuilder();
					sb.append(Integer.toString(stack.itemID));
					if (stack.getHasSubtypes()) {
						sb.append(':');
						sb.append(Integer.toString(stack.getItemDamage()));
					}
					if (aetherValues.containsKey(sb.toString())) {
						if (stack.getItem().isItemTool(stack)) {
							int extraAv = 0;
							if (stack.isItemEnchanted()) {
								NBTTagList tagList = stack
										.getEnchantmentTagList();
								for (int i = 0; i < tagList.tagCount(); i++) {
									NBTTagCompound tag = (NBTTagCompound) tagList
											.tagAt(i);
									short eid = tag.getShort("id");
									short elvl = tag.getShort("lvl");
									if (aetherValues.containsKey("enchantment_"
											+ eid)) {
										extraAv += aetherValues
												.get("enchantment_" + eid)
												* elvl;
									}
								}
							}
							float a = (float) ((((float) aetherValues.get(sb
									.toString()) / (float) stack.getMaxDamage())
									* (float) (stack.getMaxDamage() - stack
											.getItemDamage()) + extraAv) * Double
									.parseDouble(getOptions("evmultiplier")));
							if (a <= 0) {
								return 0;
							} else {
								return (int) (a);
							}
						} else {
							Integer result = (int) ((float) aetherValues.get(sb
									.toString()) * Double
									.parseDouble(getOptions("evmultiplier")));
							return result != null ? result : 0;
						}
					} else {
						sb = new StringBuilder();
						sb.append(Integer.toString(stack.itemID));
						if (aetherValues.containsKey(sb.toString())) {
							if (stack.getItem().isItemTool(stack)) {
								int extraAv = 0;
								if (stack.isItemEnchanted()) {
									NBTTagList tagList = stack
											.getEnchantmentTagList();
									for (int i = 0; i < tagList.tagCount(); i++) {
										NBTTagCompound tag = (NBTTagCompound) tagList
												.tagAt(i);
										short eid = tag.getShort("id");
										short elvl = tag.getShort("lvl");
										if (aetherValues
												.containsKey("enchantment_"
														+ eid)) {
											extraAv += aetherValues
													.get("enchantment_" + eid)
													* elvl;
										}
									}
								}
								float a = (float) ((((float) aetherValues
										.get(sb.toString()) / (float) stack
										.getMaxDamage())
										* (float) (stack.getMaxDamage() - stack
												.getItemDamage()) + extraAv) * Double
										.parseDouble(getOptions("evmultiplier")));
								if (a <= 0) {
									return 0;
								} else {
									return (int) (a);
								}
							} else {
								Integer result = (int) ((float) aetherValues
										.get(sb.toString()) * Double
										.parseDouble(getOptions("evmultiplier")));
								return result != null ? result : 0;
							}
						} else {
							return 0;
						}
					}
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	public static boolean doesFileExist(File fileDirectory, String file) {
		File fileResult = new File(fileDirectory, file);
		if (fileResult.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getOptions(String option) {
		if (option != null && options.containsKey(option)) {
			return options.get(option);
		} else {
			return null;
		}
	}

	public static void reloadConfigFiles() {
		// ------------------- AETHER VALUE CONFIGS -------------------
		if (!doesFileExist(configDir, DEFAULT_CONFIG_FILE)) {
			DefaultAetherValuesConfig.createDefaultEvConfigFile(new File(
					configDir, DEFAULT_CONFIG_FILE));
		}
		AetherManipulatorConfig config = new AetherManipulatorConfig(new File(
				configDir, DEFAULT_CONFIG_FILE));
		config.getAllAetherValues(aetherValues);

		// ------------------- PLUG-IN AETHER VALUE CONFIGS -------------------
		File[] configFiles = configDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".cfg");
			}
		});
		for (File file : configFiles) {
			AetherManipulatorConfig tempConfig = new AetherManipulatorConfig(
					file);
			tempConfig.getAllAetherValues(aetherValues);
		}

		// ------------------- CONFIG -------------------
		Configuration forgeConfig = new Configuration(forgeConfigFile);
		forgeConfig.load();

		// ------------------- CONFIG OPTIONS -------------------

		// INTEGER AND DOUBLE
		options.put(
				"consumeprecission",
				Integer.toString(forgeConfig
						.get("options",
								"consumeprecission",
								100,
								"How much of the AV an Aether Manipulator will consume (in percentage without the %, range 0-100)")
						.getInt()));

		options.put(
				"evmultiplier",
				Double.toString(forgeConfig
						.get("options",
								"evmultiplier",
								1.0,
								"Easily ramp up the AVs with this multiplier, added to comphensate for consumeprecission changing values for stone and such to 0 (decimal number)")
						.getDouble(1.0)));

		options.put("emmaxstorage", Integer.toString(forgeConfig.get("options",
				"emmaxstorage", 32768,
				"How much aether an aether manipulator can store").getInt()));

		options.put("ecmaxstorage", Integer.toString(forgeConfig.get("options",
				"ecmaxstorage", 131072,
				"How much aether an aether container can store").getInt()));

		options.put("ebatterymaxstorage", Integer.toString(forgeConfig.get(
				"options", "ebatterymaxstorage", 16384,
				"How much aether an aether battery can store").getInt()));

		options.put("flycostpersecond", Integer.toString(forgeConfig.get(
				"options", "flycostpersecond", 8,
				"How much AV the flying ring will drain per second.").getInt()));

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
				"showevalways",
				Boolean.toString(forgeConfig.get("options", "showevalways",
						false, "").getBoolean(false)));

		// CRAFTING RECIPES
		options.put(
				"emrecipe",
				forgeConfig.get("options", "emrecipe",
						"264:0,388:0,264:0,49:0,348:0,49:0,331:0,380:0,331:0",
						"Top left to bottom right.").getString());
		options.put(
				"ecrecipe",
				forgeConfig.get("options", "ecrecipe",
						"264:0,388:0,264:0,49:0,89:0,49:0,264:0,388:0,264:0",
						"Top left to bottom right.").getString());
		options.put(
				"ebatteryrecipe",
				forgeConfig
						.get("options",
								"ebatteryrecipe",
								"348:0,331:0,348:0,331:0,264:0,331:0,348:0,331:0,348:0",
								"Top left to bottom right.").getString());
		options.put(
				"flyingringrecipe",
				forgeConfig
						.get("options",
								"flyingringrecipe",
								"401:0,288:0,401:0,264:0,399:0,264:0,401:0,288:0,401:0",
								"Top left to bottom right.").getString());

		// ------------------- REGISTER BLOCK/ITEM IDs -------------------
		blockIds.add(forgeConfig.getBlock("aethermanipulator", 2690).getInt());
		blockIds.add(forgeConfig.getBlock("aethercontainer", 2691).getInt());
		itemIds.add(forgeConfig.getItem("aetherbattery", 2700).getInt());
		itemIds.add(forgeConfig.getItem("flyingring", 2701).getInt());
		forgeConfig.save();
	}

	public static boolean addAVEntryToConfig(String entry, int value) {
		if (currentAVConfig != null) {
			currentConfigAV.put(entry, value);
			return true;
		} else {
			return false;
		}
	}

	public static boolean loadAVConfig(final String config) {
		File[] configFiles = configDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.equals("EMAV_" + config + ".cfg")) {
					return true;
				} else {
					return false;
				}
			}
		});
		if (configFiles.length > 0) {
			currentAVConfig = configFiles[0];
			currentConfigAV = new HashMap<String, Integer>();
			AetherManipulatorConfig tempConfig = new AetherManipulatorConfig(
					configFiles[0]);
			tempConfig.getAllAetherValues(currentConfigAV);
			return true;
		} else {
			return false;
		}
	}

	public static boolean createNewAVConfig(String config) {
		File tempConfig = new File(configDir, "EMAV_" + config + ".cfg");
		if (!tempConfig.exists()) {
			currentAVConfig = tempConfig;
			currentConfigAV = new HashMap<String, Integer>();
			return true;
		} else {
			return false;
		}
	}

	public static void saveCurrentAVConfig() {
		AetherManipulatorConfig tempConfig = new AetherManipulatorConfig(
				currentAVConfig);
		tempConfig.saveAetherValues(currentConfigAV);
	}

	public static File getCurrentConfigFile() {
		return currentAVConfig;
	}

	public static void unloadCurrentConfig() {
		currentConfigAV = new HashMap<String, Integer>();
		currentAVConfig = null;
	}

	public static boolean doesConfigExist(String config) {
		return doesFileExist(configDir, "EMAV_" + config + ".cfg");
	}

	public static int[] stringToCraftingArray(String input) {
		int[] output = new int[18];

		String[] s1 = input.split(",");
		int nextIndex = 0;

		for (String s2 : s1) {
			String[] s3 = s2.split(":");
			output[nextIndex] = Integer.parseInt(s3[0]);
			output[nextIndex + 1] = Integer.parseInt(s3[1]);
			nextIndex += 2;
		}
		return output;
	}

	public static ItemStack[] craftingArrayToItemStack(int[] input) {
		ItemStack[] output = new ItemStack[9];
		int index = 0;
		for (int i = 0; i < input.length; i += 2) {
			output[index] = new ItemStack(input[i], 1, input[i + 1]);
			index++;
		}
		return output;
	}

	public static HashMap<String, Integer> getAetherValuesMap() {
		return aetherValues;
	}

	public static HashMap<String, String> getOptionsMap() {
		return options;
	}

	public static void setAetherValuesMap(HashMap<String, Integer> evm) {
		aetherValues = evm;
	}

	public static void setOptionsMap(HashMap<String, String> om) {
		options = om;
	}
}