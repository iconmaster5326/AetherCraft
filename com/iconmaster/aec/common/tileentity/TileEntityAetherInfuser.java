package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherInfuser extends AetherCraftTileEntity implements ISidedInventory, IAetherStorage {
	
	public float infused = 0;

	public TileEntityAetherInfuser() {
		super();
		energyBlockType = AetherCraft.GUI_ID_INFUSER;
		inventory = new ItemStack[2];
	}

	@Override
	public String getInvName() {
		return "aec.infuser";
	}

	@Override
	public void handleAether() {
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
					sync();
				} else if (outputSlot.isItemEqual(output) && outputSlot.stackSize + output.stackSize <= output.getMaxStackSize()) {
					infused -= InfuserRegistry.getOutputAV(item);
					this.decrStackSize(1, -output.stackSize);
					this.decrStackSize(0, 1);
					sync();
				}
			} else {
				//drain some AV
				float  got = Math.min(this.energy,8);
				energy -= got;
				got += AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, 8-got);
				infused += got;
				if (got > 0) {
					sync();
				}
			}
		}
	}

	@Override
	public void calculateProgress() {
		calcMax();
		progress = (int) ((energy / max)*100);
		if (progress > 100) {
			progress = 100;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 1:
			return new int[] {1};
		default:
			return new int[] {0};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot == 0;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 1;
	}
	
	@Override
	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream outputStream = new DataOutputStream(bos);

			try {
				outputStream.writeByte(this.energyBlockType);
				outputStream.writeInt(this.xCoord);
				outputStream.writeInt(this.yCoord);
				outputStream.writeInt(this.zCoord);
				outputStream.writeFloat(this.energy);
				outputStream.writeFloat(this.infused);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Aec";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			if (this.worldObj != null && this.worldObj.provider != null) {
				PacketDispatcher.sendPacketToAllPlayers(packet);
			}
		}
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
	public void calcMax() {
		if (max == 0) {
			max = (float) ((Float.parseFloat(AetherCraft.getOptions("ammaxstorage")))*(Math.pow(2,getMetadata()*2)));
		}
	}
}
