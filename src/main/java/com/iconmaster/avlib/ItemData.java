package com.iconmaster.avlib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class ItemData {
	public String name;
	public int meta;
	public int amt;
	public float percent;
	public boolean isFluid = false;

	public ItemData(String name, int amt, int meta, float percent) {
		this.name = name;
		this.amt = amt;
		this.meta = meta;
		this.percent = percent;
	}

	public ItemData(String name, int amt, int meta) {
		this(name,amt,meta,1);
	}

	public ItemData(ItemStack stack, float percent) {
		this(Item.itemRegistry.getNameForObject(stack.getItem()),stack.stackSize,stack.getItemDamage(),percent);
	}

	public ItemData(ItemStack stack) {
		this(stack,1);
	}

	public ItemData(FluidStack stack, float percent) {
		this(Float.toString(stack.fluidID),stack.amount,0,percent);
		isFluid = true;
	}

	public ItemData(FluidStack stack) {
		this(stack,1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemData)) return false;

		ItemData itemData = (ItemData) o;

		if (isFluid != itemData.isFluid) return false;
		if (meta != itemData.meta) return false;
		if (!name.equals(itemData.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + meta;
		result = 31 * result + (isFluid ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (isFluid) {
			sb.append("FLUID:");
		}
		sb.append(name);
		if (meta!=0) {
			sb.append("<").append(meta).append(">");
		}
		if (amt!=0) {
			sb.append("*").append(amt);
		}
		if (percent!=1f) {
			sb.append("*").append(percent);
		}
		sb.append(")");
		return sb.toString();
	}

	public ItemStack toStack() {
		if (isFluid) {
			return null;
		}
		return new ItemStack((Item)Item.itemRegistry.getObject(name),amt,meta);
	}
}
