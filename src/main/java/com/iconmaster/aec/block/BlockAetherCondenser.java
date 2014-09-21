package com.iconmaster.aec.block;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.tileentity.TileEntityAetherCondenser;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAetherCondenser extends AetherCraftBlock {
	public BlockAetherCondenser(Material material,String name) {
		super(material,name);
		this.setBlockName("aec.aetherCondenser");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntityAetherCondenser tileEntity = (TileEntityAetherCondenser) world.getTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		player.openGui(AetherCraft.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAetherCondenser();
	}
}