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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherExtractor extends AetherCraftTileEntity implements
		ISidedInventory, IAetherStorage {
	public TileEntityAetherExtractor() {
		energyBlockType = AetherCraft.GUI_ID_EXTRACTOR;
		max = Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))/2;
		inventory = new ItemStack[8];
	}

	@Override
	public String getInvName() {
		return "aec.extractor";
	}

	@Override
	public void handleAether() {
		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 0; i < this.getSizeInventory(); i++) {
			boolean failed = false;
			currentStack = this.getStackInSlot(i);

			// ------------------- Consuming -------------------
			if (canConsume(currentStack)) {
				float stackEv;
				if (currentStack.getItem() instanceof IConsumeBehavior) {
					stackEv = ((IConsumeBehavior)currentStack.getItem()).getConsumeAV(currentStack);
				} else {
					stackEv = AVRegistry.getAV(currentStack);
				}
				stackEv *= Float.parseFloat(AetherCraft.getOptions("consumeprecision")) / 100.0f;
				//System.out.println("Consuming... ");
				if (stackEv+energy>max) {
					//System.out.println("Has more aether than we can hold!");
					boolean canSend = AetherNetwork.canSendAV(worldObj, xCoord, yCoord, zCoord, stackEv+energy-max);
					if (!canSend) {
						//System.out.println("Could not transfer!");
						//AetherNetwork.requestAV(worldObj, xCoord, yCoord, zCoord, stackEv-energy-left);
						failed = true;
					} else {
						AetherNetwork.sendAV(worldObj, xCoord, yCoord, zCoord, stackEv+energy-max);
						energy = max;
					}
				} else {
					//System.out.println("Has "+energy+". Adding "+stackEv);
					energy += stackEv;
				}
				//System.out.println("Done Consuming! ");
				
				if (!failed) {
					if (currentStack.getItem() instanceof IConsumeBehavior) {
						ItemStack output =  ((IConsumeBehavior)currentStack.getItem()).consume(currentStack,inventory);
						if (output == null) {
							this.decrStackSize(i, 1);
						} else {
							int slot = this.getStackableSlot(output);
							if (slot >= 0) {
								ItemStack newStack = this.getStackInSlot(slot);
								newStack.stackSize++;
							} else {
								slot = this.getEmptySlot();
								//newStack.stackSize = 1;
								this.setInventorySlotContents(slot, output);
							}
						}
					} else {
						this.decrStackSize(i, 1);
					}
					doneSomething = true;
				}
			}
			
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("instantconsume")) && doneSomething) {
				this.sync();
				return;
			}
		}
		if (doneSomething) {
			this.sync();
			return;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;

		result = new int[this.getSizeInventory()];
		for (int i = 0; i < this.getSizeInventory()-1; i++) {
			result[i] = i;
		}
		
		return result;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return true;
	}
	
	@Override
	public void calculateProgress() {
		this.progress = (int) ((this.energy/max)*100);
		if (this.progress > 100) {
			this.progress = 100;
		}
	}
}
