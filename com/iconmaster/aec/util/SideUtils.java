package com.iconmaster.aec.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class SideUtils {
	public static int[] allSides = new int[] {0,1,2,3,4,5};
	
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
	
	public static Block getBlockFromSide(int x, int y, int z, World world,int side) {
		
		Offset off = new Offset(side);
		return Block.blocksList[world.getBlockId(off.getOffsetX(x), off.getOffsetY(y), off.getOffsetZ(z))];
	}
	
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
}
