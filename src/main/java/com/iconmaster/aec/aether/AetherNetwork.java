package com.iconmaster.aec.aether;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.iconmaster.aec.util.SideUtils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * A set of classes that deal with transferring Aether across devices.
 * @author iconmaster
 *
 */
public class AetherNetwork {
	
	/**
	 * A class used internally by the lookup function. Lists both a device and the maximum amount of Aether that can be used with it.
	 * @author iconmaster
	 *
	 */
	public static class DeviceData {
		public float maxAV;
		public IAetherStorage device;
		
		public DeviceData(IAetherStorage device, float av) {
			this.device = device;
			this.maxAV = av;
		}
	}

	private static boolean checkSelf = false;
	
	/**
	 * Given the coordinates of a network start, returns the amount of AV stored in the network that can be retrieved.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static float getStoredAV(World world,int x,int y,int z) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float av = 0;
		for (DeviceData device : devices) {
			av += Math.min(device.device.getAether(),device.maxAV);
		}
		return av;
	}
	
	public static float getAbsoluteStoredAV(World world,int x,int y,int z) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float av = 0;
		for (DeviceData device : devices) {
			av += device.device.getAether();
		}
		return av;
	}
	
	/**
	 * Attempts to send AV into the network at the given coords. Returns the amount of AV that was not sent successfully; this will be greater than 0 if the network could not hold all the Aether put into it.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param av
	 * @return
	 */
	public static float sendAV(World world,int x,int y,int z,float av) {
		float left = av;
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		for (DeviceData device : devices) {
			//System.out.println("Adding "+left);
			left = device.device.addAether(Math.min(left,device.maxAV))+Math.max(0,left-device.maxAV);
			//System.out.println("Left is "+left);
		}
		//System.out.println("Requested "+av+". Returning "+left);
		return left;
	}
		
	/**
	 * Attempts to retrieve AV from the network at the given coords. Returns the amount received in actuality; this will be less than what you passed to it if there wasn't enough Aether in the network.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param av
	 * @return
	 */
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
	
	/**
	 * like <code>sendAV</code>, but only checks the result of the operation and does not affect any devices. Use this to see if putting AV in the network is possible.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param av
	 * @return
	 */
	public static float canSendAV(World world, int x,int y,int z,float av) {
		float left = av;
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		for (DeviceData device : devices) {
			//System.out.println("[c] Adding "+left);
			left = device.device.tryAddAether(Math.min(left,device.maxAV))+Math.max(0,left-device.maxAV);
			//System.out.println("[c] Left is "+left);
		}
		//System.out.println("[c] Requested "+av+". Left is "+left+". Returning "+(left==0));
		return left;
	}
	
	/**
	 * Like <code>requestAV</code>, but only checks the result of the operation and does not affect any devices. Use to see if there is enough AV in the network.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param av
	 * @return
	 */
	public static float canRequestAV(World world,int x,int y,int z,float av) {
		ArrayList<DeviceData> devices = getAllConnectedDevices(world,x,y,z);
		float got = 0;
		for (DeviceData device : devices) {
			//System.out.println("[c] Extracting "+(av-got));
			got += device.device.tryExtractAether(Math.min(av-got,device.maxAV));
			//System.out.println("[c] Got is now "+(got));
		}
		//System.out.println("[c] Requested "+av+". Got "+got+". Returning "+(got==av));
		return got;
	}
	
	/**
	 * Returns an <code>ArrayList</code> of <code>DeviceData</code>s that correspond to devices on the network at the given coords.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static ArrayList<DeviceData> getAllConnectedDevices(World world, int x, int y, int z) {
		ArrayList a = new ArrayList();
		HashMap been = new HashMap();
		return getAllConnectedDevices(world, x, y, z, Float.MAX_VALUE, a, been);
	}
	
	public static ArrayList<DeviceData> getAllConnectedDevices(World world, int x, int y, int z,float maxAV,ArrayList a,HashMap been) {
		if (!checkSelf) {been.put(encodeCoords(x,y,z),true);}
		//System.out.println("Visiting "+x+" "+y+" "+z);
		for (int side : checkSelf? new int[] {-1,0,1,2,3,4,5} : SideUtils.allSides) {
			//System.out.println("SIDE "+side);
			SideUtils.Offset off = new SideUtils.Offset(side);
			int ofx = off.getOffsetX(x);
			int ofy = off.getOffsetY(y);
			int ofz = off.getOffsetZ(z);
			Block block = SideUtils.getBlockFromSide(x,y,z, world, side);
			if (been.get(encodeCoords(ofx,ofy,ofz))==null && block instanceof IAetherTransfer) {
				float nav = Math.min(maxAV,((IAetherTransfer)block).getMaxTransferAV(world, ofx, ofy, ofz, side));
				if (((IAetherTransfer)block).canTransferAV(world, ofx, ofy, ofz, side)) {
					if (checkSelf) {been.put(encodeCoords(x,y,z),true);}
					getAllConnectedDevices(world,ofx,ofy,ofz,nav,a,been);
				}
				if (world.getTileEntity(ofx, ofy, ofz)!= null && world.getTileEntity(ofx, ofy, ofz) instanceof IAetherStorage) {
					//System.out.println("Got a device");
					a.add(new DeviceData((IAetherStorage) world.getTileEntity(ofx, ofy, ofz),nav));
				}
			}


		}
		return a;
	}
	
	private static List encodeCoords(int x,int y, int z) {
		return Arrays.asList(x,y,z);
	}
	
	public static void setCheckSelfMode(boolean mode) {
		checkSelf  = mode;
	}
	
}
