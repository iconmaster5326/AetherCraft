package com.iconmaster.aec.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.IAetherContainingItem;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAetherBattery extends Item implements IAetherContainingItem {
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,
			EntityPlayer entityPlayer, List list, boolean advancedTooltip) {
		float ev = 0;
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound();
			if(tag.hasKey("AV")){
				ev = tag.getFloat("AV");
			}
		}
		list.add("\u00a72" + "AV: " + NumberUtils.display(ev));
	}

	@Override
	public float addAether(ItemStack stack, float av) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		float max = Float.parseFloat(AetherCraft.getOptions("abatterymaxstorage"));
		float has = stack.getTagCompound().getFloat("AV");
		if (has + av > max) {
			this.setAether(stack,max);
			return (has+av)-max;
		}
		this.setAether(stack,has+av);
		return 0;
	}

	@Override
	public float extractAether(ItemStack stack, float av) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		float has = stack.getTagCompound().getFloat("AV");
		if (has - av < 0) {
			this.setAether(stack,0);
			return has;
		}
		this.setAether(stack,has-av);
		return av;
	}

	@Override
	public void setAether(ItemStack stack, float av) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setFloat("AV", av);
	}

	@Override
	public float getAether(ItemStack stack) {
		if (!stack.hasTagCompound()) {return 0;}
		return stack.getTagCompound().getFloat("AV");
	}
}
