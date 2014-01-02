package com.iconmaster.aec.common.block;

import java.util.Random;

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

public class BlockAetherContainer extends AetherCraftBlock implements IAetherTransfer {
	private Icon blockIconTop, blockIconBottom;

	public BlockAetherContainer(int id, Material material,String name) {
		super(id, material,name);
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

        player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_CONTAINER, world, x, y, z);
        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAetherContainer();
	}
}
