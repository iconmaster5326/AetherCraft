package com.iconmaster.aec.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.AetherCraft;

public class ItemBlockInfused extends ItemBlock {
	public ItemBlockInfused(Block block) {
		super(block);
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
	public void getSubItems(Item par1,CreativeTabs tab,List list) {
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
		list.add(new ItemStack(this,1,3));
		list.add(new ItemStack(this,1,4));
		list.add(new ItemStack(this,1,5));
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
}
