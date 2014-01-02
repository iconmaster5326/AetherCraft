package com.iconmaster.aec.util;

import net.minecraft.util.Icon;

public class BlockTextureData {
	private Icon top;
	private Icon side;
	private Icon bottom;
	
	public BlockTextureData(Icon top,Icon side,Icon bottom) {
		this.top = top;
		this.side = side;
		this.bottom = bottom;
	}
	
	public Icon getTop() {
		return top;
	}

	public Icon getSide() {
		return side;
	}

	public Icon getBottom() {
		return bottom;
	}
}
