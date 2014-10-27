package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemLightRing extends Item implements IAetherRing {
	
	private IIcon activeIcon;
	private int update = 0;
	private boolean suppressed = false;
	
	public ItemLightRing() {
		super();
		this.setUnlocalizedName("aec.lightRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemLightRing");
		this.activeIcon = iconRegister.registerIcon("aec:itemLightRingActive");
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		deactivateRing(stack,player);
		return super.onDroppedByPlayer(stack, player);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot,boolean held) {
		if (!(entity instanceof EntityPlayer)) {return;}
		EntityPlayer player = (EntityPlayer) entity;

		if (isActive(stack)) {
			int light = world.getBlockLightValue((int)player.posX, (int)(player.posY+.5), (int)player.posZ);
			if (light <= 7 && world.isAirBlock((int)player.posX, (int)(player.posY+.5), (int)player.posZ)) {
				float cost = AVRegistry.getAV(new ItemStack(AetherCraft.blockAetherFlame));
				float av = InventoryUtils.getAVInInventory(player.inventory);
				if (av>=cost) {
					InventoryUtils.drainAVFromInventory(player.inventory, cost);
					
					world.setBlock((int)player.posX, (int)(player.posY+.5), (int)player.posZ, AetherCraft.blockAetherFlame);
				} else {
					deactivateRing(stack,player);
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			return stack;
		}
		
		if (stack.getItemDamage() == 0) {
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
	}
	
	@Override
	public void deactivateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(0);
	}
	
	@Override
	public boolean isActive(ItemStack stack) {
		return stack.getItemDamage()==1;
	}
}
