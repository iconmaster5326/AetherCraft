package com.iconmaster.aec.tileentity;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.DeviceSyncPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAetherInfuser extends AetherCraftTileEntity implements ISidedInventory, IAetherStorage {
	
	public float infused = 0;

	public TileEntityAetherInfuser() {
		super();
		inventory = new ItemStack[2];
	}

	@Override
	public String getInventoryName() {
		return "aec.infuser";
	}

	@Override
	public boolean handleAether() {
		boolean doneSomething = false;
		ItemStack item = getStackInSlot(0);
		if (InfuserRegistry.getOutputAV(item)!=0) {
			if (infused >= InfuserRegistry.getOutputAV(item)) {
				//create the thing
				ItemStack outputSlot = getStackInSlot(1);
				ItemStack output = InfuserRegistry.getOutput(item);
				if (outputSlot == null) {
					infused -= InfuserRegistry.getOutputAV(item);
					ItemStack stack = output.copy();
					this.setInventorySlotContents(1, stack);
					this.decrStackSize(0, 1);
					doneSomething = true;
				} else if (outputSlot.isItemEqual(output) && outputSlot.stackSize + output.stackSize <= output.getMaxStackSize()) {
					infused -= InfuserRegistry.getOutputAV(item);
					this.decrStackSize(1, -output.stackSize);
					this.decrStackSize(0, 1);
					doneSomething = true;
				}
			} else {
				//drain some AV
				float rate = (float) (8*Math.pow(2,getMetadata()*2));
				
				boolean failure = false;
				if (energy<rate) {
					float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, Math.min(this.getMax(),rate*AetherCraft.options.getFloat("excesspull")));
					energy+=got;
					float amt = Math.min(energy,rate);
					if (amt==0) {
						failure = true;
					} else {
						energy-=amt;
						infused+=amt;
					}
				} else {
					energy-=rate;
					infused+=rate;
				}
				
				doneSomething = !failure;
			}
		}
		return doneSomething;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 1:
			return new int[] {1};
		case 0:
			return new int[] {0};
		default:
			return new int[] {0,1};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot != 1;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot != 0;
	}
	
	@Override
	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			AetherCraftPacketHandler.HANDLER.sendToAllAround(new DeviceSyncPacket(this.xCoord,this.yCoord,this.zCoord,this.energy,this.infused), new TargetPoint(worldObj.provider.dimensionId,xCoord,yCoord,zCoord,8));
	}
	
	public void recieveSync(float par1energy,float infused) {
		this.infused = infused;
		super.recieveSync(par1energy);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.infused = tagCompound.getFloat("infused");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("infused", this.infused);
	}
	
	@Override
	public float getMax() {
		return (float) ((AetherCraft.options.getFloat("ammaxstorage")/2)*(Math.pow(2,getMetadata()*2)));
	}
}
