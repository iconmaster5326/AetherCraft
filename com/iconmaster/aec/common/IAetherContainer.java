package com.iconmaster.aec.common;

public interface IAetherContainer {
	public int addEnergy(int ev);

	public int extractEnergy(int ev);

	public void setEnergy(int ev);

	public int getEnergy();
}