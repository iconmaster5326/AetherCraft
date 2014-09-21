package com.iconmaster.aec.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
	private static ResourceLocation shader = new ResourceLocation("aec:shader-post/aether.json");
	private static ResourceLocation sky = new ResourceLocation("aec:shader-post/aether_sky.png");
	
	public static class CustomSkyRender extends IRenderHandler {
		@Override
		public void render(float ticks, WorldClient world, Minecraft mc) {

            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(false);
            Minecraft.getMinecraft().renderEngine.bindTexture(sky);
            Tessellator tessellator = Tessellator.instance;

            for (int i = 0; i < 6; ++i)
            {
                GL11.glPushMatrix();

                if (i == 1)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 3)
                {
                    GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4)
                {
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                }

                if (i == 5)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                }

                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(2631720);
                tessellator.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
                tessellator.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D+ticks);
                tessellator.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D+ticks, 16.0D+ticks);
                tessellator.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D+ticks, 0.0D);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
	    }
	}
	
//	public static class CustomCloudRender extends IRenderHandler {
//		@Override
//		public void render(float partialTicks, WorldClient world, Minecraft mc) {
//			
//		}
//	}
	
	public static void setAetherShader() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {return;}
		//Minecraft.getMinecraft().theWorld.provider.setCloudRenderer(new CustomCloudRender());
		Minecraft.getMinecraft().theWorld.provider.setSkyRenderer(new CustomSkyRender());
		
		if (OpenGlHelper.shadersSupported) {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				IResourceManager rman = mc.getResourceManager();
				TextureManager tman = new TextureManager(rman);
				Minecraft.getMinecraft().entityRenderer.theShaderGroup = new ShaderGroup(tman, rman, mc.getFramebuffer(), RenderUtils.shader);
				mc.entityRenderer.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		     } catch(Exception e) {
		    	 e.printStackTrace();
		     }
		}
	}
	
	public static void clearAetherShader() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {return;}
		//Minecraft.getMinecraft().theWorld.provider.setCloudRenderer(null);
		Minecraft.getMinecraft().theWorld.provider.setSkyRenderer(null);
		
        if (isAetherShaderOn() && OpenGlHelper.shadersSupported) {
            EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
            if (entityRenderer.getShaderGroup() != null) {
                entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            entityRenderer.theShaderGroup = null;
        }
	}
	
	public static boolean isAetherShaderOn() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {return false;}
		return Minecraft.getMinecraft().entityRenderer.theShaderGroup!=null && RenderUtils.shader.toString().equals(Minecraft.getMinecraft().entityRenderer.theShaderGroup.getShaderGroupName());
	}
}
