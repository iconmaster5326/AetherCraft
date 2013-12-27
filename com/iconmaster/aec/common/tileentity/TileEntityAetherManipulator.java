package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.aether.IAetherContainer;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherManipulator extends TileEntity implements
		ISidedInventory, IAetherContainer {
	public static final byte energyBlockType = 0;

	private ItemStack[] inventory;
	private float energy;
	private int progress;
	private boolean powered;

	// 0 = Bottom, 1 = Top, 2 = North, 3 = South, 4 = East, 5 = West
	private boolean connectedSides[];

	public TileEntityAetherManipulator() {
		inventory = new ItemStack[55];
		connectedSides = new boolean[6];
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
	public String getInvName() {
		return "gra.tileentityem";
	}

	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) == this
				&& player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
						this.zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
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
		NBTTagList tagList = tagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
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

	private void handleAether() {
		float batteryMaxStorage = Float.parseFloat(AetherCraft
				.getOptions("ebatterymaxstorage"));
		float emMaxStorage = Float.parseFloat(AetherCraft
				.getOptions("emmaxstorage"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 1; i < this.getSizeInventory(); i++) {
			currentStack = this.getStackInSlot(i);

			// ------------------- Consuming -------------------
			if (currentStack != null) {
				if (topStack != null) {
					if ((AVRegistry.getAV(topStack) > 0 || topStack.itemID == AetherCraft.itemAetherBattery.itemID)
							&& AVRegistry.getAV(currentStack) > 0
							&& ((currentStack.itemID != AetherCraft.itemAetherBattery.itemID && currentStack.itemID != topStack.itemID) || (currentStack
									.getHasSubtypes()
									&& topStack.getHasSubtypes() && currentStack
									.getItemDamage() != topStack
									.getItemDamage()))) {
						float stackEv = AVRegistry.getAV(currentStack);
						if ((stackEv
								* ((float) Float.parseFloat(AetherCraft
										.getOptions("consumeprecission"))) / 100.0f) <= this
									.getFreeAetherSpace()) {
							float ev = (float) (((float) stackEv)
									* ((float) Float
											.parseFloat(AetherCraft
													.getOptions("consumeprecission"))) / 100.0f);
							//System.out.println("Consuming "+stackEv+" and getting "+ev);
							this.addAetherToConnectedAndSelf(ev);
							this.decrStackSize(i, 1);
							doneSomething = true;
						}
					}
				} else {
					if (AVRegistry.getAV(currentStack) > 0
							&& currentStack.itemID != AetherCraft.itemAetherBattery.itemID) {
						float stackEv = AVRegistry.getAV(currentStack);
						if ((stackEv
								* ((float) Float.parseFloat(AetherCraft
										.getOptions("consumeprecission"))) / 100.0f) <= this
									.getFreeAetherSpace()) {
							float ev = (((float) stackEv)
									* ((float) Float
											.parseFloat(AetherCraft
													.getOptions("consumeprecission"))) / 100.0f);
							//System.out.println("Consuming "+stackEv+" and getting "+ev);
							this.addAetherToConnectedAndSelf(ev);
							this.decrStackSize(i, 1);
							doneSomething = true;
						}
					}
				}
			}
			// ------------------- Transmuting -------------------
			if (topStack != null
					&& AVRegistry.getAV(topStack) > 0
					&& this.getCombinedAether() >= AVRegistry.getAV(topStack)
					&& topStack.itemID != AetherCraft.itemAetherBattery.itemID) {
				int slot = this.getStackableSlot(topStack);

				if (slot > 0) {
					ItemStack newStack = this.getStackInSlot(slot);
					newStack.stackSize++;
					this.extractAetherFromConnectedAndSelf(AVRegistry.getAV(topStack));
					doneSomething = true;
				} else {
					slot = this.getEmptySlot();

					if (slot > 0) {
						ItemStack newStack = new ItemStack(topStack.itemID,1,topStack.getItemDamage());
						//newStack.stackSize = 1;
						this.setInventorySlotContents(slot, newStack);
						this.extractAetherFromConnectedAndSelf(AVRegistry.getAV(topStack));
						doneSomething = true;
					}
				}
			}
			if (this.energy > 0 && !this.isAllConnectedECsFilled()) {
				this.transferAetherFromSelfToConnected();
			}
			if (!Boolean.parseBoolean(AetherCraft
					.getOptions("instantconsume")) && i >= 9 && doneSomething) {
				this.sync();
				return;
			}
		}
		if (doneSomething) {
			this.sync();
			return;
		}
	}

	public boolean spaceForAether(float ev) {
		return this.getFreeAetherSpace() - ev >= 0;
	}

	public float getTotalAetherSpace() {
		float emmaxstorage = Float.parseFloat(AetherCraft
				.getOptions("emmaxstorage"));
		float ecmaxstorage = Float.parseFloat(AetherCraft
				.getOptions("ecmaxstorage"));
		float totalSpace = emmaxstorage;
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				totalSpace += ecmaxstorage;
			}
		}
		return totalSpace;
	}

	public float getFreeAetherSpace() {
		return this.getTotalAetherSpace() - this.getCombinedAether();
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
				PacketDispatcher.sendPacketToAllAround(this.xCoord,
						this.yCoord, this.zCoord, 7,
						this.worldObj.provider.dimensionId, packet);
			}
		}
	}

	public void recieveSync(float par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {
		ItemStack stack = this.getStackInSlot(0);

		if (stack != null) {
			this.progress = (int) ((float) this.getCombinedAether()
					/ (float) AVRegistry.getAV(stack) * 100.0f);

			if (this.progress > 100) {
				this.progress = 100;
			}
		} else {
			this.progress = 0;
		}
	}

	public int getProgress() {
		return this.progress;
	}

	public int getStackableSlot(ItemStack cStack) {
		ItemStack stack;
		for (int i = 1; i < this.inventory.length; i++) {
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
		for (int i = 1; i < this.inventory.length; i++) {
			stack = this.getStackInSlot(i);
			if (stack == null) {
				return i;
			}
		}
		return -1;
	}

	private int getConnectedECWithAVHigherOrEqual(int ev) {
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				switch (i) {
				// Bottom
				case 0:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord - 1,
									this.zCoord)).getAether() >= ev) {
						return 0;
					}
					break;
				// Top
				case 1:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord + 1,
									this.zCoord)).getAether() >= ev) {
						return 1;
					}
					break;
				// North
				case 2:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord - 1)).getAether() >= ev) {
						return 2;
					}
					break;
				// South
				case 3:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord + 1)).getAether() >= ev) {
						return 3;
					}
					break;
				// West
				case 4:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord - 1, this.yCoord,
									this.zCoord)).getAether() >= ev) {
						return 4;
					}
					break;
				// East
				case 5:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord + 1, this.yCoord,
									this.zCoord)).getAether() >= ev) {
						return 5;
					}
					break;
				}
			}
		}
		return -1;
	}

	private int getConnectedECWithAVLowerOrEqual(float l) {
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				switch (i) {
				// Bottom
				case 0:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord - 1,
									this.zCoord)).getAether() <= l) {
						return 0;
					}
					break;
				// Top
				case 1:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord + 1,
									this.zCoord)).getAether() <= l) {
						return 1;
					}
					break;
				// North
				case 2:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord - 1)).getAether() <= l) {
						return 2;
					}
					break;
				// South
				case 3:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord + 1)).getAether() <= l) {
						return 3;
					}
					break;
				// West
				case 4:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord - 1, this.yCoord,
									this.zCoord)).getAether() <= l) {
						return 4;
					}
					break;
				// East
				case 5:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord + 1, this.yCoord,
									this.zCoord)).getAether() <= l) {
						return 5;
					}
					break;
				}
			}
		}
		return -1;
	}

	private float getConnectedECAether(int side) {
		switch (side) {
		// Bottom
		case 0:
			return (this.worldObj.getBlockId(this.xCoord, this.yCoord - 1,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord - 1,
							this.zCoord)).getAether() : 0);
			// Top
		case 1:
			return (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord + 1,
							this.zCoord)).getAether() : 0);
			// North
		case 2:
			return (this.worldObj.getBlockId(this.xCoord, this.yCoord,
					this.zCoord - 1) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord,
							this.zCoord - 1)).getAether() : 0);
			// South
		case 3:
			return (this.worldObj.getBlockId(this.xCoord, this.yCoord,
					this.zCoord + 1) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord,
							this.zCoord + 1)).getAether() : 0);
			// West
		case 4:
			return (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord - 1, this.yCoord,
							this.zCoord)).getAether() : 0);
			// East
		case 5:
			return (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord + 1, this.yCoord,
							this.zCoord)).getAether() : 0);
		default:
			return 0;
		}
	}

	public void updateConnectedSides() {
		// Bottom
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[0] = true;
		} else {
			this.connectedSides[0] = false;
		}
		// Top
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[1] = true;
		} else {
			this.connectedSides[1] = false;
		}
		// North
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[2] = true;
		} else {
			this.connectedSides[2] = false;
		}
		// South
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[3] = true;
		} else {
			this.connectedSides[3] = false;
		}
		// West
		if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[4] = true;
		} else {
			this.connectedSides[4] = false;
		}
		// East
		if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == AetherCraft.blockAetherContainer.blockID) {
			this.connectedSides[5] = true;
		} else {
			this.connectedSides[5] = false;
		}
	}

	public boolean[] getConnectedSides() {
		return this.connectedSides;
	}

	public boolean getConnectedSide(int side) {
		return this.connectedSides[side];
	}

	public void setConnectedSides(boolean[] cs) {
		if (cs != null && cs.length == 6) {
			this.connectedSides = cs;
		}
	}

	public void setConnectedSide(boolean connected, int side) {
		this.connectedSides[side] = connected;
	}

	public float addAetherToConnectedECs(float energy2) {
		float rest = energy2;
		TileEntityAetherContainer te;
		int ec;
		while (true) {
			ec = this
					.getConnectedECWithAVLowerOrEqual(Float.parseFloat(AetherCraft
									.getOptions("ecmaxstorage")) - 1);
			if (ec == -1 || rest <= 0) {
				this.sync();
				return rest;
			}
			switch (ec) {
			// Bottom
			case 0:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord - 1,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			// Top
			case 1:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord + 1,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			// North
			case 2:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord - 1);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			// South
			case 3:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord + 1);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			// West
			case 4:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord - 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			// East
			case 5:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord + 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addAether(rest);
				}
				break;
			}
		}
	}

	public float extractAetherFromConnectedECs(float l) {
		float extracted = 0;
		TileEntityAetherContainer te;
		int ec;
		while (true) {
			ec = this.getConnectedECWithAVHigherOrEqual(1);
			if (ec == -1 || extracted >= l) {
				this.sync();
				return extracted;
			}
			switch (ec) {
			// Bottom
			case 0:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord - 1,
								this.zCoord);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			// Top
			case 1:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord + 1,
								this.zCoord);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			// North
			case 2:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord - 1);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			// South
			case 3:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord + 1);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			// West
			case 4:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord - 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			// East
			case 5:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord + 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					extracted += te.extractAether(l - extracted);
				}
				break;
			}
		}
	}

	public float addAetherToConnectedAndSelf(float ev) {
		float rest = this.addAetherToConnectedECs(ev);
		if (rest > 0) {
			rest = this.addAether(rest);
		}
		this.sync();
		return rest;
	}

	public float extractAetherFromConnectedAndSelf(float l) {
		float extracted = this.extractAetherFromConnectedECs(l);
		if (extracted < l) {
			extracted += this.extractAether(l - extracted);
		}
		this.sync();
		return extracted;
	}

	/*
	 * Convenience method
	 */
	public void transferAetherFromSelfToConnected() {
		this.energy = this.addAetherToConnectedECs(this.energy);
		this.sync();
	}

	@Override
	public float addAether(float ev) {
		float emmaxstorage = Float.parseFloat(AetherCraft
				.getOptions("emmaxstorage"));
		if (this.energy + ev <= emmaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			float rest = emmaxstorage - this.energy;
			this.energy = emmaxstorage;
			this.sync();
			return rest;
		}
	}

	@Override
	public float extractAether(float l) {
		if (this.energy - l >= 0) {
			this.energy -= l;
			this.sync();
			return l;
		}
		float rest = l - this.energy;
		this.energy = 0;
		this.sync();
		return rest;
	}

	@Override
	public void setAether(float ev) {
		this.energy = ev;
		this.sync();
	}

	@Override
	public float getAether() {
		return this.energy;
	}

	public float getConnectedAether() {
		return this.getConnectedECAether(0) + this.getConnectedECAether(1)
				+ this.getConnectedECAether(2) + this.getConnectedECAether(3)
				+ this.getConnectedECAether(4) + this.getConnectedECAether(5);
	}

	public float getCombinedAether() {
		return this.getAether() + this.getConnectedAether();
	}

	public boolean isAllConnectedECsFilled() {
		short sides = 0;
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				sides++;
			}
		}
		float ecMaxStorage = Float.parseFloat(AetherCraft
				.getOptions("ecmaxstorage"));
		return this.getConnectedAether() >= (sides * ecMaxStorage);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] result;
		switch (side) {
		// Bottom
		case 0:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
			// Top
		case 1:
			return new int[] { 0 };
			// Sides
		default:
			result = new int[this.getSizeInventory() - 1];
			for (int i = 1; i < this.getSizeInventory(); i++) {
				result[i - 1] = i;
			}
			return result;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (slot == 0) {
			if (side == 1) {
				return true;
			}
		} else if (slot > 0) {
			if (side != 1) {
				return true;
			}
		}
		return false;
	}
}
