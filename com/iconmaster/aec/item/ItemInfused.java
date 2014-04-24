package com.iconmaster.aec.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInfused extends Item {
	public Icon[] icons = new Icon[6];
	public ItemInfused(int id) {
		super(id);
        this.setUnlocalizedName("aec.infused");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.icons[0] = iconRegister.registerIcon("aec:itemInfusedIngot");
		this.icons[1] = iconRegister.registerIcon("aec:itemAetheralFoci");
		this.icons[2] = iconRegister.registerIcon("aec:itemInfusedRod");
		this.icons[3] = iconRegister.registerIcon("aec:itemWroughtBand");
		this.icons[4] = iconRegister.registerIcon("aec:itemGildedIngot");
		this.icons[5] = iconRegister.registerIcon("aec:itemGoldMatter");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void getSubItems(int par1,CreativeTabs tab,List list) {
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
		list.add(new ItemStack(this,1,3));
		list.add(new ItemStack(this,1,4));
		list.add(new ItemStack(this,1,5));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "aec.infused."+stack.getItemDamage();
	}
}
