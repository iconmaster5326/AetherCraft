package com.iconmaster.aec.event;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.config.AVConfigHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

/**
* Created by iconmaster on 9/21/2014.
*/
public class SSPLogInEvent {
	@SubscribeEvent()
	public void onEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		if (event.isLocal) {
			AetherCraft.options.readConfig();
			AetherCraft.options.writeConfig();
			AetherCraft.options.saveConfig();

			AVConfigHandler.loadAllConfigFiles();
		}
	}
}
