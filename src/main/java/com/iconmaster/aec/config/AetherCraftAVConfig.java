package com.iconmaster.aec.config;

import java.io.File;

public class AetherCraftAVConfig extends AVConfig {
	public AetherCraftAVConfig(File file) {
		super(file);
	}
	
	public void setupDefault() {
		addValue("AetherCraft:aetherFlame",16.0F);
	}
}
