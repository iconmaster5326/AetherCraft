package com.iconmaster.aec.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.tileentity.TileEntityAetherExtractor;

public class ContainerAetherExtractor extends Container {
	private TileEntityAetherExtractor tileEntity;

	public ContainerAetherExtractor(InventoryPlayer player, TileEntityAetherExtractor tileEntityAetherExtractor) {
		this.tileEntity = tileEntityAetherExtractor;

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 2; x++) {
				this.addSlotToContainer(new Slot(this.tileEntity, x + (y * 2), 8 + x * 18, 8 + y * 18));
			}
		}

		this.bindPlayerInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEntity.isUseableByPlayer(player);
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
			if (slot < 8) {
				if (!this.mergeItemStack(stackInSlot, 8, 8+26, false)) {
					return null;
				}
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 8, false)) {
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
}
