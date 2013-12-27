package com.iconmaster.aec.common.item;

import java.util.List;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.NumberUtils;

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

public class ItemAetherBattery extends Item {
	public ItemAetherBattery(int id) {
		super(id);
        this.setUnlocalizedName("aec.aetherBattery");
        this.setMaxStackSize(1);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemAetherBattery");
	}

	@Override
	public void onCreated(ItemStack stack, World world,
			EntityPlayer entityPlayer) {
		if(!stack.hasTagCompound()){
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setFloat("EMAV", 0);
			stack.setTagCompound(tagCompound);
		} else if(!stack.getTagCompound().hasKey("EMAV")){
			stack.getTagCompound().setFloat("EMAV", 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,
			EntityPlayer entityPlayer, List list, boolean advancedTooltip) {
		float ev = 0;
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound();
			if(tag.hasKey("EMAV")){
				ev = tag.getFloat("EMAV");
			}
		}
		list.add("\u00a72" + "AV: " + NumberUtils.display(ev));
	}
}
