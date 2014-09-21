package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;
import com.iconmaster.aec.util.RenderUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPhasingRing extends Item implements IAetherRing {
	
	private IIcon activeIcon;
	private int update = 0;
	private boolean suppressed = false;
	
	public ItemPhasingRing() {
		super();
		this.setUnlocalizedName("aec.phasingRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemPhasingRing");
		this.activeIcon = iconRegister.registerIcon("aec:itemPhasingRingActive");
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
		if (stack.getItemDamage()==1) {
			float av = Float.parseFloat(AetherCraft.getOptions("flycostpersecond"))/20;
			float got = InventoryUtils.drainAVFromInventory(player.inventory, av);
			if (got<av) {
				this.deactivateRing(stack, player);
			} else if (player.getActivePotionEffect(AetherCraft.regnerationPotion) == null) {
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 20, 1));
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
	public IIcon getIconFromDamage(int meta) {
		return meta==0 ? itemIcon : activeIcon;
	}
	
	@Override
	public void activateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(1);
		
		RenderUtils.setAetherShader();
	}
	
	@Override
	public void deactivateRing(ItemStack stack,EntityPlayer player) {
		stack.setItemDamage(0);
		RenderUtils.clearAetherShader();
	}
	
	@Override
	public boolean isActive(ItemStack stack) {
		return stack.getItemDamage()==1;
	}
}