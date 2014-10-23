package com.iconmaster.avlib;

import com.iconmaster.aec.aether.AVRegistry;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class TestInterpreter extends NumericInterpreter {
	@Override
	public Float getItemValue(ItemData item) {
		return (Float) AVRegistry.getAbsoluteAV(item.toStack());
	}
}
