package com.iconmaster.aec.aether;

import net.minecraft.item.ItemStack;

/**
 * Like IAetherStorage, but used for Aether-storing Items.
 * @author iconmaster
 *
 */
public interface IAetherStorageItem {
	/**
	 * Call this to add aether to a item. Returns the amount of Aether that was unable to be sent.
	 * @param av
	 * @return
	 */
	public float addAether(ItemStack stack,float av);

	/**
	 * Call this to get Aether from a item. Returns the amount actually extracted.
	 * @param av
	 * @return
	 */
	public float extractAether(ItemStack stack,float av);

	/**
	 * Returns the amount of Aether stored in this item.
	 * @return
	 */
	public float getAether(ItemStack stack);
	
	/**
	 * Like addAether, but only returns the theoretical result if addAether was called. Use to check if aether can be added.
	 * @param av
	 * @return
	 */
	public float tryAddAether(ItemStack stack, float av);
	
	/**
	 * like extractAether, but only returns the theoretical result if extractAether was called. Use to check if aether can be extracted.
	 * @param av
	 * @return
	 */
	public float tryExtractAether(ItemStack stack, float av);
}