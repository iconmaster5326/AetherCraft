package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.iconmaster.aec.util.SideUtils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class AetherNetwork {
	
	public static class DeviceData {
		public float maxAV;
		public IAetherStorage device;
		
		public DeviceData(IAetherStorage device, float av) {
			this.device = device;
			this.maxAV = av;
		}
	}
	
	public static float getStoredAV(World world,int x,int y,int z) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float av = 0;
		for (DeviceData device : devices) {
			av += Math.min(device.device.getAether(),device.maxAV);
		}
		return av;
	}
	
	public static float sendAV(World world,int x,int y,int z,float av) {
		float left = av;
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		for (DeviceData device : devices) {
			//System.out.println("Adding "+left);
			left = device.device.addAether(Math.min(left,device.maxAV));
			//System.out.println("Left is "+left);
		}
		//System.out.println("Requested "+av+". Returning "+left);
		return left;
	}
		
	public static float requestAV(World world,int x,int y,int z,float av) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float got = 0;
		for (DeviceData device : devices) {
			//System.out.println("Extraxcting "+(av-got));
			got += device.device.extractAether(Math.min(av-got,device.maxAV));
			//System.out.println("Got is now "+(got));
		}
		//System.out.println("Requested "+av+". Returning "+got);
		return got;
	}
	
	public static boolean canSendAV(World world, int x,int y,int z,float av) {
		float left = av;
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		for (DeviceData device : devices) {
			//System.out.println("[c] Adding "+left);
			left = device.device.tryAddAether(Math.min(left,device.maxAV));
			//System.out.println("[c] Left is "+left);
		}
		//System.out.println("[c] Requested "+av+". Left is "+left+". Returning "+(left==0));
		return left==0;
	}
	
	public static boolean canRequestAV(World world,int x,int y,int z,float av) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float got = 0;
		for (DeviceData device : devices) {
			//System.out.println("[c] Extracting "+(av-got));
			got += device.device.tryExtractAether(Math.min(av-got,device.maxAV));
			//System.out.println("[c] Got is now "+(got));
		}
		//System.out.println("[c] Requested "+av+". Got "+got+". Returning "+(got==av));
		return got==av;
	}
	
	public static ArrayList<DeviceData> getAllConnectedDevices(World world, int x, int y, int z) {
		ArrayList a = new ArrayList();
		HashMap been = new HashMap();
		return getAllConnectedDevices(world, x, y, z, Float.MAX_VALUE, a, been);
	}
	
	public static ArrayList<DeviceData> getAllConnectedDevices(World world, int x, int y, int z,float maxAV,ArrayList a,HashMap been) {
		been.put(encodeCoords(x,y,z),true);
		////System.out.println("Visiting "+x+" "+y+" "+z);
		for (int side : SideUtils.allSides) {
			//System.out.println("SIDE "+side);
			SideUtils.Offset off = new SideUtils.Offset(side);
			int ofx = off.getOffsetX(x);
			int ofy = off.getOffsetY(y);
			int ofz = off.getOffsetZ(z);
			Block block = SideUtils.getBlockFromSide(x,y,z, world, side);
			if (been.get(encodeCoords(ofx,ofy,ofz))==null && block instanceof IAetherTransfer) {
				float nav = Math.min(maxAV,((IAetherTransfer)block).getMaxTransferAV(world, ofx, ofy, ofz, side));
				if (((IAetherTransfer)block).canTransferAV(world, ofx, ofy, ofz, side)) {
					getAllConnectedDevices(world,ofx,ofy,ofz,nav,a,been);
				}
				if (world.getBlockTileEntity(ofx, ofy, ofz)!= null && world.getBlockTileEntity(ofx, ofy, ofz) instanceof IAetherStorage) {
					a.add(new DeviceData((IAetherStorage) world.getBlockTileEntity(ofx, ofy, ofz),nav));
				}
			}


		}
		return a;
	}
	
	private static List encodeCoords(int x,int y, int z) {
		return Arrays.asList(x,y,z);
	}
	
}
