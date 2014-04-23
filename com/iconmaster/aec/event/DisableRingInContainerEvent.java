package com.iconmaster.aec.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import com.iconmaster.aec.item.IAetherRing;

public class DisableRingInContainerEvent {
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void eventHandler(PlayerOpenContainerEvent event) {
		EntityPlayer player = event.entityPlayer;
		
		if (player.openContainer instanceof ContainerPlayer) {return;}
		
		for (int i=0;i<player.inventory.getSizeInventory();i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IAetherRing) {
					if (((IAetherRing)stack.getItem()).isActive(stack)) {
						((IAetherRing)stack.getItem()).deactivateRing(stack, player);
					}
			}
		}
	}
}
