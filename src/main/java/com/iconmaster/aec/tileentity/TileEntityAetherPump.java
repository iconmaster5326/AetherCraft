package com.iconmaster.aec.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.PumpFacePacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityAetherPump extends AetherCraftTileEntity implements
		IInventory, IAetherStorage {
	public int face;

	public TileEntityAetherPump() {
		super();
		inventory = new ItemStack[0];
	}

	@Override
	public String getInventoryName() {
		return "aec.pump";
	}

	@Override
	public void handleAether() {
		//TODO
	}
	
	@Override
	public void calcMax() {

	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		face = tagCompound.getInteger("face");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("face", face);
	}
	
	@Override
	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			System.out.println("Sending face pkt");
			AetherCraftPacketHandler.HANDLER.sendToAll(new PumpFacePacket(this.xCoord,this.yCoord,this.zCoord,this.face));
		}
	}
	
    @Override
    public Packet getDescriptionPacket() 
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	this.writeToNBT(tagCompound);
    	return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
    	this.readFromNBT(pkt.func_148857_g());
    }
}