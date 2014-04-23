package com.iconmaster.aec.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.client.ClientProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockInfused extends Block {
	
	public Icon[] icons = new Icon[4];

	public BlockInfused(int id, Material material) {
		super(id, material);
        this.setHardness(1.0f);
        this.setUnlocalizedName("aec.blockInfused");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        icons[0] = iconRegister.registerIcon("aec:infusedBlock");
        icons[1] = iconRegister.registerIcon("aec:InfusedBrick");
        icons[2] = iconRegister.registerIcon("aec:gildedBlock");
        icons[3] = iconRegister.registerIcon("aec:gildedBrick");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side,int meta) {
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