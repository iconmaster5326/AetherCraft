package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IConsumeBehavior;
import com.iconmaster.aec.aether.IProduceBehavior;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.SideUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * Extend this to get a bunch of default AetherCraft TE functions.
 * @author iconmaster
 *
 */
public class AetherCraftTileEntity extends TileEntity implements
		ISidedInventory, IAetherStorage {
	public byte energyBlockType;

	protected ItemStack[] inventory;
	protected float energy;
	protected int progress;
	protected boolean powered;
	public float max;
	
	protected boolean polled = false;

	public AetherCraftTileEntity() {
		
	}

	public int getSizeInventory() {
		return this.inventory.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = this.getStackInSlot(slot);

		if (stack != null) {
			if (stack.stackSize <= amount) {
				this.setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);

				if (stack.stackSize == 0) {
					this.setInventorySlotContents(slot, null);
				}
			}
		}

		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = this.getStackInSlot(slot);

		if (stack != null) {
			setInventorySlotContents(slot, null);
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "gra.tileentityem";
	}

	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) == this
				&& player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
						this.zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot > 0) {
			return true;
		}

		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");

			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}

		this.energy = tagCompound.getFloat("AV");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];

			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		tagCompound.setTag("Inventory", itemList);
		tagCompound.setFloat("AV", this.energy);
	}

	public void handleAether() {
	}

	public void setPoweredState(boolean state) {
		this.powered = state;
	}

	@Override
	public void updateEntity() {
		this.calculateProgress();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER
				&& !this.powered) {
//			this.updateConnectedSides();
			handleAether();
		}
	}

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

	public void recieveSync(float par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {

	}

	public int getProgress() {
		return this.progress;
	}

	public int getStackableSlot(ItemStack cStack) {
		ItemStack stack;
		if (cStack == null) {return 1;}
		for (int i = 1; i < this.inventory.length; i++) {
			stack = this.getStackInSlot(i);

			if (stack != null && stack.isItemEqual(cStack)
					&& stack.stackSize < this.getInventoryStackLimit()
					&& stack.stackSize < stack.getMaxStackSize()) {
				return i;
			}
		}
		return -1;
	}

	public int getEmptySlot() {
		ItemStack stack;
		for (int i = 1; i < this.inventory.length; i++) {
			stack = this.getStackInSlot(i);
			if (stack == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public float addAether(float ev) {
		float ammaxstorage = Float.parseFloat(AetherCraft
				.getOptions("ammaxstorage"));
		if (this.energy + ev <= ammaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			float rest = (this.energy + ev) - ammaxstorage;
			this.energy = ammaxstorage;
			this.sync();
			return rest;
		}
	}

	@Override
	public float extractAether(float av) {
		//System.out.println("Got "+av+" AV.");
		if (this.energy - av >= 0) {
			//System.out.println("Had enough AV. Returning "+av+" AV.");
			this.energy -= av;
			this.sync();
			return av;
		}
		float rest = this.energy;
		//System.out.println("Did not have enough AV. Returning "+rest+" AV.");
		this.energy = 0;
		this.sync();
		return rest;
	}
	
	@Override
	public float tryAddAether(float ev) {
		float ammaxstorage = Float.parseFloat(AetherCraft
				.getOptions("ammaxstorage"));
		if (this.energy + ev <= ammaxstorage) {
			//this.energy += ev;
			//this.sync();
			return 0;
		} else {
			float rest = (this.energy + ev) - ammaxstorage;
			//this.energy = ammaxstorage;
			//this.sync();
			return rest;
		}
	}

	@Override
	public float tryExtractAether(float av) {
		//System.out.println("Got "+av+" AV.");
		if (this.energy - av >= 0) {
			//System.out.println("Had enough AV. Returning "+av+" AV.");
			//this.energy -= av;
			//this.sync();
			return av;
		}
		float rest = this.energy;
		//System.out.println("Did not have enough AV. Returning "+rest+" AV.");
		//this.energy = 0;
		//this.sync();
		return rest;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return SideUtils.allSides;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return true;
	}
	
	public float getPossibleAether() {
		return getAether() + AetherNetwork.getStoredAV(worldObj, xCoord, yCoord, zCoord);
	}
	
	public boolean canConsume(ItemStack currentStack) {
		ItemStack topStack = this.getStackInSlot(0);
		if (currentStack == null) {
			return false;
		}
		if (topStack != null && currentStack.itemID == topStack.itemID && currentStack.getItemDamage() == topStack.getItemDamage()) {
			return false;
		}
		float av = AVRegistry.getAV(currentStack);
		if (av<=0 ) {
			return false;
		}
		return true;
	}
	public boolean canProduce(ItemStack currentItem) {
		float av = AVRegistry.getAV(currentItem);
		if (this.getStackableSlot(currentItem)<=0 && this.getEmptySlot() <=0) {return false;}
		return av>0 && av<=getPossibleAether();
	}

	@Override
	public float getAether() {
		//System.out.println("Returning "+this.energy);
		if (!polled) {
			requestSync();
			polled = true;
		}
		return this.energy;
	}

	public void requestSync() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
	
		try {
			outputStream.writeByte(this.energyBlockType);
			outputStream.writeInt(this.xCoord);
			outputStream.writeInt(this.yCoord);
			outputStream.writeInt(this.zCoord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "AecReq";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToServer(packet);
	}
}
