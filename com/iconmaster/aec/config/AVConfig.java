package com.iconmaster.aec.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.util.UidUtils;

public class AVConfig {
	private File file;
	private HashMap values;
	private HashMap valueStrings;
	
	protected AVConfig() {}
	
	public AVConfig(File file) {
		this.file = file;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.values = new HashMap();
		this.valueStrings = new HashMap();
	}
	
	public File getFile() {
		return file;
	}
	
	public void loadValues() {
		String line = null;
		String[] sbuf;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(this.file));
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#") && line.contains("=")) {
					sbuf = line.split("=");
					addValue(sbuf[0], Float.parseFloat(sbuf[1]));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveValues() {
		StringBuilder sb = new StringBuilder();

		Set mapSet = (Set) values.entrySet();
		Iterator mapIterator = mapSet.iterator();
		while (mapIterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) mapIterator.next();
			ItemStack keyValue = UidUtils.getStackFromUid((List) mapEntry.getKey());
			
			if (keyValue != null) {
				//System.out.println("Saving to file. Name is "+keyValue);
				float value = (Float) mapEntry.getValue();
				String name= keyValue.getUnlocalizedName();
				if (AVRegistry.getItemFromString(name)==null) {
					name = Item.itemsList[keyValue.itemID].getUnlocalizedName();
					if (name!=null) {
						name += "::"+keyValue.getItemDamage();
					}
					if (name == null || AVRegistry.getItemFromString(name)==null) {
						name = ""+keyValue.itemID+"::"+keyValue.getItemDamage();
					}
				}
				if (AVRegistry.getItemFromString(name).getHasSubtypes()) {
					name+="::"+keyValue.getItemDamage();
				}
				sb.append(name + "=" + Float.toString(value) + System.getProperty("line.separator"));
			} else {
				//System.out.println("Not Saving to file. key is"+mapEntry.getKey());
			}
		}
		try {
			file.delete();
			file.createNewFile();
			PrintWriter pw = new PrintWriter(this.file);
			pw.print(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addValue(String s, float av) {
		valueStrings.put(s,av);
		addValue(AVRegistry.getItemFromString(s),av);
	}
	
	public void addValue(ItemStack item, float av) {
		values.put(UidUtils.getUID(item),av);
	}
	
	public float getValue(ItemStack item) {
		if (item == null) {return 0;}
		return (Float) values.get(UidUtils.getUID(item));
	}
	
	public boolean isEntry(ItemStack item) {
		return values.get(UidUtils.getUID(item)) != null;
	}
	
	public void addValuesToTable() {
		Iterator it = values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			AVRegistry.setConfigAV(UidUtils.getStackFromUid((List)entry.getKey()), (Float)entry.getValue());
		}
	}
	
	public void deleteValue(ItemStack item) {
		values.remove(UidUtils.getUID(item));
	}

	public HashMap getValueMap() {
		return values;
	}
	
	public HashMap getValueStrings() {
		return valueStrings;
	}
}
