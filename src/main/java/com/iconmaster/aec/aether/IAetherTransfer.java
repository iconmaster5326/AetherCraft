package com.iconmaster.aec.aether;

import net.minecraft.world.World;

/**
 * Blocks implement this class to be able to transfer AV across. If you have a TE, be sure to make it implement IAetherStorage too!
 * @author iconmaster
 *
 */
public interface IAetherTransfer {
	/**
	 * Given a side and a set of coords, returns if AV can be transferred through.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param sideFrom
	 * @return
	 */
	public boolean canTransferAV(World world,int x,int y,int z, int sideFrom);
	
	/**
	 * Given a side and a set of coords, returns the maximum amount of AV can be transferred through.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param sideFrom
	 * @return
	 */
	public float getMaxTransferAV(World world,int x,int y,int z, int sideFrom);
}
