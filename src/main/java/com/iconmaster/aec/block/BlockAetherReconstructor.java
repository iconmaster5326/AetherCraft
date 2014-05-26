package com.iconmaster.aec.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetherReconstructor;

public class BlockAetherReconstructor extends AetherCraftBlock implements IAetherTransfer {

	public BlockAetherReconstructor(Material material,String name) {
		super(material,name);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntityAetherReconstructor tileEntity = (TileEntityAetherReconstructor) world
                .getTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_RECONSTRUCTOR, world, x, y, z);
        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAetherReconstructor();
	}
    
    @Override
    public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
    	list.add(new ItemStack(this,1,0));
    }
}
