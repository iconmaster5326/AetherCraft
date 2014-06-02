//package com.iconmaster.aec.nei;
//
//import codechicken.nei.api.IConfigureNEI;
//import codechicken.nei.guihook.GuiContainerManager;
//
//import com.iconmaster.aec.AetherCraft;
//
//public class NEIAetherCraftConfig implements IConfigureNEI {
//	private static boolean isNeiLoaded = false;
//
//	@Override
//	public void loadConfig() {
//		isNeiLoaded = true;
//		GuiContainerManager.addTooltipHandler(new TooltipHandler());
//	}
//
//	@Override
//	public String getName() {
//		return "AetherCraft";
//	}
//
//	@Override
//	public String getVersion() {
//		return AetherCraft.VERSION;
//	}
//
//	public static boolean isNeiLoaded() {
//		return isNeiLoaded;
//	}
//}
