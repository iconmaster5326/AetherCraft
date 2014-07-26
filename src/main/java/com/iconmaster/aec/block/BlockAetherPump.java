package com.iconmaster.aec.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.tileentity.TileEntityAetherPump;
import com.iconmaster.aec.util.SideUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetherPump extends AetherCraftBlock implements IAetherTransfer {
	public IIcon[] frontIcon = new IIcon[3];
	public IIcon[] sideIcon = new IIcon[3];
	
	public BlockAetherPump(Material material,String name) {
		super(material,name);
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
		if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT) {return true;}
		TileEntityAetherPump te = (TileEntityAetherPump) world.getTileEntity(x, y, z);
		te.face+=1;
		if (te.face==6) {te.face=0;}
		te.syncFace();
        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAetherPump();
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.frontIcon[0] = register.registerIcon("aec:aetherPump");
		this.frontIcon[1] = register.registerIcon("aec:aetherPumpInfused");
		this.frontIcon[2] = register.registerIcon("aec:aetherPumpGilded");
		
		this.sideIcon[0] = register.registerIcon("aec:deviceBottom");
		this.sideIcon[1] = register.registerIcon("aec:deviceInfusedBottom");
		this.sideIcon[2] = register.registerIcon("aec:deviceGildedBottom");
	}
    
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if (side == 3) {
			return this.frontIcon[meta];
		} else {
			return this.sideIcon[meta];
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT) {return;}
		if (!(entity instanceof EntityPlayer)) {return;}
		//int i = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int i = SideUtils.getBlockHitSide(world, (EntityPlayer)entity, 8D);
		
		TileEntityAetherPump te = (TileEntityAetherPump) world.getTileEntity(x, y, z);
		
		te.face = i;
		System.out.println(FMLCommonHandler.instance().getEffectiveSide()+": "+te.face);
		te.syncFace();
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return AetherCraft.proxy.pumpRenderType;
	}

	@Override
	public boolean canTransferAV(World world, int x, int y, int z, int sideFrom) {
//		TileEntityAetherPump te = (TileEntityAetherPump) world.getTileEntity(x, y, z);
//		return te.face==SideUtils.getOppositeSide(sideFrom);
		return false;
	}
	
	@Override
	public float getMaxTransferAV(World world, int x,int y,int z,int side) {
//		return super.getMaxTransferAV(world, x, y, z, side)/4;
		return 0;
	}
}
