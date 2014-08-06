package com.iconmaster.aec.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map.Entry;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;

public class AVConfigHandler {
	private static DefaultAVConfig defs;
	private static AetherCraftAVConfig aecdefs;
	public final static String DEFAULT_DIR = "default.cfg";
	public final static String AEC_DEFAULT_DIR = "AetherCraft.cfg";
	
	public static void loadAllConfigFiles() {
		File dir = AetherCraft.getConfigDir();
		
		//first get the default config file
		boolean didExist = doesFileExist(dir,DEFAULT_DIR);
		defs  = new DefaultAVConfig(new File(dir,DEFAULT_DIR));
		if (!didExist) {
			defs.setupDefault();
			defs.saveValues();
		} else {
			defs.loadValues();
		}
		defs.addValuesToTable();
		
		//AND the AeC config file
		didExist = doesFileExist(dir,AEC_DEFAULT_DIR);
		aecdefs  = new AetherCraftAVConfig(new File(dir,AEC_DEFAULT_DIR));
		if (!didExist) {
			aecdefs.setupDefault();
			aecdefs.saveValues();
		} else {
			aecdefs.loadValues();
		}
		aecdefs.addValuesToTable();
		
		//next get any plugin config files
		File[] configFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(DEFAULT_DIR) &&  !name.equals(AEC_DEFAULT_DIR) && name.endsWith(".cfg");
			}
		});
		for (File file : configFiles) {
			System.out.println("[AEC] Found a config file: "+file);
			AVConfig config = new AVConfig(file);
			config.loadValues();
			config.addValuesToTable();
		}
	}
	
	public static void loadNetworkConfigFile(HashMap stringValues) {
		for (Object entry : stringValues.entrySet().toArray()) {
			String item = (String) ((Entry)entry).getKey();
			float av = (Float) ((Entry)entry).getValue();
			
			AVRegistry.setConfigAV(item,av);
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

	public static HashMap getNetworkConfigMap() {
		HashMap allStrings  = new HashMap();
		File dir = AetherCraft.getConfigDir();
		File[] configFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".cfg");
			}
		});
		for (File file : configFiles) {
			AVConfig config = new AVConfig(file);
			config.loadValues();
			allStrings.putAll(config.getValueStrings());
		}
		return allStrings;
	}
}
