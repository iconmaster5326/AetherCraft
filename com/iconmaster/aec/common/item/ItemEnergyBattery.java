package com.iconmaster.aec.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemEnergyBattery extends Item {
	public ItemEnergyBattery(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("aec.energyBattery");
        this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemEnergyBattery");
	}

	@Override
	public void onCreated(ItemStack stack, World world,
			EntityPlayer entityPlayer) {
		if(!stack.hasTagCompound()){
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("EMEV", 0);
			stack.setTagCompound(tagCompound);
		} else if(!stack.getTagCompound().hasKey("EMEV")){
			stack.getTagCompound().setInteger("EMEV", 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,
			EntityPlayer entityPlayer, List list, boolean advancedTooltip) {
		int ev = 0;
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound();
			if(tag.hasKey("EMEV")){
				ev = tag.getInteger("EMEV");
			}
		}
		list.add("\u00a72" + "EV: " + ev);
	}
}
