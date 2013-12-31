package com.iconmaster.aec.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.client.ClientProxy;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAetherConduit extends Block implements IAetherTransfer {

	public Icon centerIcon;
	public Icon sideIcon;

	public BlockAetherConduit(int id, Material material) {
		super(id, material);
        this.setHardness(1.0f);
        this.setUnlocalizedName("aec.aetherConduit");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister
                         .registerIcon("aec:aetherConduitIcon");
        this.centerIcon = iconRegister
                .registerIcon("aec:aetherConduitCenter");
        this.sideIcon = iconRegister
                .registerIcon("aec:aetherConduitSide");
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
		return ClientProxy.conduitRenderType;
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

	public boolean canConnect(int blockId) {
		return (Block.blocksList [blockId] instanceof IAetherTransfer);
	}
	
	@Override
	public float getMaxTransferAV(World world, int x,int y,int z,int side) {
		return Float.MAX_VALUE;
	}
}
