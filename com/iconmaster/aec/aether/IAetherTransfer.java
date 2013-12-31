package com.iconmaster.aec.aether;

import net.minecraft.world.World;

public interface IAetherTransfer {
	public boolean canTransferAV(World world,int x,int y,int z, int sideFrom);
	
	public float getMaxTransferAV(World world,int x,int y,int z, int sideFrom);
}
