package com.iconmaster.aec.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryTransmuteRing implements IInventory {
	public ItemStack extractStack;
	public ItemStack condenseStack;
	public ItemStack outputStack;

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		switch (i) {
		case 0:
			return extractStack;
		case 1:
			return condenseStack;
		case 2:
			return outputStack;
		default:
			return null;
		}
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
		switch (slot) {
		case 0:
			extractStack = stack;
			break;
		case 1:
			condenseStack = stack;
			break;
		case 2:
			outputStack = stack;
			break;
		}

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "aec.transmuteRing";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot>-1 && slot<4;
	}

	@Override
	public void markDirty() {
		
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound invtag = new NBTTagCompound();
		if (extractStack != null) {
			NBTTagCompound s1tag = new NBTTagCompound();
			extractStack.writeToNBT(s1tag);
			invtag.setTag("extract", s1tag);
		}
		if (condenseStack != null) {
			NBTTagCompound s2tag = new NBTTagCompound();
			condenseStack.writeToNBT(s2tag);
			invtag.setTag("condense", s2tag);
		}
		if (outputStack != null) {
			NBTTagCompound s3tag = new NBTTagCompound();
			outputStack.writeToNBT(s3tag);
			invtag.setTag("output", s3tag);
		}
		tag.setTag("inventory", invtag);
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagCompound invtag = tag.getCompoundTag("inventory");
		if (invtag!=null) {
			if (invtag.getCompoundTag("extract")!=null) {
				extractStack = ItemStack.loadItemStackFromNBT(invtag.getCompoundTag("extract"));
			}
			if (invtag.getCompoundTag("condense")!=null) {
				condenseStack = ItemStack.loadItemStackFromNBT(invtag.getCompoundTag("condense"));
			}
			if (invtag.getCompoundTag("output")!=null) {
				outputStack = ItemStack.loadItemStackFromNBT(invtag.getCompoundTag("output"));
			}
		}
	}

}
