package com.iconmaster.aec.common.handler.tick;

import java.util.EnumSet;

import com.iconmaster.aec.common.AetherCraft;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class FlyingRingTickHandler implements IScheduledTickHandler {
	private void tickPlayer(EnumSet<TickType> type, EntityPlayer player) {
		if (!player.inventory.hasItem(AetherCraft.itemFlyingRing.itemID) && !player.capabilities.isCreativeMode) {
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.PLAYER))) {
			this.tickPlayer(type, (EntityPlayer) tickData[0]);
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nextTickSpacing() {
		return 20;
	}
}
