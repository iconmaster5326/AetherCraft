package com.iconmaster.aec.config;

import org.lwjgl.input.Keyboard;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.nei.TooltipHandler;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;

public class NEIEnergyManipulatorConfig implements IConfigureNEI {
	private static boolean isNeiLoaded = false;

	@Override
	public void loadConfig() {
		isNeiLoaded = true;
		GuiContainerManager.addTooltipHandler(new TooltipHandler());
	}

	@Override
	public String getName() {
		return "Energy Manipulator";
	}

	@Override
	public String getVersion() {
		return AetherCraft.VERSION;
	}

	public static boolean isNeiLoaded() {
		return isNeiLoaded;
	}
}
