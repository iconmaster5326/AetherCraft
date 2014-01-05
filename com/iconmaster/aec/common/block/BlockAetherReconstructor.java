package com.iconmaster.aec.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
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
import com.iconmaster.aec.common.tileentity.TileEntityAetherReconstructor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetherReconstructor extends AetherCraftBlock implements IAetherTransfer {
	private Icon blockIconTop, blockIconBottom;

	public BlockAetherReconstructor(int id, Material material,String name) {
		super(id, material,name);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntityAetherReconstructor tileEntity = (TileEntityAetherReconstructor) world
                .getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_RECONSTRUCTOR, world, x, y, z);
        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAetherReconstructor();
	}
    
    @Override
    public void getSubBlocks(int par1, CreativeTabs tab, List list) {
    	list.add(new ItemStack(this,1,0));
    }
}
