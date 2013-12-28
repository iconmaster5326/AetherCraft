package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherManipulator extends TileEntity implements
		ISidedInventory, IAetherStorage {
	public static final byte energyBlockType = 0;

	private ItemStack[] inventory;
	private float energy;
	private int progress;
	private boolean powered;

	// 0 = Bottom, 1 = Top, 2 = North, 3 = South, 4 = East, 5 = West
	private boolean connectedSides[];

	public TileEntityAetherManipulator() {
		inventory = new ItemStack[55];
		connectedSides = new boolean[6];
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

	private void handleAether() {
		float emMaxStorage = Float.parseFloat(AetherCraft.getOptions("ammaxstorage"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 1; i < this.getSizeInventory(); i++) {
			currentStack = this.getStackInSlot(i);

			// ------------------- Consuming -------------------
			if (canConsume(currentStack)) {
				float stackEv = AVRegistry.getAV(currentStack);
				stackEv *= Float.parseFloat(AetherCraft.getOptions("consumeprecision")) / 100.0f;
				//System.out.println("Consuming...");
				if (stackEv+getAether()>emMaxStorage) {
					//System.out.println("Has more aether than we can hold!");
					float orig = extractAether(stackEv);
					float left = AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, orig);
					if (left > 0) {
						//System.out.println("Could not transfer!");
						addAether(left);
						this.sync();
						return;
					}
				}
				//System.out.println("Transferred!");
				this.addAether(stackEv);
				this.decrStackSize(i, 1);
				doneSomething = true;
			}

			// ------------------- Creating -------------------
			if (canProduce(topStack)) {
				int slot = this.getStackableSlot(topStack);

				if (slot > 0) {
					ItemStack newStack = this.getStackInSlot(slot);
					newStack.stackSize++;
					this.extractAether(AVRegistry.getAV(topStack));
					doneSomething = true;
				} else {
					slot = this.getEmptySlot();

					if (slot > 0) {
						float av = AVRegistry.getAV(topStack);
						if (getAether() - av < 0) {
							float got = AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, av);
							if (got < av) {
								this.sync();
								return;
							}
						} else {
							this.extractAether(av);
						}
						
						ItemStack newStack = new ItemStack(topStack.itemID,1,topStack.getItemDamage());
						//newStack.stackSize = 1;
						this.setInventorySlotContents(slot, newStack);
						
						doneSomething = true;
					}
				}
			}
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("instantconsume")) && i >= 9 && doneSomething) {
				this.sync();
				return;
			}
		}
		if (doneSomething) {
			this.sync();
			return;
		}
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
				PacketDispatcher.sendPacketToAllAround(this.xCoord,
						this.yCoord, this.zCoord, 7,
						this.worldObj.provider.dimensionId, packet);
			}
		}
	}

	public void recieveSync(float par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {
		ItemStack stack = this.getStackInSlot(0);

		if (stack != null) {
			this.progress = (int) ((float) this.getAether()
					/ (float) AVRegistry.getAV(stack) * 100.0f);

			if (this.progress > 100) {
				this.progress = 100;
			}
		} else {
			this.progress = 0;
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

	@Override
	public float addAether(float ev) {
		float ammaxstorage = Float.parseFloat(AetherCraft
				.getOptions("ammaxstorage"));
		if (this.energy + ev <= ammaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			float rest = ammaxstorage - this.energy;
			this.energy = ammaxstorage;
			this.sync();
			return rest;
		}
	}

	@Override
	public float extractAether(float l) {
		if (this.energy - l >= 0) {
			this.energy -= l;
			this.sync();
			return l;
		}
		float rest = l - this.energy;
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

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 0:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
			// Top
		case 1:
			return new int[] { 0 };
			// Sides
		default:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
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
//		if (getAether()+av>Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))) {
//			return false;
//		}
		return true;
	}
	public boolean canProduce(ItemStack currentItem) {
		float av = AVRegistry.getAV(currentItem);
		return av>0 && av<=getPossibleAether();
	}
}
