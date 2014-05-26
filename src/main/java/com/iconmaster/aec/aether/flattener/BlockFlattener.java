package com.iconmaster.aec.aether.flattener;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockFlattener implements IFlattenerHandler<Block> {

	@Override
	public ItemStack flatten(Block input) {
		return new ItemStack(input);
	}

}
