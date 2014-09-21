package com.iconmaster.aec.block;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.client.particle.AetherFlameFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAetherFlame extends Block {
	
	public BlockAetherFlame(Material material) {
		super(material);
		this.setLightLevel(1);
		this.setLightOpacity(1);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        this.setBlockName("aec.flame");
        this.setBlockBounds(.3F, .3F, .3F, .6F, .6F, .6F);
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("aec:aetherFlame");
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return AetherCraft.proxy.flameRenderType;
	}
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		AetherFlameFX.spawnFX(Minecraft.getMinecraft().theWorld, x+.5, y+.5, z+.5, random.nextFloat()/8-.0625, random.nextFloat()/8-.0625, random.nextFloat()/8-.0625);
	}
}
