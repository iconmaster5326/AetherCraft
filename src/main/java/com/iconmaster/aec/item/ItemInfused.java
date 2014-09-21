package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemInfused extends Item {
	public IIcon[] icons = new IIcon[11];
	public ItemInfused() {
		super();
        this.setUnlocalizedName("aec.infused");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icons[0] = iconRegister.registerIcon("aec:itemInfusedIngot");
		this.icons[1] = iconRegister.registerIcon("aec:itemAetheralFoci");
		this.icons[2] = iconRegister.registerIcon("aec:itemInfusedRod");
		this.icons[3] = iconRegister.registerIcon("aec:itemWroughtBand");
		this.icons[4] = iconRegister.registerIcon("aec:itemGildedIngot");
		this.icons[5] = iconRegister.registerIcon("aec:itemGoldMatter");
		this.icons[6] = iconRegister.registerIcon("aec:itemSingularity");
		this.icons[7] = iconRegister.registerIcon("aec:itemPurePower");
		this.icons[8] = iconRegister.registerIcon("aec:itemEternalIngot");
		this.icons[9] = iconRegister.registerIcon("aec:itemPureCrystal");
		this.icons[10] = iconRegister.registerIcon("aec:itemCompressedSingularity");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void getSubItems(Item par1,CreativeTabs tab,List list) {
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
		list.add(new ItemStack(this,1,3));
		list.add(new ItemStack(this,1,4));
		list.add(new ItemStack(this,1,5));
		list.add(new ItemStack(this,1,6));
		list.add(new ItemStack(this,1,7));
		list.add(new ItemStack(this,1,8));
		list.add(new ItemStack(this,1,9));
		list.add(new ItemStack(this,1,10));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "aec.infused."+stack.getItemDamage();
	}
}
