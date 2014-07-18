package com.iconmaster.aec.item;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.util.NumberUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAetometer extends Item {
	
	public ItemAetometer() {
		super();
		this.setUnlocalizedName("aec.aetometer");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemAetometer");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par1, float par2, float par3, float par4) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return true;
		}
		
		Block block = world.getBlock(x, y, z);
		TileEntity te = world.getTileEntity(x, y, z);
		
		boolean showNetwork = true;
		float deviceAV = 0;
		if (te!=null && te instanceof IAetherStorage) {
			ArrayList a = AetherNetwork.getAllConnectedDevices(world, x, y, z);
			if (a.size()==0) {
				showNetwork = false;
			}
			deviceAV = ((IAetherStorage)te).getAether();
			
			ChatComponentText cmc = new ChatComponentText("Aether in device: "+NumberUtils.display(deviceAV));
			player.addChatMessage(cmc);
		}
		
		if (block!=null && block instanceof IAetherTransfer && showNetwork) {
			float av = AetherNetwork.getStoredAV(world, x, y, z);
			
			ChatComponentText cmc = new ChatComponentText("Aether in network: "+NumberUtils.display(deviceAV+av));
			player.addChatMessage(cmc);
		}
		
		if (te!=null && te instanceof IInventory) {
			IInventory inv = (IInventory)te;
			float sum=0;
			for (int i=0;i<inv.getSizeInventory();i++) {
				ItemStack slot = inv.getStackInSlot(i);
				if (slot!=null) {
					if (slot.getItem() instanceof IProduceBehavior) {
						sum += ((IProduceBehavior)slot.getItem()).getProduceAV(slot);
					} else {
						sum += AVRegistry.getAV(slot)*slot.stackSize;
					}
				}
			}
			
			ChatComponentText cmc = new ChatComponentText("Aether stored in items: "+NumberUtils.display(sum));
			player.addChatMessage(cmc);
		}
		
		return true;
	}
}