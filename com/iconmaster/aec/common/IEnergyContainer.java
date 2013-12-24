package com.iconmaster.aec.common;

public interface IEnergyContainer {
	public int addEnergy(int ev);

	public int extractEnergy(int ev);

	public void setEnergy(int ev);

	public int getEnergy();
}