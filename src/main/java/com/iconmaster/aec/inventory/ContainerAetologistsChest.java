package com.iconmaster.aec.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.tileentity.TileEntityAetherExtractor;
import com.iconmaster.aec.tileentity.TileEntityAetologistsChest;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ContainerAetologistsChest extends Container {
	private TileEntityAetologistsChest tileEntity;

	public ContainerAetologistsChest(InventoryPlayer player, TileEntityAetologistsChest tileEntity2) {
		this.tileEntity = tileEntity2;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new SlotAetoChest(this.tileEntity, x + (y * 9), 8 + x * 18, 18 + y * 18));
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
						86 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player, x, 8 + x * 18, 144));
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
			if (slot < 27) {
				if (!this.mergeItemStack(stackInSlot, 27, 27+26, false)) {
					return null;
				}
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 27, false)) {
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
    public ItemStack slotClick(int slot, int button, int mod, EntityPlayer player) {
		//if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {return null;}
		Slot pslot = slot < 0 ? null : (Slot) this.inventorySlots.get(slot);
		if (pslot instanceof SlotAetoChest) {
			if (tileEntity.editMode) {
				//in edit mode
				if (button==1) {
					//remove the stack
					pslot.putStack(null);
				} else {
					//add a stack if the player has one
					InventoryPlayer playerInv = player.inventory;
					ItemStack stackSlot = pslot.getStack();
					ItemStack stackHeld = playerInv.getItemStack();
					float av = AVRegistry.getAV(stackHeld);
					if (av>0) {
						ItemStack clone = stackHeld.copy();
						clone.stackSize = 1;
						pslot.putStack(clone);
						
						return clone;
					}
				}
			} else {
				//in retrieve mode
				InventoryPlayer playerInv = player.inventory;
				ItemStack stackSlot = pslot.getStack();
				ItemStack stackHeld = playerInv.getItemStack();
				if (stackSlot!=null) {
					int qty = 0;
					float av = AVRegistry.getAV(stackSlot);
					float network = AetherNetwork.getStoredAV(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
					if (av>0) {
						if (mod==1) {
							//shift-click, grab up to stack limit if able
							for (int i=1;i<=stackSlot.getMaxStackSize();i++) {
								if (av*i<=network) {
									qty=i;
								}
							}
						} else {
							//normal click, grab 1 if able
							if (av<=network) {
								qty = 1;
							}
						}
						
						if (qty!=0) {
							if (stackHeld == null) {
								ItemStack newStack = new ItemStack(stackSlot.getItem(),qty,stackSlot.getItemDamage());
								playerInv.setItemStack(newStack);
								AetherNetwork.requestAV(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,av*qty);
							} else {
								if (stackHeld.getItem()==stackSlot.getItem() && stackHeld.getItemDamage()==stackSlot.getItemDamage()) {
									int room = stackHeld.getMaxStackSize()-stackHeld.stackSize;
									qty = Math.min(room, qty);
									
									stackHeld.stackSize += qty;
									AetherNetwork.requestAV(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,av*qty);
								}
							}
						}
					}
				}
			}

			return null;
		} else {
			return super.slotClick(slot,button,mod,player);
		}
	}
}
