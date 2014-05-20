package com.iconmaster.aec.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlyingRing extends Item implements IAetherRing {
	
	private IIcon activeIcon;
	private boolean suppressed = false;
	
	public ItemFlyingRing() {
		super();
		this.setUnlocalizedName("aec.flyingRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemFlyingRing");
		this.activeIcon = iconRegister.registerIcon("aec:itemFlyingRingActive");
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		System.out.println("RING WAS DROPPED LOCALLY");
		deactivateRing(stack,player);
		return super.onDroppedByPlayer(stack, player);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot,
			boolean held) {
		if (entity instanceof EntityPlayer && (!((EntityPlayer)entity).inventory.hasItem(AetherCraft.itemFlyingRing) || !canRingFunction(stack,(EntityPlayer)entity))) {
			deactivateRing(stack,(EntityPlayer)entity);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() == 0) {
			if (!canRingFunction(stack,player)) {return stack;}
			activateRing(stack,player);
		} else {
			deactivateRing(stack,player);
		}
		return stack;
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return meta==0 ? itemIcon : activeIcon;
	}
	
	@Override
	public void activateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(1);
		player.capabilities.allowFlying = true;
		player.capabilities.isFlying = true;
		
		player.sendPlayerAbilities();
	}
	
	@Override
	public void deactivateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(0);
		player.capabilities.allowFlying = false;
		player.capabilities.isFlying = false;
		
		player.sendPlayerAbilities();
	}
	
	public boolean canRingFunction(ItemStack stack,EntityPlayer player) {
		if (stack.getItemDamage()==1) {
			float flyCost = Float.parseFloat(AetherCraft.getOptions("flycostpersecond"))/20;
			if (!player.capabilities.isFlying) { flyCost /= 4;}
			float rest = InventoryUtils.drainAVFromInventory(player.inventory, flyCost);
			
			if (rest < flyCost) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isActive(ItemStack stack) {
		return stack.getItemDamage()==1;
	}
}