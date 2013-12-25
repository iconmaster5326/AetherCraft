package com.iconmaster.aec.common.item;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.AecInvUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFlyingRing extends Item {
	public ItemFlyingRing(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("aec.flyingRing");
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister
				.registerIcon("aec:itemFlyingRing");
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound stackTag = new NBTTagCompound();
		if (!stack.hasTagCompound()) {
			stackTag.setByte("EMUpdateTick", (byte) 0);
			stack.setTagCompound(stackTag);
		} else {
			stackTag = stack.getTagCompound();
			stackTag.setByte("EMUpdateTick", (byte) 0);
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		if (!player.inventory.hasItem(AetherCraft.itemFlyingRing.itemID)
				&& !player.capabilities.isCreativeMode) {
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		}
		return super.onDroppedByPlayer(stack, player);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot,
			boolean held) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!player.capabilities.isCreativeMode) {
				if (player.inventory
						.hasItem(AetherCraft.itemFlyingRing.itemID)) {
					if (player.capabilities.isFlying) {
						if (!stack.hasTagCompound()) {
							NBTTagCompound stackTag = new NBTTagCompound();
							stackTag.setByte("EMUpdateTick", (byte) 0);
							stack.setTagCompound(stackTag);
						}
						NBTTagCompound stackTag = stack.getTagCompound();
						byte tick = stackTag.getByte("EMUpdateTick");
						if (tick >= 20) {
							stackTag.setByte("EMUpdateTick", (byte) 0);
							int flySecondCost = Integer
									.parseInt(AetherCraft
											.getOptions("flycostpersecond"));

							ItemStack[] batteries = AecInvUtils.getAllISInArray(
									player.inventory.mainInventory,
									AetherCraft.itemAetherBattery.itemID);
							ItemStack[] batteriesAboveMinimum = AecInvUtils
									.getAllBatteriesWithEnergyAboveOrEqual(
											batteries, flySecondCost);
							ItemStack[] batteriesAboveZero = AecInvUtils
									.getAllBatteriesWithEnergyAboveOrEqual(
											batteries, 1);
							if (batteriesAboveMinimum != null
									&& batteriesAboveMinimum.length > 0) {
								NBTTagCompound tag = batteriesAboveMinimum[0]
										.getTagCompound();
								float batteryEv = tag.getFloat("EMAV");
								tag.setFloat("EMAV", batteryEv
										- flySecondCost);
								player.capabilities.allowFlying = true;
								return;
							} else if (batteriesAboveZero != null
									&& batteriesAboveZero.length > 0) {
								int energyNeeded = flySecondCost;
								int i = 0;
								while (energyNeeded > 0
										&& i < batteriesAboveZero.length) {
									NBTTagCompound tag = batteriesAboveZero[i]
											.getTagCompound();
									float batteryEv = tag.getFloat("EMAV");
									energyNeeded -= batteryEv;
									tag.setFloat("EMAV", 0);
									i++;
								}
								if (energyNeeded > 0) {
									player.capabilities.allowFlying = false;
									player.capabilities.isFlying = false;
									return;
								} else {
									player.capabilities.allowFlying = true;
									return;
								}
							} else {
								player.capabilities.allowFlying = false;
								player.capabilities.isFlying = false;
								return;
							}
						}

						// UPDATE TICKS
						else {
							stackTag.setByte("EMUpdateTick", (byte) (tick + 1));
						}
					} else {
						ItemStack[] batteries = AecInvUtils.getAllISInArray(
								player.inventory.mainInventory,
								AetherCraft.itemAetherBattery.itemID);
						ItemStack[] batteriesAboveZero = AecInvUtils
								.getAllBatteriesWithEnergyAboveOrEqual(
										batteries, 1);

						if (batteriesAboveZero.length > 0) {
							player.capabilities.allowFlying = true;
						}
					}
				} else {
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
				}
			}
		}
	}
}