package com.iconmaster.aec.event;

import com.iconmaster.aec.AetherCraft;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by iconmaster on 9/21/2014.
 */
public class ConfigUpdateEvent {
	@SubscribeEvent()
	public void onSave(ConfigChangedEvent event) {
		if (event.modID == AetherCraft.MODID) {

		}
	}
}
