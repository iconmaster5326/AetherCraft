package com.iconmaster.aec.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAetherArmor extends ItemArmor {
	
	public ItemAetherArmor(int type) {
		super(ArmorMaterial.CHAIN, 0, type);
		
		this.setUnlocalizedName("aec.aetherArmor");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        this.setMaxDamage(400);
        
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		switch (this.armorType) {
		case (0):
			this.itemIcon = iconRegister.registerIcon("aec:itemAetherHelmet");
			break;
		case (1):
			this.itemIcon = iconRegister.registerIcon("aec:itemAetherChestplate");
			break;
		case (2):
			this.itemIcon = iconRegister.registerIcon("aec:itemAetherLeggings");
			break;
		case (3):
			this.itemIcon = iconRegister.registerIcon("aec:itemAetherBoots");
			break;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName()+"."+this.armorType;
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (slot == 2) {
            return "aec:textures/models/armor/aether_layer_2.png";
        }
        return "aec:textures/models/armor/aether_layer_1.png";
    }
}
