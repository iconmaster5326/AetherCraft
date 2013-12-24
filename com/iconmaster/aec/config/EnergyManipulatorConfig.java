package com.iconmaster.aec.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EnergyManipulatorConfig {
	private File file;

	public EnergyManipulatorConfig(File configFile) {
		this.file = configFile;
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getAllEnergyValues(Map<String, Integer> ev) {
		String line = null;
		String[] sbuf;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(this.file));
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#") && line.contains("=")) {
					sbuf = line.split("=");
					ev.put(sbuf[0], Integer.parseInt(sbuf[1]));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveEnergyValues(Map<String, Integer> currentConfigEV) {
		StringBuilder sb = new StringBuilder();

		Set mapSet = (Set) currentConfigEV.entrySet();
		Iterator mapIterator = mapSet.iterator();
		while (mapIterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) mapIterator.next();
			String keyValue = (String) mapEntry.getKey();
			int value = (Integer) mapEntry.getValue();
			sb.append(keyValue + "=" + Integer.toString(value) + System.getProperty("line.separator"));
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