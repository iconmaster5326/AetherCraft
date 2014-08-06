package com.iconmaster.aec.client.render;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAetherFlame implements ISimpleBlockRenderingHandler {
	private static Random random = new Random();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		renderItem(renderer,block,metadata,0);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tess = Tessellator.instance;
		
		IIcon icon = AetherCraft.blockAetherFlame.getIcon(0, 0);
		
		double minU = icon.getMinU();
		double maxU = icon.getMaxU();
		double minV = icon.getMinV();
		double maxV = icon.getMaxV();
		
		double minX = x+.3;
		double minY = y+.3;
		double minZ = z+.3;
		double midX = x+.45;
		double midY = y+.45;
		double midZ = z+.45;
		double maxX = x+.6;
		double maxY = y+.6;
		double maxZ = z+.6;
		
		tess.setBrightness(14680304);
		
		tess.addVertexWithUV(maxX, maxY, midZ, maxU, maxV);
		tess.addVertexWithUV(maxX, minY, midZ, maxU, minV);
		tess.addVertexWithUV(minX, minY, midZ, minU, minV);
		tess.addVertexWithUV(minX, maxY, midZ, minU, maxV);
		
		tess.addVertexWithUV(minX, maxY, midZ, minU, maxV);
		tess.addVertexWithUV(minX, minY, midZ, minU, minV);
		tess.addVertexWithUV(maxX, minY, midZ, maxU, minV);
		tess.addVertexWithUV(maxX, maxY, midZ, maxU, maxV);
		
		tess.addVertexWithUV(midX, maxY, maxZ, maxU, maxV);
		tess.addVertexWithUV(midX, minY, maxZ, maxU, minV);
		tess.addVertexWithUV(midX, minY, minZ, minU, minV);
		tess.addVertexWithUV(midX, maxY, minZ, minU, maxV);
		
		tess.addVertexWithUV(midX, maxY, minZ, minU, maxV);
		tess.addVertexWithUV(midX, minY, minZ, minU, minV);
		tess.addVertexWithUV(midX, minY, maxZ, maxU, minV);
		tess.addVertexWithUV(midX, maxY, maxZ, maxU, maxV);
		
		tess.addVertexWithUV(maxX, midY, maxZ, maxU, maxV);
		tess.addVertexWithUV(minX, midY, maxZ, maxU, minV);
		tess.addVertexWithUV(minX, midY, minZ, minU, minV);
		tess.addVertexWithUV(maxX, midY, minZ, minU, maxV);
		
		tess.addVertexWithUV(maxX, midY, minZ, minU, maxV);
		tess.addVertexWithUV(minX, midY, minZ, minU, minV);
		tess.addVertexWithUV(minX, midY, maxZ, maxU, minV);
		tess.addVertexWithUV(maxX, midY, maxZ, maxU, maxV);
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.flameRenderType;
	}
	
	public void renderItem(RenderBlocks renderer, Block block, int meta, int pass) {
		Tessellator tessellator = Tessellator.instance;
		renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        float r=0,g=0,b=0;
    	
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * r;
        float f8 = f4 * g;
        float f9 = f4 * b;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        f10 = f3 * r;
        f11 = f5 * r;
        f12 = f6 * r;
        f13 = f3 * g;
        f14 = f5 * g;
        f15 = f6 * g;
        f16 = f3 * b;
        f17 = f5 * b;
        f18 = f6 * b;

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceYNeg(block, 0.0D, pass*-.1D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceYPos(block, 0.0D, pass*.01D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, pass*-.01D, renderer.getBlockIconFromSideAndMetadata(block, 2, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceZPos(block, 0.0D, 0.0D, pass*.01D, renderer.getBlockIconFromSideAndMetadata(block, 3, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceXNeg(block, pass*-.01D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        if (pass==1) {
        	tessellator.setColorOpaque_F(f10, f13, f16);
        }
        renderer.renderFaceXPos(block, pass*.01D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, meta));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        
	}

}
