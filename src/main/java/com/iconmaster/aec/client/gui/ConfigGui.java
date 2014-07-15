package com.iconmaster.aec.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigGui implements IModGuiFactory {

	public static class ConfigGuiScreen extends GuiConfig {
		public ConfigGuiScreen(GuiScreen parent) {
			super(parent, getConfigElements(), "AetherCraft", false, false, "AetherCraft Options");
		}

		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			
			list.add(new DummyConfigElement<Double>("test", 12.1, ConfigGuiType.DOUBLE, "Description here"));
			
			return list;
		}
	}

    private Minecraft minecraft;
    @Override
    public void initialize(Minecraft minecraftInstance) {
        this.minecraft = minecraftInstance;
    }

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ConfigGuiScreen.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	   @Override
	    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
	        return new RuntimeOptionGuiHandler() {
	            @Override
	            public void paint(int x, int y, int w, int h) {
	            	
	            }

	            @Override
	            public void close() {
	            	
	            }

	            @Override
	            public void addWidgets(List<Gui> widgets, int x, int y, int w, int h) {
	                
	            }

	            @Override
	            public void actionCallback(int actionId) {
	            	
	            }
	        };
	    }
	
	
}
