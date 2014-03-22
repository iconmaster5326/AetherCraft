package com.iconmaster.aec.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;
import com.iconmaster.aec.util.SideUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAetherHammer extends ItemPickaxe {
	public static Icon activeIcon;
	
	public ItemAetherHammer(int id) {
		super(id,AetherCraft.aetherMaterial);
        this.setUnlocalizedName("aec.aetherHammer");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        
        // Add additional effectiveness ratings
        for (Block block : ItemPickaxe.blocksEffectiveAgainst) {
        	MinecraftForge.setBlockHarvestLevel(block, "aecHammer", 0);
        }
        for (Block block : ItemSpade.blocksEffectiveAgainst) {
        	MinecraftForge.setBlockHarvestLevel(block, "aecHammer", 0);
        }
        
        MinecraftForge.setToolClass(this, "aecHammer", 3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemAetherHammer");
		this.activeIcon = iconRegister.registerIcon("aec:itemAetherHammerActive");
	}
	
	@Override
    public Icon getIcon(ItemStack stack, int pass)
    {
		boolean active = false;
		if (stack.hasTagCompound()) {
			active = stack.getTagCompound().getBoolean("active");
		}
		return (!active) ? itemIcon : activeIcon;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconIndex(ItemStack stack)
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
    public float getStrVsBlock(ItemStack stack, Block block, int meta)
    {
        float str = super.getStrVsBlock(stack, block, meta);
        
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
			return false;
		}
		
		NBTTagCompound tag;
		if (!stack.hasTagCompound()) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		} else {
			tag = stack.getTagCompound();
		}
		boolean active = tag.getBoolean("active");
		
		if (active) {
				InventoryUtils.drainAVFromInventory(player.inventory, Float.parseFloat(AetherCraft.getOptions("toolcost"))*9F);
		}
		
		//destroy surrounding blocks
		
		int side = SideUtils.getBlockHitSide(player.worldObj, player, 8D);
		int axis1 = -1,axis2 = -1;
		switch (side) {
		case (0):
		case (1):
			axis1 = 4;
			axis2 = 2;
			break;
		case (2):
		case (3):
			axis1 = 4;
			axis2 = 0;
			break;
		case (4):
		case (5):
			axis1 = 2;
			axis2 = 0;
			break;
		}
		
		SideUtils.Offset offset1 = new SideUtils.Offset(axis1);
		SideUtils.Offset offset2 = new SideUtils.Offset(axis2);
		
		SideUtils.Offset endOffset1 = new SideUtils.Offset(SideUtils.getOppositeSide(axis1));
		SideUtils.Offset endOffset2 = new SideUtils.Offset(SideUtils.getOppositeSide(axis2));
		
		int minx = Math.min(offset1.getOffsetX(xCoord),offset2.getOffsetX(xCoord));
		int miny = Math.min(offset1.getOffsetY(yCoord),offset2.getOffsetY(yCoord));
		int minz = Math.min(offset1.getOffsetZ(zCoord),offset2.getOffsetZ(zCoord));
		
		int maxx = Math.max(endOffset1.getOffsetX(xCoord),endOffset2.getOffsetX(xCoord));
		int maxy = Math.max(endOffset1.getOffsetY(yCoord),endOffset2.getOffsetY(yCoord));
		int maxz = Math.max(endOffset1.getOffsetZ(zCoord),endOffset2.getOffsetZ(zCoord));
		
		for (int x=minx;x<=maxx;x++) {
			for (int y=miny;y<=maxy;y++) {
				for (int z=minz;z<=maxz;z++) {
					Block subject = Block.blocksList[player.worldObj.getBlockId(x, y, z)];
					if (!(x==xCoord && y==yCoord && z==zCoord) && subject != null && ForgeHooks.isToolEffective(stack, subject, player.worldObj.getBlockMetadata(x, y, z))) {
						//harvest the block
						
						
                        //subject.harvestBlock(player.worldObj, player, x, y, z, player.worldObj.getBlockMetadata(x, y, z));
                        //subject.onBlockHarvested(player.worldObj, x, y, z, player.worldObj.getBlockMetadata(x, y, z), player);
                        
                        player.worldObj.destroyBlock(x, y, z, true);
					}
				}
			}
		}
		
		return false;
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
	
	@Override
	 public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		if (Block.blocksList[par3] == null) {return true;}
		return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLivingBase);
	 }
}
