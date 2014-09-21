package com.iconmaster.aec.item;

import com.google.common.collect.Sets;
import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.util.InventoryUtils;
import com.iconmaster.aec.util.SideUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Set;

public class ItemAetherHammer extends ItemPickaxe {
	//really bad hack for getting into a private field: Copy it!
	public static final Set<Block> pickGoodOn = Sets.newHashSet(new Block[] {Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail});
	public static final Set<Block> shovelGoodOn = Sets.newHashSet(new Block[] {Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium});
			
	public static IIcon activeIcon;
	
	public ItemAetherHammer() {
		super(AetherCraft.aetherMaterial);
        this.setUnlocalizedName("aec.aetherHammer");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        
//        // Add additional effectiveness ratings
//        for (Block block : pickGoodOn) {
//        	block.setHarvestLevel("aecHammer", 0);
//        }
//        for (Block block : shovelGoodOn) {
//        	block.setHarvestLevel("aecHammer", 0);
//        }
//        
//        this.setHarvestLevel("aecHammer", 3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemAetherHammer");
		this.activeIcon = iconRegister.registerIcon("aec:itemAetherHammerActive");
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
				InventoryUtils.drainAVFromInventory(player.inventory, AetherCraft.options.getFloat("toolcost")*9F);
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
					Block subject = player.worldObj.getBlock(x, y, z);
					int meta =  player.worldObj.getBlockMetadata(x, y, z);
					if (!(x==xCoord && y==yCoord && z==zCoord) && subject != null && canHammerBreak(subject,meta)) {
						//harvest the block
						
						subject.harvestBlock(player.worldObj, player, x, y, z, meta);
						subject.onBlockHarvested(player.worldObj, x, y, z, meta, player);
						subject.removedByPlayer(player.worldObj, player, x, y, z, true);
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
		if (av < AetherCraft.options.getFloat("toolcost")) {
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
	 public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		if (par3 == null) {return true;}
		return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLivingBase);
	 }
	
	public boolean canHammerBreak(Block block, int meta) {
		return ForgeHooks.canToolHarvestBlock(block, meta, new ItemStack(Items.diamond_pickaxe)) || ForgeHooks.canToolHarvestBlock(block, meta, new ItemStack(Items.diamond_shovel));
	}
}
