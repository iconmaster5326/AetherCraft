package com.iconmaster.aec.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * This class is a set of utilities concerning sided blocks.
 * @author iconmaster
 *
 */
public class SideUtils {
	/**
	 * A list of all sides of a block.
	 */
	public static int[] allSides = new int[] {0,1,2,3,4,5};
	
	/**
	 * An object representing a sided offset. Call the getOfset methods to offset coords by a sided value.
	 * @author iconmaster
	 *
	 */
	public static class Offset {
		private int x = 0;
		private int y = 0;
		private int z = 0;
		
		public Offset(int side) {
			switch(side) {
				case (0):
					y = -1;
					break;
				case (1):
					y = 1;
					break;
				case (2):
					z = -1;
					break;
				case (3):
					z = 1;
					break;
				case (4):
					x = -1;
					break;
				case (5):
					x = 1;
					break;
			}
			//System.out.println("Side was "+side+". Selected offsets are "+x+" "+y+" "+z);
		}
		
		public int getOffsetX(int x) {
			return x+this.x;
		}
		
		public int getOffsetY(int y) {
			return y+this.y;
		}
		
		public int getOffsetZ(int z) {
			return z+this.z;
		}
	}
	
	/**
	 * Returns the block to the given side of a given coord.
	 * @param x
	 * @param y
	 * @param z
	 * @param world
	 * @param side
	 * @return
	 */
	public static Block getBlockFromSide(int x, int y, int z, World world,int side) {
		
		Offset off = new Offset(side);
		return world.getBlock(off.getOffsetX(x), off.getOffsetY(y), off.getOffsetZ(z));
	}
	
	/**
	 * Given a side, returns the opposite side.
	 * @param side
	 * @return
	 */
	public static int getOppositeSide(int side) {
		switch(side) {
		case(0):
			return 1;
		case (1):
			return 0;
		case (2):
			return 3;
		case (3):
			return 2;
		case (4):
			return 5;
		case (5):
			return 4;
		}
		return -1;
	}
	
	/**
	 * If [4] is -1 , there was no block within reach.
	 * @return the side hit
	 */
	public static int getBlockHitSide(World theWorld, EntityPlayer thePlayer, double reach)
	{
		int blockInfo = -1;

		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(theWorld, thePlayer, true, reach);
		if (movingobjectposition != null) {
			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
				blockInfo = movingobjectposition.sideHit;
			}
		}
		return blockInfo;
	}

	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer entityplayer, boolean flag, double reach)
	{
		float f = 1.0F;
		float playerPitch = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
		float playerYaw = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
		double playerPosX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * f;
		double playerPosY = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * f + 1.6200000000000001D) - entityplayer.yOffset;
		double playerPosZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * f;
		Vec3 vecPlayer = Vec3.createVectorHelper(playerPosX, playerPosY, playerPosZ);
		float cosYaw = MathHelper.cos(-playerYaw * 0.01745329F - 3.141593F);
		float sinYaw = MathHelper.sin(-playerYaw * 0.01745329F - 3.141593F);
		float cosPitch = -MathHelper.cos(-playerPitch * 0.01745329F);
		float sinPitch = MathHelper.sin(-playerPitch * 0.01745329F);
		float pointX = sinYaw * cosPitch;
		float pointY = sinPitch;
		float pointZ = cosYaw * cosPitch;
		Vec3 vecPoint = vecPlayer.addVector(pointX * reach, pointY * reach, pointZ * reach);
		MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vecPlayer, vecPoint, flag);
		return movingobjectposition;
	}
}
