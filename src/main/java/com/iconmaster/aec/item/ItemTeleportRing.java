package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.TeleportRingPacket;
import com.iconmaster.aec.util.InventoryUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class ItemTeleportRing extends Item {
	public static class TeleportWaypoint {
		public int x;
		public int y;
		public int z;
		public int dim;

		public TeleportWaypoint(int x, int y, int z, int dim) {
			this.x =x ;
			this.y = y;
			this.z = z;
			this.dim = dim;
		}
	}
	
	public ItemTeleportRing() {
		super();
		this.setUnlocalizedName("aec.teleRing");
		this.setMaxStackSize(1);
		 this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("aec:itemTeleRing");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par1, float par2, float par3, float par4) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				//shift is down, set this as waypoint
				AetherCraftPacketHandler.HANDLER.sendToServer(new TeleportRingPacket(true));
			} else {
				//teleport to waypoint
				AetherCraftPacketHandler.HANDLER.sendToServer(new TeleportRingPacket(false));
			}
		}
		return true;
	}
	
	public static void setWaypoint(ItemStack stack, TeleportWaypoint waypoint) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag==null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		NBTTagCompound waytag = new NBTTagCompound();
		waytag.setInteger("x", waypoint.x);
		waytag.setInteger("y", waypoint.y);
		waytag.setInteger("z", waypoint.z);
		waytag.setInteger("dim", waypoint.dim);
		tag.setTag("waypoint", waytag);
	}
	
	public static TeleportWaypoint getWaypoint(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag==null) {
			return null;
		}
		
		NBTTagCompound waytag = tag.getCompoundTag("waypoint");
		if (waytag==null) {
			return null;
		}
		
		return new TeleportWaypoint(waytag.getInteger("x"),waytag.getInteger("y"),waytag.getInteger("z"),waytag.getInteger("dim"));
	}
	
	public static void registerWaypoint(ItemStack stack,EntityPlayer player) {
		setWaypoint(stack, new TeleportWaypoint((int)player.posX,(int)player.posY,(int)player.posZ,player.dimension));
		sendMessage(player,"Waypoint set!");
	}
	
	public static void teleportToWaypoint(ItemStack stack,EntityPlayer player) {
		TeleportWaypoint pos = getWaypoint(stack);
		if (player.worldObj.provider.dimensionId == pos.dim) {
			float cost = Float.parseFloat(AetherCraft.getOptions("telecost"));
			double dis = distance((int)player.posX,(int)player.posY,(int)player.posZ,pos.x,pos.y,pos.z);
			cost *= dis;
			
			float av = InventoryUtils.getAVInInventory(player.inventory);
			if (cost <= av) {
				InventoryUtils.drainAVFromInventory(player.inventory, cost);
				player.setPositionAndUpdate(pos.x, pos.y, pos.z);
				sendMessage(player,"Teleported across "+(int)dis+" blocks.");
			} else {
				sendMessage(player,"Not enough AV!");
			}
			
		} else {
			sendMessage(player,"Cannot teleport across dimensions!");
		}
	}
	
	public static void sendMessage(EntityPlayer player,String msg) {
		ChatComponentText cmc = new ChatComponentText(msg);
		player.addChatMessage(cmc);
	}
	
	public static double distance(int x1,int y1,int z1,int x2,int y2, int z2) {
		return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)+Math.pow(z2-z1, 2));
	}
}