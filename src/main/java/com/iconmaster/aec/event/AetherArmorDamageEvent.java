package com.iconmaster.aec.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.item.ItemAetherArmor;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AetherArmorDamageEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void LivingHurtEventHandler(LivingHurtEvent event) {

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			float reduced = 0;
			
			for (int i=1;i<=4;i++) {
				if (player.getEquipmentInSlot(i)!= null && player.getEquipmentInSlot(i).getItem() instanceof ItemAetherArmor) {
					reduced += Math.min(event.ammount-reduced, .25F);
				}
			}
			
			if (reduced > 0F) {
				float have = InventoryUtils.getAVInInventory(player.inventory);
				float av = reduced*(Float.parseFloat(AetherCraft.getOptions("armorcost")));
				//System.out.println(reduced+" damage costs "+av+" av");
				if (have >= av) {
					player.setHealth(player.getHealth()+reduced);
					InventoryUtils.drainAVFromInventory(player.inventory, av);
				}
			}
			
		}
	}
}
