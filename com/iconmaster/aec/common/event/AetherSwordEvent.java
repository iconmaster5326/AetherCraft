package com.iconmaster.aec.common.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;

public class AetherSwordEvent {
	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void AttackEventHandler(AttackEntityEvent event) {
		ItemStack hand = event.entityPlayer.getCurrentEquippedItem();
		if (hand == null || hand.itemID!=AetherCraft.aetherSword.itemID) {
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
