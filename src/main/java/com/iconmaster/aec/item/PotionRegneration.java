package com.iconmaster.aec.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class PotionRegneration extends Potion {

	public PotionRegneration(int id) {
		super(id, false, 13458603);
		setPotionName("aec.potion.regneration");
		setIconIndex(7, 0);
		setEffectiveness(0.25D);
	}
	
	@Override
	 public void performEffect(EntityLivingBase entity, int id) {
        if (entity.getHealth() < entity.getMaxHealth())
        {
            entity.heal(1.0F);
        }
	}
	
	@Override
    public boolean isReady(int duration, int power)
    {
        return duration % 50 == 0;
    }

}
