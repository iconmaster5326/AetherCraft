package com.iconmaster.aec.config;

import java.io.File;
import java.io.FilenameFilter;

import com.iconmaster.aec.common.AetherCraft;

public class AVConfigHandler {
	private static DefaultAVConfig defs;
	public final static String DEFAULT_DIR = "default.cfg";
	
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
		
		//next get any plugin config files
		File[] configFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(DEFAULT_DIR) && name.endsWith(".cfg");
			}
		});
		for (File file : configFiles) {
			System.out.println("Found a config file: "+file);
			AVConfig config = new AVConfig(file);
			config.loadValues();
			config.addValuesToTable();
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
}
