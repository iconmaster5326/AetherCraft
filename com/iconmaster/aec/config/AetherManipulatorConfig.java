package com.iconmaster.aec.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.iconmaster.aec.aether.AVRegistry;

public class AetherManipulatorConfig {
	private File file;

	public AetherManipulatorConfig(File configFile) {
		this.file = configFile;
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getAllAetherValues() {
		String line = null;
		String[] sbuf;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(this.file));
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#") && line.contains("=")) {
					sbuf = line.split("=");
					AVRegistry.setConfigAV(sbuf[0], Float.parseFloat(sbuf[1]));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveAetherValues(HashMap<String, Float> currentConfigAV) {
		StringBuilder sb = new StringBuilder();

		Set mapSet = (Set) currentConfigAV.entrySet();
		Iterator mapIterator = mapSet.iterator();
		while (mapIterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) mapIterator.next();
			String keyValue = (String) mapEntry.getKey();
			float value = (Float) mapEntry.getValue();
			sb.append(keyValue + "=" + Float.toString(value) + System.getProperty("line.separator"));
		}
		try {
			this.file.delete();
			this.file.createNewFile();
			PrintWriter pw = new PrintWriter(this.file);
			pw.print(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}