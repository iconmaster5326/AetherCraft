package com.iconmaster.aec.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetologistsChest extends BlockContainer implements IAetherTransfer {
	public IIcon topIcon;
	public IIcon sideIcon;
	public IIcon bottomIcon;
	public IIcon frontIcon;
	
	public BlockAetologistsChest(Material material) {
		super(material);
		this.setCreativeTab(AetherCraft.tabAetherCraft);
		this.setBlockName("aec.aetoChest");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntityAetologistsChest tileEntity = (TileEntityAetologistsChest) world.getTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		player.openGui(AetherCraft.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityAetologistsChest();
	}

	@Override
	public boolean canTransferAV(World world, int x, int y, int z, int sideFrom) {
		return true;
	}

	@Override
	public float getMaxTransferAV(World world, int x, int y, int z, int sideFrom) {
		return (float) ((Float.parseFloat(AetherCraft.getOptions("flowrate")))*(Math.pow(2,2*2)));
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
       frontIcon = iconRegister.registerIcon("aec:aetoChestFront");
       sideIcon = iconRegister.registerIcon("aec:aetoChestSide");
       topIcon = iconRegister.registerIcon("aec:aetoChestTop");
       bottomIcon = iconRegister.registerIcon("aec:aetoChestBottom");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side,int meta) {
    	if (meta==0 && side==3) {
    		return frontIcon;
    	}
    	
    	if (side==1) {
    		return topIcon;
    	}
    	
    	if (side==0) {
    		return bottomIcon;
    	}
    	
    	return side==meta? frontIcon : sideIcon;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        byte b0 = 0;
        int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            b0 = 2;
        }

        if (l == 1)
        {
            b0 = 5;
        }

        if (l == 2)
        {
            b0 = 3;
        }

        if (l == 3)
        {
            b0 = 4;
        }

        world.setBlockMetadataWithNotify(x, y, z, b0, 3);
    }
}