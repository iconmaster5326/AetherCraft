package com.iconmaster.aec.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.iconmaster.aec.AetherCraft;

public class NEIAetherCraftConfig implements IConfigureNEI {
	private static boolean isNeiLoaded = false;

	@Override
	public void loadConfig() {
		isNeiLoaded = true;

		API.registerRecipeHandler(new NEIInfuserHandler());
		API.registerUsageHandler(new NEIInfuserHandler());
	}

	@Override
	public String getName() {
		return "AetherCraft";
	}

	@Override
	public String getVersion() {
		return AetherCraft.VERSION;
	}

	public static boolean isNeiLoaded() {
		return isNeiLoaded;
	}
}
