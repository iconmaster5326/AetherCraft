package com.iconmaster.aec.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetologistsChest extends BlockContainer implements IAetherTransfer {
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
	        blockIcon = iconRegister.registerIcon("aec:aetoChest");
	    }
	    
	    @Override
	    @SideOnly(Side.CLIENT)
	    public IIcon getIcon(int side,int meta) {
	    	return blockIcon;
	    }
}