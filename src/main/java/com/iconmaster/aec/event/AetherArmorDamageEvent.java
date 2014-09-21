package com.iconmaster.aec.event;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.item.ItemAetherArmor;
import com.iconmaster.aec.item.ItemPhasingRing;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AetherArmorDamageEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventHandler(LivingHurtEvent event) {

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			
			//handle Ring of Phasing
			for (int i=1;i<player.inventory.getSizeInventory();i++) {
				ItemStack slot = player.inventory.getStackInSlot(i);
				if (slot!=null && slot.getItem() instanceof ItemPhasingRing && slot.getItemDamage()==1){
					if (Math.random()<.5) {
						event.ammount = 0;
						event.setCanceled(true);
					}
					break;
				}
			}
			
			//handle Aether Armor
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
