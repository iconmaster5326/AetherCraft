package com.iconmaster.aec.common.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.tileentity.TileEntityAetherContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetherContainer extends BlockContainer implements IAetherTransfer {
	private Icon blockIconTop, blockIconBottom;

	public BlockAetherContainer(int id, Material material) {
		super(id, material);
        this.setHardness(1.5f);
        this.setUnlocalizedName("aec.aetherContainer");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0)
            {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
                                                       + rz, new ItemStack(item.itemID, item.stackSize,
                                                               item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound(
                        (NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z,
                                int side)
    {
        return side == 0 ? this.blockIconBottom : side == 1 ? this.blockIconTop
               : this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister
                         .registerIcon("aec:aetherContainerSide");
        this.blockIconTop = iconRegister
                            .registerIcon("aec:aetherContainerTop");
        this.blockIconBottom = iconRegister
                               .registerIcon("aec:aetherContainerBottom");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntityAetherContainer tileEntity = (TileEntityAetherContainer) world
                .getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_EC, world, x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        return side == 0 ? this.blockIconBottom : side == 1 ? this.blockIconTop
               : this.blockIcon;
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAetherContainer();
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (!world.isRemote) {
			boolean state = world.isBlockIndirectlyGettingPowered(x, y, z);
			TileEntityAetherContainer tileEntity = (TileEntityAetherContainer) world
					.getBlockTileEntity(x, y, z);
			if (tileEntity != null) {
				tileEntity.setPoweredState(state);
			}
		}
	}

    /**
     * Ticks the block if it's been scheduled
     */
	@Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
		boolean state = world.isBlockIndirectlyGettingPowered(x, y, z);
		TileEntityAetherContainer tileEntity = (TileEntityAetherContainer) world
				.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			tileEntity.setPoweredState(state);
		}
    }

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		if (!world.isRemote) {
			boolean state = world.isBlockIndirectlyGettingPowered(x, y, z);
			TileEntityAetherContainer tileEntity = (TileEntityAetherContainer) world
					.getBlockTileEntity(x, y, z);
			if (tileEntity != null) {
				tileEntity.setPoweredState(state);
			}
		}
	}
}
