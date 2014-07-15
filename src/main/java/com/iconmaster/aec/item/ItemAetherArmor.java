package com.iconmaster.aec.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.ISpecialArmor;

import com.iconmaster.aec.AetherCraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAetherArmor extends ItemArmor implements ISpecialArmor {
	public IIcon[] icons = new IIcon[4];
	
	public ItemAetherArmor(int type) {
		super(ArmorMaterial.IRON, 0, type);
		
		this.setUnlocalizedName("aec.aetherArmor");
        this.setCreativeTab(AetherCraft.tabAetherCraft);
        this.setMaxDamage(200);

	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icons[0] = iconRegister.registerIcon("aec:itemAetherHelmet");
		this.icons[1] = iconRegister.registerIcon("aec:itemAetherChestplate");
		this.icons[2] = iconRegister.registerIcon("aec:itemAetherLeggings");
		this.icons[3] = iconRegister.registerIcon("aec:itemAetherBoots");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[this.armorType];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName()+"."+this.armorType;
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(0, 1, (int) (damage*(this.damageReduceAmount/10)));
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return this.damageReduceAmount;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		System.out.println("Armor damage by "+damage);
		stack.damageItem(damage, entity);
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
