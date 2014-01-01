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

public class TileEntityAetherInfuser extends AetherCraftTileEntity implements
		ISidedInventory, IAetherStorage {
	public TileEntityAetherInfuser() {
		energyBlockType = AetherCraft.GUI_ID_INFUSER;
		max = Float.parseFloat(AetherCraft.getOptions("ammaxstorage"))/2;
		inventory = new ItemStack[2];
	}

	@Override
	public String getInvName() {
		return "aec.infuser";
	}

	@Override
	public void handleAether() {

	}

	@Override
	public void calculateProgress() {
		this.progress = (int) ((this.getAether()/max)*100);
		if (this.progress > 100) {
			this.progress = 100;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 1:
			return new int[] {1};
		default:
			return new int[] {0};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot == 0;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 1;
	}
}
