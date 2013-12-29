package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.aether.IAetherStorageItem;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherContainer extends TileEntity implements
		IInventory, IAetherStorage {
	public static final byte energyBlockType = 1;

	private ItemStack[] inventory;
	private float energy;
	private int progress;

	private boolean powered;

	public TileEntityAetherContainer() {
		inventory = new ItemStack[2];
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
		return "gra.tileentityec";
	}

	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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
		return true;
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

	private void handleAether() {
		float ecMaxStorage = Float.parseFloat(AetherCraft
				.getOptions("acmaxstorage"));
		float chargeRate = Float.parseFloat(AetherCraft
				.getOptions("chargerate"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack bottomStack = this.getStackInSlot(1);

		// ------------------- Discharging - TOP SLOT -------------------
		if (topStack != null && topStack.getItem() instanceof IAetherStorageItem) {
			float got =  ((IAetherStorageItem)topStack.getItem()).extractAether(topStack, Math.min(ecMaxStorage - energy,chargeRate));
			energy += got;
			this.sync();

		}

		// ------------------- Charging - BOTTOM SLOT -------------------
		if (bottomStack != null && bottomStack.getItem() instanceof IAetherStorageItem) {
			float rest = ((IAetherStorageItem)bottomStack.getItem()).addAether(bottomStack, Math.min(this.energy,chargeRate));
			this.energy -= Math.min(this.energy,chargeRate)-rest;
			this.sync();
		}
	}

	@Override
	public void updateEntity() {
		this.calculateProgress();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER
				&& !this.powered) {
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
				PacketDispatcher.sendPacketToAllAround(this.xCoord,
						this.yCoord, this.zCoord, 8,
						this.worldObj.provider.dimensionId, packet);
			}
		}
	}

	public void recieveSync(float f) {
		this.energy = f;
	}

	public void calculateProgress() {
		this.progress = (int) ((float) this.energy
				/ (float) Float.parseFloat(AetherCraft
						.getOptions("acmaxstorage")) * 100.0f);
		if (this.progress > 100) {
			this.progress = 100;
		}
	}

	public int getProgress() {
		return this.progress;
	}

	public int getStackableSlot(ItemStack cStack) {
		ItemStack stack;

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

	public void setPoweredState(boolean state) {
		this.powered = state;
	}

	@Override
	public float addAether(float ev) {
		float acmaxstorage = Float.parseFloat(AetherCraft
				.getOptions("acmaxstorage"));
		if (this.energy + ev <= acmaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			float rest = (this.energy + ev) - acmaxstorage;
			this.energy = acmaxstorage;
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
	public void setAether(float ev) {
		this.energy = ev;
		this.sync();
	}

	@Override
	public float getAether() {
		return this.energy;
	}
}