package com.iconmaster.aec.block;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockInfused extends Block {
	
	public IIcon[] icons = new IIcon[6];

	public BlockInfused(Material material) {
		super(material);
        this.setHardness(1.0f);
        this.setBlockName("aec.blockInfused");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        icons[0] = iconRegister.registerIcon("aec:infusedBlock");
        icons[1] = iconRegister.registerIcon("aec:InfusedBrick");
        icons[2] = iconRegister.registerIcon("aec:gildedBlock");
        icons[3] = iconRegister.registerIcon("aec:gildedBrick");
        icons[4] = iconRegister.registerIcon("aec:eternalBlock");
        icons[5] = iconRegister.registerIcon("aec:eternalBrick");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side,int meta) {
    	return icons[meta];
    }
    
    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this,1,world.getBlockMetadata(x, y, z));
	}
}
