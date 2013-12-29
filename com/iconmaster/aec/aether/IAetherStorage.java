package com.iconmaster.aec.aether;

public interface IAetherStorage {
	public float addAether(float av);

	public float extractAether(float av);

	//public void setAether(float av);

	public float getAether();
	
	public float tryAddAether(float av);
	
	public float tryExtractAether(float av);
}