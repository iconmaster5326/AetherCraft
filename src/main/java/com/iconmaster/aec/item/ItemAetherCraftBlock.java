package com.iconmaster.aec.item;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.block.BlockAetherConduit;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemAetherCraftBlock extends ItemBlock {
	public ItemAetherCraftBlock(Block block) {
		super(block);
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
	public IIcon getIcon(ItemStack stack, int pass) {
		 ItemAetherCraftBlock blockStack = (ItemAetherCraftBlock) stack.getItem();
		 Block block = Block.getBlockFromItem(blockStack);
		 if (block instanceof BlockAetherConduit) {
			 return block.getIcon(0, stack.getItemDamage());
		 }
		 return super.getIcon(stack, pass);
	}
	
}
