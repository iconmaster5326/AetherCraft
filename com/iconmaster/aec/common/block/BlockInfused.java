package com.iconmaster.aec.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.iconmaster.aec.aether.IAetherTransfer;
import com.iconmaster.aec.client.ClientProxy;
import com.iconmaster.aec.common.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockInfused extends Block {
	
	public Icon[] icons = new Icon[2];

	public BlockInfused(int id, Material material) {
		super(id, material);
        this.setHardness(1.0f);
        this.setUnlocalizedName("aec.aetherConduit");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        icons[0] = iconRegister.registerIcon("aec:infusedBlock");
        icons[1] = iconRegister.registerIcon("aec:infusedBrick");
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
}
