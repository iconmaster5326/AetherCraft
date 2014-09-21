package com.iconmaster.aec.client.particle;

import com.iconmaster.aec.AetherCraft;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.World;

public class AetherFlameFX extends EntityFX {

	public AetherFlameFX(World world, double x,double y, double z, double xv,double yv, double zv) {
		super(world, x, y, z, xv, yv, zv);
        this.motionX = this.motionX * 0.01D + xv;
        this.motionY = this.motionY * 0.01D + yv;
        this.motionZ = this.motionZ * 0.01D + zv;
        double d6 = x + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = y + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = z + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.noClip = true;
        //this.setParticleTextureIndex(48);
		this.setParticleIcon(AetherCraft.blockAetherFlame.getIcon(0, 0));
	}

    @Override
    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
        float f6 = ((float)this.particleAge + par2) / (float)this.particleMaxAge;
        this.particleScale = this.particleScale * (1.0F - f6 * f6 * 0.5F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
    }
    
    @Override
    public int getBrightnessForRender(float par1) {
    	float f1 = ((float)this.particleAge + par1) / (float)this.particleMaxAge;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender(par1);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public float getBrightness(float par1) {
    	float f1 = ((float)this.particleAge + par1) / (float)this.particleMaxAge;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        float f2 = super.getBrightness(par1);
        return f2 * f1 + (1.0F - f1);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
    
    @Override
    public int getFXLayer()
    {
        return 2;
    }
    
    @SideOnly(Side.CLIENT)
    public static void spawnFX(World world, double x, double y, double z, double xVel, double yVel, double zVel){
    	AetherFlameFX fx = new AetherFlameFX(Minecraft.getMinecraft().theWorld, x, y, z, xVel, yVel, zVel);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect((EntityFX)fx);
    }
}
