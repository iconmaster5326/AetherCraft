package com.iconmaster.aec.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDummy extends Item {
	public ItemDummy() {
		super();
        this.setUnlocalizedName("aec.dummy");
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "aec.dummy."+stack.getItemDamage();
	}
}
