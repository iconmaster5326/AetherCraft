package com.iconmaster.aec.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AetherSwordEvent {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void AttackEventHandler(AttackEntityEvent event) {
		ItemStack hand = event.entityPlayer.getCurrentEquippedItem();
		if (hand == null || hand.getItem()!=AetherCraft.aetherSword) {
			return;
		}
		NBTTagCompound tag = hand.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		if (tag.getBoolean("active") && event.target instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase)event.target;

			entity.setHealth(entity.getHealth()-3F);
			//entity.hurtResistantTime = 0;	//Hackish workaround due to attribute system
			//entity.attackEntityFrom(DamageSource.causeMobDamage(event.entityPlayer), 3F);
			
			InventoryUtils.drainAVFromInventory(event.entityPlayer.inventory, Float.parseFloat(AetherCraft.getOptions("toolcost")));
		}
	}
}
