package com.iconmaster.aec.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.common.block.BlockAetherConduit;

public class ItemAetherCraftBlock extends ItemBlock {
	public ItemAetherCraftBlock(int id) {
		super(id);
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setCreativeTab(AetherCraft.tabAetherCraft);
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack item) {
		return getUnlocalizedName()+"."+item.getItemDamage();
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		 ItemAetherCraftBlock blockStack = (ItemAetherCraftBlock) stack.getItem();
		 Block block = Block.blocksList[blockStack.getBlockID()];
		 if (block instanceof BlockAetherConduit) {
			 return block.getIcon(0, stack.getItemDamage());
		 }
		 return super.getIcon(stack, pass);
	}
	
}
