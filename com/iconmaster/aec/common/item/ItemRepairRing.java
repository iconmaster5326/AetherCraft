package com.iconmaster.aec.common.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRepairRing extends Item {
	
	private Icon activeIcon;
	private int update = 0;
	
	public ItemRepairRing(int id) {
		super(id);
		this.setUnlocalizedName("aec.repairRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemRepairRing");
		this.activeIcon = iconRegister.registerIcon("aec:itemRepairRingActive");
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		System.out.println("RING WAS DROPPED LOCALLY");
		deactivateRing(stack,player);
		return super.onDroppedByPlayer(stack, player);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot,boolean held) {
		update++;
		if (update%16!=0) {
			return;
		} //Only proc occasionally
		if (!(entity instanceof EntityPlayer) || stack.getItemDamage()==0) {
			return;
		}
		if (entity instanceof EntityPlayer && (!((EntityPlayer)entity).inventory.hasItem(this.itemID))) {
			deactivateRing(stack,(EntityPlayer)entity);
			return;
		}
		InventoryPlayer inv = ((EntityPlayer)entity).inventory;
		for (int i=0;i<inv.getSizeInventory();i++) {
			ItemStack tool = inv.getStackInSlot(i);
			if (repairItem(stack,entity,inv,tool)) {
				return;
			}
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
	
	public void activateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(1);

	}
	
	public void deactivateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(0);

		
	}
	
	public boolean repairItem(ItemStack stack,Entity entity,InventoryPlayer inv,ItemStack tool) {
		if (tool != null && tool.isItemDamaged()) {
			float av = AVRegistry.getAbsoluteAV(tool)/tool.getMaxDamage();
			if (av<=0) {return false;}
			if (InventoryUtils.getAVInInventory(inv)>=av) {
				float got = InventoryUtils.drainAVFromInventory(inv, av);
				tool.setItemDamage(tool.getItemDamage()-1);
				return true;
			} else {
				deactivateRing(stack,(EntityPlayer)entity);
				return true;
			}
		}
		return false;
	}
}