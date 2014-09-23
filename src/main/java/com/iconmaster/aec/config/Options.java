package com.iconmaster.aec.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

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
		if (options.get(name) instanceof Double) {
			return ((Double)options.get(name)).floatValue();
		} else if (options.get(name) instanceof Integer) {
			return ((Integer)options.get(name)).floatValue();
		}
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
			} else if (entry.getValue() instanceof Double) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (Double) entry.getValue(),descs.get(entry.getKey())).getDouble());
			} else if (entry.getValue() instanceof Boolean) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (Boolean) entry.getValue(),descs.get(entry.getKey())).getBoolean());
			} else if (entry.getValue() instanceof String) {
				options.put(entry.getKey(), config.get("options", entry.getKey(), (String) entry.getValue(),descs.get(entry.getKey())).getString());
			}
		}
	}
	
	public void writeConfig() {
		ConfigCategory opts = config.getCategory("options");
		for (Entry<String, Object> entry : defaults.entrySet()) {
			if (entry.getValue() instanceof Integer) {
				opts.put(entry.getKey(),new Property(entry.getKey(),entry.getValue().toString(), Property.Type.INTEGER));
			} else if (entry.getValue() instanceof Float || entry.getValue() instanceof Double) {
				opts.put(entry.getKey(),new Property(entry.getKey(),entry.getValue().toString(), Property.Type.DOUBLE));
			} else if (entry.getValue() instanceof Boolean) {
				opts.put(entry.getKey(),new Property(entry.getKey(),entry.getValue().toString(), Property.Type.BOOLEAN));
			} else if (entry.getValue() instanceof String) {
				opts.put(entry.getKey(),new Property(entry.getKey(),entry.getValue().toString(), Property.Type.STRING));
			}
			opts.get(entry.getKey()).comment = descs.get(entry.getKey());
		}
	}
	
	public void saveConfig() {
		config.save();
	}
	
	public void setOptionsMap(HashMap<String,Object> options) {
		this.options = options;
	}
	
	public HashMap<String,Object> getOptionsMap() {
		return  options;
	}

	public void addDefaultOptions() {
		addOption("consumeprecision","Values below 100 mean that AV conversion is that much inefficent.",100);
		addOption("avmultiplier","Ramp up AVs globally with this multiplier.",1.0);
		addOption("ammaxstorage","How much aether an Aether Manipulator (and other machines) can store.",32768);
		addOption("acmaxstorage","How much aether an Aether Container can store.",65536);
		addOption("abatterymaxstorage","How much aether an Aether Cell can store.",16384);
		addOption("flycostpersecond","How much AV the flying ring will drain per second.",8);
		addOption("chargerate","How fast Aether Cells placed in an Aether Container charge.",128);
		addOption("flowrate","How much AV a basic Conduit can transfer maximum.",8192);
		addOption("toolcost","How much AV it costs for a basic Aether tool to break a block.",2.0);
		addOption("avlimit","The transmutation limit for Tier 1 machines. Multiplies by 4 for each tier.",8192.0);
		addOption("excesspull","The multiple of which machines try to pull AV from. EX: A value of 16 means machines pull in 16x the needed AV when they need to.",16.0);
		addOption("armorcost","The amount of AV Aether Armor draws to prevent 1/2 heart of damage.",128.0);
		addOption("regnerationid","A potion ID. Don't think about it too much.",25);
		addOption("ticksperop","Machines will take X ticks to process X items. X is this value.",1);
		addOption("telecost","The amount of AV the Ring of Teleportation uses to move you 1 block.",4.0);
		addOption("instantconsume","Whether stacks will get transmuted/consumed instantly or not.",false);
		addOption("enableflyring","If false, there will be no Flying Ring.",true);
		addOption("showavalways","If true, the AV of objects will be shown even if the user is not holding shift.",false);
		addOption("cobblehack","If true, you can't consume cobble for AV. A hackish fix!",false);

		//addOption("","",0);
	}

	public HashMap<String,String> getDescs() {
		return descs;
	}
}
