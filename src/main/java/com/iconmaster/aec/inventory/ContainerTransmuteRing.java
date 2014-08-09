package com.iconmaster.aec.inventory;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerTransmuteRing extends Container {
	public InventoryTransmuteRing inv;
	public ItemStack ring;

	public ContainerTransmuteRing(InventoryPlayer player, ItemStack ring) {
		this.ring = ring;
		this.inv = new InventoryTransmuteRing();
		NBTTagCompound tag = ring.getTagCompound();
		if (tag!=null) {
			inv.readFromNBT(tag);
		}
		System.out.println("Opened! "+FMLCommonHandler.instance().getEffectiveSide()+" "+tag);

		this.addSlotToContainer(new Slot(this.inv, 0, 38, 33));
		
		this.addSlotToContainer(new Slot(this.inv, 1, 58, 33));
		
		this.addSlotToContainer(new Slot(this.inv, 2, 78, 33));

		this.bindPlayerInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	protected void bindPlayerInventory(InventoryPlayer player) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18,
						84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			// merges the item into player inventory since its in the tileEntity
			if (slot < 3) {
				if (!this.mergeItemStack(stackInSlot, 3, 9+26, false)) {
					return null;
				}
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 3, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}

			slotObject.onPickupFromSlot(player, stackInSlot);
		}

		return stack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		NBTTagCompound tag = ring.getTagCompound();
		if (tag==null) {
			tag = new NBTTagCompound();
		}
		inv.writeToNBT(tag);
		ring.setTagCompound(tag);
		System.out.println("Closed! "+FMLCommonHandler.instance().getEffectiveSide()+" "+ring.getTagCompound());
	}
}
