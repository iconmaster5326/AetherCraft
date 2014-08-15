package com.iconmaster.aec.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.TeleportRingPacket;
import com.iconmaster.aec.util.InventoryUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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