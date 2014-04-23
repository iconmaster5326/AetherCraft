package com.iconmaster.aec.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlyingRing extends Item implements IAetherRing {
	
	private Icon activeIcon;
	private boolean suppressed = false;
	
	public ItemFlyingRing(int id) {
		super(id);
		this.setUnlocalizedName("aec.flyingRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
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
		if (entity instanceof EntityPlayer && (!((EntityPlayer)entity).inventory.hasItem(AetherCraft.itemFlyingRing.itemID) || !canRingFunction(stack,(EntityPlayer)entity))) {
			deactivateRing(stack,(EntityPlayer)entity);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack.getItemDamage() == 0) {
			activateRing(stack,player);
		} else {
			deactivateRing(stack,player);
		}
		return stack;
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return meta==0 ? itemIcon : activeIcon;
	}
	
	@Override
	public void activateRing(ItemStack stack,EntityPlayer player) {
		if (!canRingFunction(stack,player)) {return;}
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