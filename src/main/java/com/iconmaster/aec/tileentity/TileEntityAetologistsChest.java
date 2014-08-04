package com.iconmaster.aec.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.DeviceSyncPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetologistsChest extends AetherCraftTileEntity implements ISidedInventory, IAetherStorage {
	public boolean editMode = false;
	
	public TileEntityAetologistsChest() {
		super();
		inventory = new ItemStack[27];
	}

	@Override
	public String getInventoryName() {
		return "aec.chest";
	}

	@Override
	public void handleAether() {

	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;

		result = new int[this.getSizeInventory()];
		for (int i = 0; i < this.getSizeInventory()-1; i++) {
			result[i] = i;
		}
		
		return result;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
	
	@Override
	public void calcMax() {
		if (max == 0) {
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))/2)*(Math.pow(2,2*2)));
		}
	}
	
	@Override
	public void calcLimit() {
		if (limit!=0) {return;}
		limit = (float) ((Float.parseFloat(AetherCraft.getOptions("avlimit")))*(Math.pow(2,2*2)));
	}
	
	@Override
	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			AetherCraftPacketHandler.HANDLER.sendToAll(new DeviceSyncPacket(this.xCoord,this.yCoord,this.zCoord,this.energy,this.editMode));
	}

	public void recieveSync(float av1, boolean b) {
		super.recieveSync(av1);
		this.editMode = b;
	}
}
