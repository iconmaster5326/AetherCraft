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

public class TileEntityAetherContainer extends AetherCraftTileEntity implements
		IInventory, IAetherStorage {

	public TileEntityAetherContainer() {
		energyBlockType = AetherCraft.GUI_ID_CONTAINER;
		inventory = new ItemStack[2];
	}

	@Override
	public String getInvName() {
		return "gra.tileentityec";
	}

	@Override
	public void handleAether() {
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
	public void calculateProgress() {
		this.progress = (int) ((float) this.energy
				/ (float) Float.parseFloat(AetherCraft
						.getOptions("acmaxstorage")) * 100.0f);
		if (this.progress > 100) {
			this.progress = 100;
		}
	}
}