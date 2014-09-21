package com.iconmaster.aec.tileentity;

import com.iconmaster.aec.aether.AetherNetwork;
import com.iconmaster.aec.aether.IAetherStorage;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.PumpFacePacket;
import com.iconmaster.aec.util.SideUtils;
import com.iconmaster.aec.util.SideUtils.Offset;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

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
	public boolean handleAether() {
		getLimit();
		AetherNetwork.setCheckSelfMode(true);
		Offset out = new Offset(face);
		float need = getLimit()-AetherNetwork.canSendAV(worldObj, out.getOffsetX(xCoord), out.getOffsetY(yCoord), out.getOffsetZ(zCoord), getLimit());

		if (need>0) {
			float got = 0;
			for (int side : SideUtils.allSides) {
				if (side!=face) {
					Offset off = new Offset(side);
					got += AetherNetwork.requestAV(worldObj, off.getOffsetX(xCoord), off.getOffsetY(yCoord), off.getOffsetZ(zCoord), need-got);
				}
			}
			if (got>0) {
				AetherNetwork.sendAV(worldObj, out.getOffsetX(xCoord), out.getOffsetY(yCoord), out.getOffsetZ(zCoord), got);
			}
		}
		
		AetherNetwork.setCheckSelfMode(false);
		return false;
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
		
	}
	
	public void syncFace() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			AetherCraftPacketHandler.HANDLER.sendToAll(new PumpFacePacket(this.xCoord,this.yCoord,this.zCoord,this.face));
		}
	}
	
	@Override
	public float getLimit() {
		return super.getLimit()/8;
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