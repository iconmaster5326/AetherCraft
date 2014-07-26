package com.iconmaster.aec.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.client.ClientProxy;
import com.iconmaster.aec.util.BlockTextureData;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetherConduit extends Block implements IAetherTransfer {

	public BlockTextureData[] icon;
	public IIcon[] blockIcons;

	public BlockAetherConduit(Material material) {
		super(material);
        this.setHardness(1.0f);
        this.setBlockName("aec.aetherConduit");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}

	@Override
	public boolean canTransferAV(World world, int x, int y, int z, int sideFrom) {
		return true;
	}
	
	@Override
    public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
    public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
    public int getRenderType()
	{
		return AetherCraft.proxy.conduitRenderType;
	}
	
	@Override
    public boolean canRenderInPass(int pass)
	{
		ClientProxy.renderPass = pass;
		return true;
	}
	
	@Override
    public int getRenderBlockPass()
	{
            return 1;
	}

	public boolean canConnect(Block block) {
		return (block instanceof IAetherTransfer);
	}
	
	@Override
	public float getMaxTransferAV(World world, int x,int y,int z,int side) {
		int meta = world.getBlockMetadata(x, y, z);
		return (float) ((Float.parseFloat(AetherCraft.getOptions("flowrate")))*(Math.pow(2,meta*2)));
	}

	@Override
	public int damageDropped(int par1)
	{
	    return par1;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcons = new IIcon[3];
		blockIcons[0] = iconRegister.registerIcon("aec:aetherConduitIcon");
		blockIcons[1] = iconRegister.registerIcon("aec:aetherConduitInfusedIcon");
		blockIcons[2] = iconRegister.registerIcon("aec:aetherConduitGildedIcon");
		icon = new BlockTextureData[3];
		icon[0] = new BlockTextureData(iconRegister.registerIcon("aec:aetherConduitCenter"),iconRegister.registerIcon("aec:aetherConduitSide"),iconRegister.registerIcon("aec:aetherConduitCenter"));
		icon[1] = new BlockTextureData(iconRegister.registerIcon("aec:aetherConduitInfusedCenter"),iconRegister.registerIcon("aec:aetherConduitInfusedSide"),iconRegister.registerIcon("aec:aetherConduitInfusedCenter"));
		icon[2] = new BlockTextureData(iconRegister.registerIcon("aec:aetherConduitGildedCenter"),iconRegister.registerIcon("aec:aetherConduitGildedSide"),iconRegister.registerIcon("aec:aetherConduitGildedCenter"));
	}
	
	@Override
	public IIcon getIcon(int side,int meta) {
		return blockIcons[meta];
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this,1,world.getBlockMetadata(x, y, z));
	}
}
