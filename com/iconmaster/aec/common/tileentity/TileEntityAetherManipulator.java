package com.iconmaster.aec.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.IAetherContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLPacket;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherManipulator extends TileEntity implements
		ISidedInventory, IAetherContainer {
	public static final byte energyBlockType = 0;

	private ItemStack[] inventory;
	private int energy;
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

		this.energy = tagCompound.getInteger("Energy");
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
		tagCompound.setInteger("Energy", this.energy);
	}

	private void handleEnergy() {
		int batteryMaxStorage = Integer.parseInt(AetherCraft
				.getOptions("ebatterymaxstorage"));
		int emMaxStorage = Integer.parseInt(AetherCraft
				.getOptions("emmaxstorage"));

		ItemStack topStack = this.getStackInSlot(0);
		ItemStack currentStack;
		boolean doneSomething = false;
		for (int i = 1; i < this.getSizeInventory(); i++) {
			currentStack = this.getStackInSlot(i);

			// ------------------- Consuming -------------------
			if (currentStack != null) {
				if (topStack != null) {
					if ((AetherCraft.getAetherValueByItemStack(topStack) > 0 || topStack.itemID == AetherCraft.itemAetherBattery.itemID)
							&& AetherCraft
									.getAetherValueByItemStack(currentStack) > 0
							&& ((currentStack.itemID != AetherCraft.itemAetherBattery.itemID && currentStack.itemID != topStack.itemID) || (currentStack
									.getHasSubtypes()
									&& topStack.getHasSubtypes() && currentStack
									.getItemDamage() != topStack
									.getItemDamage()))) {
						int stackEv = AetherCraft
								.getAetherValueByItemStack(currentStack);
						if ((stackEv
								* ((float) Integer.parseInt(AetherCraft
										.getOptions("consumeprecission"))) / 100.0f) <= this
									.getFreeEnergySpace()) {
							int ev = (int) (((float) stackEv)
									* ((float) Integer
											.parseInt(AetherCraft
													.getOptions("consumeprecission"))) / 100.0f);
							this.addEnergyToConnectedAndSelf(ev);
							this.decrStackSize(i, 1);
							doneSomething = true;
						}
					}
				} else {
					if (AetherCraft
							.getAetherValueByItemStack(currentStack) > 0
							&& currentStack.itemID != AetherCraft.itemAetherBattery.itemID) {
						int stackEv = AetherCraft
								.getAetherValueByItemStack(currentStack);
						if ((stackEv
								* ((float) Integer.parseInt(AetherCraft
										.getOptions("consumeprecission"))) / 100.0f) <= this
									.getFreeEnergySpace()) {
							int ev = (int) (((float) stackEv)
									* ((float) Integer
											.parseInt(AetherCraft
													.getOptions("consumeprecission"))) / 100.0f);
							this.addEnergyToConnectedAndSelf(ev);
							this.decrStackSize(i, 1);
							doneSomething = true;
						}
					}
				}
			}
			// ------------------- Transmuting -------------------
			if (topStack != null
					&& AetherCraft.getAetherValueByItemStack(topStack) > 0
					&& this.getCombinedEnergy() >= AetherCraft
							.getAetherValueByItemStack(topStack)
					&& topStack.itemID != AetherCraft.itemAetherBattery.itemID) {
				int slot = this.getStackableSlot(topStack);

				if (slot > 0) {
					ItemStack newStack = this.getStackInSlot(slot);
					newStack.stackSize++;
					this.extractEnergyFromConnectedAndSelf(AetherCraft
							.getAetherValueByItemStack(topStack));
					doneSomething = true;
				} else {
					slot = this.getEmptySlot();

					if (slot > 0) {
						ItemStack newStack = topStack.copy();
						newStack.stackSize = 1;
						this.setInventorySlotContents(slot, newStack);
						this.extractEnergyFromConnectedAndSelf(AetherCraft
								.getAetherValueByItemStack(topStack));
						doneSomething = true;
					}
				}
			}
			if (this.energy > 0 && !this.isAllConnectedECsFilled()) {
				this.transferEnergyFromSelfToConnected();
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

	public boolean spaceForEnergy(int ev) {
		return this.getFreeEnergySpace() - ev >= 0;
	}

	public int getTotalEnergySpace() {
		int emmaxstorage = Integer.parseInt(AetherCraft
				.getOptions("emmaxstorage"));
		int ecmaxstorage = Integer.parseInt(AetherCraft
				.getOptions("ecmaxstorage"));
		int totalSpace = emmaxstorage;
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				totalSpace += ecmaxstorage;
			}
		}
		return totalSpace;
	}

	public int getFreeEnergySpace() {
		return this.getTotalEnergySpace() - this.getCombinedEnergy();
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
			handleEnergy();
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
				outputStream.writeInt(this.energy);
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

	public void recieveSync(int par1energy) {
		this.energy = par1energy;
	}

	public void calculateProgress() {
		ItemStack stack = this.getStackInSlot(0);

		if (stack != null) {
			this.progress = (int) ((float) this.getCombinedEnergy()
					/ (float) AetherCraft
							.getAetherValueByItemStack(stack) * 100.0f);

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
									this.zCoord)).getEnergy() >= ev) {
						return 0;
					}
					break;
				// Top
				case 1:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord + 1,
									this.zCoord)).getEnergy() >= ev) {
						return 1;
					}
					break;
				// North
				case 2:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord - 1)).getEnergy() >= ev) {
						return 2;
					}
					break;
				// South
				case 3:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord + 1)).getEnergy() >= ev) {
						return 3;
					}
					break;
				// West
				case 4:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord - 1, this.yCoord,
									this.zCoord)).getEnergy() >= ev) {
						return 4;
					}
					break;
				// East
				case 5:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord + 1, this.yCoord,
									this.zCoord)).getEnergy() >= ev) {
						return 5;
					}
					break;
				}
			}
		}
		return -1;
	}

	private int getConnectedECWithAVLowerOrEqual(int ev) {
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				switch (i) {
				// Bottom
				case 0:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord - 1,
									this.zCoord)).getEnergy() <= ev) {
						return 0;
					}
					break;
				// Top
				case 1:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord + 1,
									this.zCoord)).getEnergy() <= ev) {
						return 1;
					}
					break;
				// North
				case 2:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord - 1)).getEnergy() <= ev) {
						return 2;
					}
					break;
				// South
				case 3:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord, this.yCoord,
									this.zCoord + 1)).getEnergy() <= ev) {
						return 3;
					}
					break;
				// West
				case 4:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord - 1, this.yCoord,
									this.zCoord)).getEnergy() <= ev) {
						return 4;
					}
					break;
				// East
				case 5:
					if (((TileEntityAetherContainer) this.worldObj
							.getBlockTileEntity(this.xCoord + 1, this.yCoord,
									this.zCoord)).getEnergy() <= ev) {
						return 5;
					}
					break;
				}
			}
		}
		return -1;
	}

	private int getConnectedECEnergy(int side) {
		switch (side) {
		// Bottom
		case 0:
			return this.worldObj.getBlockId(this.xCoord, this.yCoord - 1,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord - 1,
							this.zCoord)).getEnergy() : 0;
			// Top
		case 1:
			return this.worldObj.getBlockId(this.xCoord, this.yCoord + 1,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord + 1,
							this.zCoord)).getEnergy() : 0;
			// North
		case 2:
			return this.worldObj.getBlockId(this.xCoord, this.yCoord,
					this.zCoord - 1) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord,
							this.zCoord - 1)).getEnergy() : 0;
			// South
		case 3:
			return this.worldObj.getBlockId(this.xCoord, this.yCoord,
					this.zCoord + 1) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord, this.yCoord,
							this.zCoord + 1)).getEnergy() : 0;
			// West
		case 4:
			return this.worldObj.getBlockId(this.xCoord - 1, this.yCoord,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord - 1, this.yCoord,
							this.zCoord)).getEnergy() : 0;
			// East
		case 5:
			return this.worldObj.getBlockId(this.xCoord + 1, this.yCoord,
					this.zCoord) == AetherCraft.blockAetherContainer.blockID ? ((TileEntityAetherContainer) this.worldObj
					.getBlockTileEntity(this.xCoord + 1, this.yCoord,
							this.zCoord)).getEnergy() : 0;
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

	public int addEnergyToConnectedECs(int ev) {
		int rest = ev;
		TileEntityAetherContainer te;
		int ec;
		while (true) {
			ec = this
					.getConnectedECWithAVLowerOrEqual(Integer
							.parseInt(AetherCraft
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
					rest -= rest - te.addEnergy(rest);
				}
				break;
			// Top
			case 1:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord + 1,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addEnergy(rest);
				}
				break;
			// North
			case 2:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord - 1);
				if (te != null) {
					rest -= rest - te.addEnergy(rest);
				}
				break;
			// South
			case 3:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord + 1);
				if (te != null) {
					rest -= rest - te.addEnergy(rest);
				}
				break;
			// West
			case 4:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord - 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addEnergy(rest);
				}
				break;
			// East
			case 5:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord + 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					rest -= rest - te.addEnergy(rest);
				}
				break;
			}
		}
	}

	public int extractEnergyFromConnectedECs(int ev) {
		int extracted = 0;
		TileEntityAetherContainer te;
		int ec;
		while (true) {
			ec = this.getConnectedECWithAVHigherOrEqual(1);
			if (ec == -1 || extracted >= ev) {
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
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			// Top
			case 1:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord + 1,
								this.zCoord);
				if (te != null) {
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			// North
			case 2:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord - 1);
				if (te != null) {
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			// South
			case 3:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord, this.yCoord,
								this.zCoord + 1);
				if (te != null) {
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			// West
			case 4:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord - 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			// East
			case 5:
				te = (TileEntityAetherContainer) this.worldObj
						.getBlockTileEntity(this.xCoord + 1, this.yCoord,
								this.zCoord);
				if (te != null) {
					extracted += te.extractEnergy(ev - extracted);
				}
				break;
			}
		}
	}

	public int addEnergyToConnectedAndSelf(int ev) {
		int rest = this.addEnergyToConnectedECs(ev);
		if (rest > 0) {
			rest = this.addEnergy(rest);
		}
		this.sync();
		return rest;
	}

	public int extractEnergyFromConnectedAndSelf(int ev) {
		int extracted = this.extractEnergyFromConnectedECs(ev);
		if (extracted < ev) {
			extracted += this.extractEnergy(ev - extracted);
		}
		this.sync();
		return extracted;
	}

	/*
	 * Convenience method
	 */
	public void transferEnergyFromSelfToConnected() {
		this.energy = this.addEnergyToConnectedECs(this.energy);
		this.sync();
	}

	@Override
	public int addEnergy(int ev) {
		int emmaxstorage = Integer.parseInt(AetherCraft
				.getOptions("emmaxstorage"));
		if (this.energy + ev <= emmaxstorage) {
			this.energy += ev;
			this.sync();
			return 0;
		} else {
			int rest = emmaxstorage - this.energy;
			this.energy = emmaxstorage;
			this.sync();
			return rest;
		}
	}

	@Override
	public int extractEnergy(int ev) {
		if (this.energy - ev >= 0) {
			this.energy -= ev;
			this.sync();
			return ev;
		}
		int rest = ev - this.energy;
		this.energy = 0;
		this.sync();
		return rest;
	}

	@Override
	public void setEnergy(int ev) {
		this.energy = ev;
		this.sync();
	}

	@Override
	public int getEnergy() {
		return this.energy;
	}

	public int getConnectedEnergy() {
		return this.getConnectedECEnergy(0) + this.getConnectedECEnergy(1)
				+ this.getConnectedECEnergy(2) + this.getConnectedECEnergy(3)
				+ this.getConnectedECEnergy(4) + this.getConnectedECEnergy(5);
	}

	public int getCombinedEnergy() {
		return this.getEnergy() + this.getConnectedEnergy();
	}

	public boolean isAllConnectedECsFilled() {
		short sides = 0;
		for (int i = 0; i < this.connectedSides.length; i++) {
			if (this.connectedSides[i]) {
				sides++;
			}
		}
		int ecMaxStorage = Integer.parseInt(AetherCraft
				.getOptions("ecmaxstorage"));
		return this.getConnectedEnergy() >= (sides * ecMaxStorage);
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
