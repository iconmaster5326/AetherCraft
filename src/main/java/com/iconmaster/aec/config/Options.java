package com.iconmaster.aec.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraftforge.common.config.Configuration;

import com.iconmaster.aec.AetherCraft;

public class Options {
	private HashMap<String,Object> options = new HashMap<String, Object>();
	private HashMap<String,Object> defaults = new HashMap<String, Object>();
	private HashMap<String,String> descs = new HashMap<String, String>();
	Configuration config;
	
	public Options() {
		
	}
	
	public void addOption(String name, String desc, Object defaultValue) {
		defaults.put(name, defaultValue);
		descs.put(name, desc);
	}
	
	public int getInt(String name) {
		return (Integer) options.get(name);
	}
	
	public float getFloat(String name) {
		return (Float) options.get(name);
	}
	
	public boolean getBoolean(String name) {
		return (Boolean) options.get(name);
	}
	
	public String getString(String name) {
		return (String) options.get(name);
	}
	
	public void loadConfig(File forgeConfigFile) {
		config = new Configuration(forgeConfigFile);
		config.load();
	}
	
	public void readConfig() {
		for (Entry<String, Object> entry : defaults.entrySet()) {
			if (entry.getValue() instanceof Integer) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (Integer) entry.getValue(),descs.get(entry.getKey())).getInt());
			} else if (entry.getValue() instanceof Float) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (Float) entry.getValue(),descs.get(entry.getKey())).getDouble());
			} else if (entry.getValue() instanceof Boolean) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (Boolean) entry.getValue(),descs.get(entry.getKey())).getBoolean());
			} else if (entry.getValue() instanceof String) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (String) entry.getValue(),descs.get(entry.getKey())).getString());
			}
		}
	}
	
	public void writeConfig() {
		
	}
	
	public void saveConfig() {
		config.save();
	}
	
	public void setOptionsMap(HashMap options) {
		this.options = options;
	}
	
	public HashMap getOptionsMap() {
		return  options;
	}
	
}
