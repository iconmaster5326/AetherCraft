package com.iconmaster.aec.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockInfused extends ItemBlock {
	public ItemBlockInfused(int id) {
		super(id);
        this.setUnlocalizedName("aec.infusedBlock");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "aec.infused.block."+stack.getItemDamage();
	}
	
	@Override
	public void getSubItems(int par1,CreativeTabs tab,List list) {
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
}
