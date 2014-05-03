package com.iconmaster.aec.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.network.DeviceSyncPacket;
import com.iconmaster.aec.network.RequestSyncPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Extend this to get a bunch of default AetherCraft TE functions.
 * @author iconmaster
 *
 */
public class AetherCraftTileEntity extends TileEntity implements
		ISidedInventory, IAetherStorage {
	public byte energyBlockType;

	protected ItemStack[] inventory;
	protected float energy;
	protected int progress;
	protected boolean powered;
	public float max;
	public float limit;
	
	protected boolean polled = false;
	/**
	 * Set this to false is you don't want the 0th slot to be considered as valid in getStackableSlot and getEmptySlot.
	 */
	protected boolean use0 = true;

	public AetherCraftTileEntity() {
		
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
	public String getInventoryName() {
		return "gra.tileentityem";
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) == this
				&& player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
						this.zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
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
		//TODO: what is second arg to getTagList??
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
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

	public void handleAether() {
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
		//TODO: handle sych packet
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			AetherCraft.packetHandler.sendToAll(new DeviceSyncPacket(this.xCoord,this.yCoord,this.zCoord,this.energy));
		/*
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
				PacketDispatcher.sendPacketToAllPlayers(packet);
			}
		}
		*/
	}

	public void recieveSync(float par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {

	}

	public int getProgress() {
		return this.progress;
	}

	public int getStackableSlot(ItemStack cStack) {
		ItemStack stack;
		if (cStack == null) {return 1;}
		for (int i = use0  ? 0 : 1; i < this.inventory.length; i++) {
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
		for (int i = use0 ? 0 : 1; i < this.inventory.length; i++) {
			stack = this.getStackInSlot(i);
			if (stack == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public float addAether(float ev) {
		if (ev==0) {return 0;}
		calcMax();
		if (this.energy + ev <= max) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			float rest = (this.energy + ev) - max;
			this.energy = max;
			this.sync();
			return rest;
		}
	}

	@Override
	public float extractAether(float av) {
		if (av==0 || energy==0) {return 0;}
		//System.out.println("Got "+av+" AV.");
		if (this.energy - av >= 0) {
			//System.out.println("Had enough AV. Returning "+av+" AV.");
			this.energy -= av;
			this.sync();
			return av;
		}
		float rest = this.energy;
		//System.out.println("Did not have enough AV. Returning "+rest+" AV.");
		this.energy = 0;
		this.sync();
		return rest;
	}
	
	@Override
	public float tryAddAether(float ev) {
		if (ev==0) {return 0;}
		calcMax();
		if (this.energy + ev <= max) {
			return 0;
		} else {
			float rest = (this.energy + ev) - max;
			return rest;
		}
	}

	@Override
	public float tryExtractAether(float av) {
		//System.out.println("Got "+av+" AV.");
		if (this.energy - av >= 0) {
			//System.out.println("Had enough AV. Returning "+av+" AV.");
			//this.energy -= av;
			//this.sync();
			return av;
		}
		float rest = this.energy;
		//System.out.println("Did not have enough AV. Returning "+rest+" AV.");
		//this.energy = 0;
		//this.sync();
		return rest;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return true;
	}
	
	public float getPossibleAether() {
		return getAether() + AetherNetwork.getStoredAV(worldObj, xCoord, yCoord, zCoord);
	}
	
	public boolean canConsume(ItemStack topStack, ItemStack currentStack) {
		calcLimit();
		if (currentStack == null) {
			return false;
		}
		if (topStack != null && currentStack.getItem() == topStack.getItem() && currentStack.getItemDamage() == topStack.getItemDamage()) {
			return false;
		}
		float av = AVRegistry.getAV(currentStack);
		if (av<=0 || av>limit) {
			return false;
		}
		return true;
	}
	public boolean canProduce(ItemStack currentItem) {
		calcLimit();
		float av = AVRegistry.getAV(currentItem);
		if (this.getStackableSlot(currentItem)<=0 && this.getEmptySlot() <=0) {return false;}
		return av>0 && av<=getPossibleAether() && av<=limit;
	}

	@Override
	public float getAether() {
		//System.out.println("Returning "+this.energy);
		if (!polled && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			requestSync();
			polled = true;
		}
		return this.energy;
	}

	public void requestSync() {
		//TODO: handle sync here
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			AetherCraft.packetHandler.sendToServer(new RequestSyncPacket(this.xCoord,this.yCoord,this.zCoord));
		/*
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
	
		try {
			outputStream.writeByte(this.energyBlockType);
			outputStream.writeInt(this.xCoord);
			outputStream.writeInt(this.yCoord);
			outputStream.writeInt(this.zCoord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "AecReq";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToServer(packet);
		*/
	}
	
	public int getMetadata() {
		return this.worldObj.getBlockMetadata(this.xCoord,this.yCoord,this.zCoord);
	}

	public void calcMax() {
		
	}
	
	public void calcLimit() {
		if (limit!=0) {return;}
		limit = (float) ((Float.parseFloat(AetherCraft.getOptions("avlimit")))*(Math.pow(2,getMetadata()*2)));
	}
}
