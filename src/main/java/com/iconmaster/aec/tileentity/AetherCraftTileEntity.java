package com.iconmaster.aec.tileentity;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.DeviceSyncPacket;
import com.iconmaster.aec.network.RequestSyncPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

/**
 * Extend this to get a bunch of default AetherCraft TE functions.
 * @author iconmaster
 *
 */
public class AetherCraftTileEntity extends TileEntity implements ISidedInventory, IAetherStorage {

	protected ItemStack[] inventory;
	protected float energy;
	protected boolean polled = false;
	/**
	 * Set this to false is you don't want the 0th slot to be considered as valid in getStackableSlot and getEmptySlot.
	 */
	protected boolean use0 = true;
	private int tickCount = 0;
	private boolean dirty = false;

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

	public boolean handleAether() {
		return false;
	}

	@Override
	public void updateEntity() {
		tickCount ++;
		int ticks = Integer.parseInt(AetherCraft.getOptions("ticksperop"));
		if (tickCount%ticks==0 && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			boolean doSync = false;
			for (int i=0;i<ticks;i++) {
				boolean result = handleAether();
				doSync = doSync || result;
			}
			if (doSync || dirty) {
				sync();
				dirty = false;
			}
		}
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			AetherCraftPacketHandler.HANDLER.sendToAllAround(new DeviceSyncPacket(this.xCoord,this.yCoord,this.zCoord,this.energy), new TargetPoint(worldObj.provider.dimensionId,xCoord,yCoord,zCoord,8));
	}

	public void recieveSync(float par1energy) {
		this.energy = par1energy;
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
		getMax();
		if (this.energy + ev <= getMax()) {
			this.energy += ev;
			this.markDeviceDirty();
			return 0;
		} else {
			float rest = (this.energy + ev) - getMax();
			this.energy = getMax();
			this.markDeviceDirty();
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
			this.markDeviceDirty();
			return av;
		}
		float rest = this.energy;
		//System.out.println("Did not have enough AV. Returning "+rest+" AV.");
		this.energy = 0;
		this.markDeviceDirty();
		return rest;
	}
	
	@Override
	public float tryAddAether(float ev) {
		if (ev==0) {return 0;}
		getMax();
		if (this.energy + ev <= getMax()) {
			return 0;
		} else {
			float rest = (this.energy + ev) - getMax();
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
		if (Boolean.parseBoolean(AetherCraft.getOptions("cobblehack")) && currentStack!=null && Block.getBlockFromItem(currentStack.getItem())==Blocks.cobblestone) {
			return false;
		}
		getLimit();
		if (currentStack == null) {
			return false;
		}
		if (topStack != null && currentStack.getItem() == topStack.getItem() && currentStack.getItemDamage() == topStack.getItemDamage()) {
			return false;
		}
		float av = AVRegistry.getAV(currentStack);
		if (av<=0 || av>getLimit()) {
			return false;
		}
		return true;
	}
	public boolean canProduce(ItemStack currentItem) {
		getLimit();
		float av = AVRegistry.getAV(currentItem);
		if (this.getStackableSlot(currentItem)<=0 && this.getEmptySlot() <=0) {return false;}
		return av>0 && av<=getPossibleAether() && av<=getLimit();
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
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			AetherCraftPacketHandler.HANDLER.sendToServer(new RequestSyncPacket(this.xCoord,this.yCoord,this.zCoord));
	}
	
	public int getMetadata() {
		return this.worldObj.getBlockMetadata(this.xCoord,this.yCoord,this.zCoord);
	}

	public float getMax() {
		return 0;
	}
	
	public float getLimit() {
		return (float) ((Float.parseFloat(AetherCraft.getOptions("avlimit")))*(Math.pow(2,getMetadata()*2)));
	}
	
	public void markDeviceDirty() {
		dirty = true;
	}
}
