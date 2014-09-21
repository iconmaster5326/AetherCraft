package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemAetherShears extends ItemShears {
	public static IIcon activeIcon;
	
	public ItemAetherShears() {
		super();
        this.setUnlocalizedName("aec.aetherShears");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        
        this.setMaxDamage(400);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemAetherShears");
		this.activeIcon = iconRegister.registerIcon("aec:itemAetherShearsActive");
	}
	
	@Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
		boolean active = false;
		if (stack.hasTagCompound()) {
			active = stack.getTagCompound().getBoolean("active");
		}
		return (!active) ? itemIcon : activeIcon;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
		boolean active = false;
		if (stack.hasTagCompound()) {
			active = stack.getTagCompound().getBoolean("active");
		}
		return (!active) ? itemIcon : activeIcon;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
//		if (!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
//		{
//			return stack;
//		}
		NBTTagCompound tag;
		if (!stack.hasTagCompound()) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		} else {
			tag = stack.getTagCompound();
		}
		boolean active = tag.getBoolean("active");
		tag.setBoolean("active",!active);
		return stack;
	}
	
	@Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        float str = super.getDigSpeed(stack, block, meta);
        
		boolean active = false;
		if (stack.hasTagCompound()) {
			active = stack.getTagCompound().getBoolean("active");
		}
		
		str = str * .8F;
		if (active) {
			str = str * 2.5F;
		}
		
		return str;
    }
	
	@Override
    public boolean onBlockStartBreak(ItemStack stack, int xCoord, int yCoord, int zCoord, EntityPlayer player) {
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ) {
			return super.onBlockStartBreak(stack, xCoord, yCoord, zCoord, player);
		}
		
		NBTTagCompound tag;
		if (!stack.hasTagCompound()) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		} else {
			tag = stack.getTagCompound();
		}
		boolean active = tag.getBoolean("active");
		
		return super.onBlockStartBreak(stack, xCoord, yCoord, zCoord, player);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean held) {
		if (!(entity  instanceof EntityPlayer)) {
			return;
		}
		float av = InventoryUtils.getAVInInventory(((EntityPlayer)entity).inventory);
		if (av < Float.parseFloat(AetherCraft.getOptions("toolcost"))) {
			NBTTagCompound tag;
			if (!stack.hasTagCompound()) {
				tag = new NBTTagCompound();
				stack.setTagCompound(tag);
			} else {
				tag = stack.getTagCompound();
			}
			tag.setBoolean("active",false);
		}
		
		return;
    }

}
