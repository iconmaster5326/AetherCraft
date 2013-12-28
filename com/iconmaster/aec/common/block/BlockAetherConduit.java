package com.iconmaster.aec.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.IAetherTransfer;

public class BlockAetherConduit extends Block implements IAetherTransfer {

	public BlockAetherConduit(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public boolean canTransferAV(World world, int x, int y, int z, int sideFrom) {
		return true;
	}

}
