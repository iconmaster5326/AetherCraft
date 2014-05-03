package com.iconmaster.aec.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetherContainer;

public class BlockAetherContainer extends AetherCraftBlock implements IAetherTransfer {
	private IIcon blockIconTop, blockIconBottom;

	public BlockAetherContainer(Material material,String name) {
		super(material,name);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntityAetherContainer tileEntity = (TileEntityAetherContainer) world
                .getTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_CONTAINER, world, x, y, z);
        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAetherContainer();
	}
}
