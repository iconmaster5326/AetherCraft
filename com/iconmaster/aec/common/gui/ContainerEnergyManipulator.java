package com.iconmaster.aec.common.gui;

import com.iconmaster.aec.common.tileentity.TileEntityEnergyManipulator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnergyManipulator extends Container {
	private TileEntityEnergyManipulator tileEntity;

	public ContainerEnergyManipulator(InventoryPlayer player,
			TileEntityEnergyManipulator tileEntityEnergyManipulator) {
		this.tileEntity = tileEntityEnergyManipulator;
		this.addSlotToContainer(new Slot(this.tileEntity, 0, 8, 6));

		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(this.tileEntity, x + (y * 9)
						+ 1, 8 + x * 18, 52 + y * 18));
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
						174 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player, x, 8 + x * 18, 232));
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
			if (slot < 55) {
				if (!this.mergeItemStack(stackInSlot, 55, 91, false)) {
					return null;
				}
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 1, 55, false)) {
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
