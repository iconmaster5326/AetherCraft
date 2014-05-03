package com.iconmaster.aec.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetherInfuser;

public class BlockAetherInfuser extends AetherCraftBlock implements IAetherTransfer {
	public BlockAetherInfuser(Material material,String name) {
		super(material, name);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntityAetherInfuser tileEntity = (TileEntityAetherInfuser) world.getTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		player.openGui(AetherCraft.instance, AetherCraft.GUI_ID_INFUSER, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAetherInfuser();
	}
}