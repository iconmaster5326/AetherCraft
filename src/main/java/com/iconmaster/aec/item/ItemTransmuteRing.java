package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTransmuteRing extends Item {
	
	public ItemTransmuteRing() {
		super();
		this.setUnlocalizedName("aec.transmuteRing");
		this.setMaxStackSize(1);
		 //this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemTransmuteRing");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par1, float par2, float par3, float par4) {
		player.openGui(AetherCraft.instance, CommonProxy.RING_TRANSMUTE_GUI_ID, world, x, y, z);
		return true;
	}
}