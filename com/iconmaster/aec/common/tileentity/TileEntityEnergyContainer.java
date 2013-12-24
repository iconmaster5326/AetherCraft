package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.IEnergyContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityEnergyContainer extends TileEntity implements
		IInventory, IEnergyContainer {
	public static final byte energyBlockType = 1;

	private ItemStack[] inventory;
	private int energy;
	private int progress;

	private boolean powered;

	public TileEntityEnergyContainer() {
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
		this.energy = tagCompound.getInteger("Energy");
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
		tagCompound.setInteger("Energy", this.energy);
	}

	private void handleEnergy() {
		int batteryMaxStorage = Integer.parseInt(AetherCraft
				.getOptions("ebatterymaxstorage"));
		int ecMaxStorage = Integer.parseInt(AetherCraft
				.getOptions("ecmaxstorage"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack bottomStack = this.getStackInSlot(1);
		boolean doneSomething = false;

		// ------------------- Discharging - TOP SLOT -------------------
		// Energy Battery
		if (topStack != null
				&& AetherCraft.itemEnergyBattery != null
				&& topStack.itemID == AetherCraft.itemEnergyBattery.itemID) {
			if (!topStack.hasTagCompound()) {
				topStack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound tag = topStack.getTagCompound();
			if (!tag.hasKey("EMEV")) {
				tag.setInteger("EMEV", 0);
			}
			int batteryEv = tag.getInteger("EMEV");
			if (batteryEv > batteryMaxStorage) {
				batteryEv = batteryMaxStorage;
				tag.setInteger("EMEV", batteryEv);
			}
			if (batteryEv > 0) {
				if (this.energy + batteryEv <= ecMaxStorage) {
					this.energy += batteryEv;
					tag.setInteger("EMEV", 0);
					doneSomething = true;
				} else {
					tag.setInteger("EMEV", batteryEv - (ecMaxStorage
							- this.energy));
					this.energy = ecMaxStorage;
					doneSomething = true;
				}
			}
		}

		// ------------------- Charging - BOTTOM SLOT -------------------
		// Energy Battery
		if (bottomStack != null
				&& AetherCraft.itemEnergyBattery != null
				&& bottomStack.itemID == AetherCraft.itemEnergyBattery.itemID
				&& this.energy > 0) {
			if (!bottomStack.hasTagCompound()) {
				bottomStack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound tag = bottomStack.getTagCompound();
			if (!tag.hasKey("EMEV")) {
				tag.setInteger("EMEV", 0);
			}
			int batteryEv = tag.getInteger("EMEV");
			if (batteryEv < batteryMaxStorage) {
				if (batteryEv + this.energy <= batteryMaxStorage) {
					tag.setInteger("EMEV", batteryEv + this.energy);
					this.energy = 0;
					doneSomething = true;
				} else {
					this.energy -= batteryMaxStorage - batteryEv;
					tag.setInteger("EMEV", batteryMaxStorage);
					doneSomething = true;
				}
			}
		}
		if (doneSomething) {
			this.sync();
			return;
		}
	}

	@Override
	public void updateEntity() {
		this.calculateProgress();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER
				&& !this.powered) {
			handleEnergy();
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
				outputStream.writeInt(this.energy);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "GraEnMa";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			if (this.worldObj != null && this.worldObj.provider != null) {
				PacketDispatcher.sendPacketToAllAround(this.xCoord,
						this.yCoord, this.zCoord, 8,
						this.worldObj.provider.dimensionId, packet);
			}
		}
	}

	public void recieveSync(int par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {
		this.progress = (int) ((float) this.energy
				/ (float) Integer.parseInt(AetherCraft
						.getOptions("ecmaxstorage")) * 100.0f);
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
	public int addEnergy(int ev) {
		int ecmaxstorage = Integer.parseInt(AetherCraft
				.getOptions("ecmaxstorage"));
		if (this.energy + ev <= ecmaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			int rest = ecmaxstorage - this.energy;
			this.energy = ecmaxstorage;
			this.sync();
			return rest;
		}
	}

	@Override
	public int extractEnergy(int ev) {
		if (this.energy - ev >= 0) {
			this.energy -= ev;
			this.sync();
			return ev;
		}
		int rest = ev - this.energy;
		this.energy = 0;
		this.sync();
		return rest;
	}

	@Override
	public void setEnergy(int ev) {
		this.energy = ev;
		this.sync();
	}

	@Override
	public int getEnergy() {
		return this.energy;
	}
}