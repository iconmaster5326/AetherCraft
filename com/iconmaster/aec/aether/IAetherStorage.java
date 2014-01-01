package com.iconmaster.aec.aether;

/**
 * TileEntites implement this to gain Aether reception abilities. Be sure to have the corresponding Block implement IAetherTransfer too!
 * @author iconmaster
 *
 */
public interface IAetherStorage {
	/**
	 * Call this to add aether to a device. Returns the amount of Aether that was unable to be sent.
	 * @param av
	 * @return
	 */
	public float addAether(float av);

	/**
	 * Call this to get Aether from a device. Returns the amount actually extracted.
	 * @param av
	 * @return
	 */
	public float extractAether(float av);

	/**
	 * Returns the amount of Aether stored in this device.
	 * @return
	 */
	public float getAether();
	
	/**
	 * Like addAether, but only returns the theoretical result if addAether was called. Use to check if aether can be added.
	 * @param av
	 * @return
	 */
	public float tryAddAether(float av);
	
	/**
	 * like extractAether, but only returns the theoretical result if extractAether was called. Use to check if aether can be extracted.
	 * @param av
	 * @return
	 */
	public float tryExtractAether(float av);
}