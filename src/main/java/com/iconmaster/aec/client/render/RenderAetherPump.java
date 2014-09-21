package com.iconmaster.aec.client.render;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.CommonProxy;
import com.iconmaster.aec.block.BlockAetherPump;
import com.iconmaster.aec.tileentity.TileEntityAetherPump;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderAetherPump extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderTileEntityAt(TileEntity rawte, double x, double y, double z, float f) {
		 TileEntityAetherPump te = (TileEntityAetherPump) rawte;
		 
		 Tessellator tessellator = Tessellator.instance;
		 
		 IIcon iconFront = ((BlockAetherPump)AetherCraft.blockAetherPump).frontIcon[te.getMetadata()];
		 IIcon iconSide = ((BlockAetherPump)AetherCraft.blockAetherPump).sideIcon[te.getMetadata()];
		 
		 this.bindTexture(TextureMap.locationBlocksTexture);
		 
 	    double minU;
 	    double maxU;
 	    double minV;
 	    double maxV;
 	    
	    //double x=0,y=0,z=0;
	    
	    double xMin = x;
	    double xMax = x + 1;
	
	    double zMin = z;
	    double zMax = z + 1;
	
	    double yMin = y;
	    double yMax = y + 1;

        tessellator.startDrawingQuads();
        
        minU = te.face==2?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==2?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==2?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==2?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMax, yMax, zMin, maxU, maxV);
		tessellator.addVertexWithUV(xMax, yMin, zMin, maxU, minV);
		tessellator.addVertexWithUV(xMin, yMin, zMin, minU, minV);
		tessellator.addVertexWithUV(xMin, yMax, zMin, minU, maxV);

        minU = te.face==3?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==3?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==3?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==3?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMin, yMax, zMax, minU, minV);
		tessellator.addVertexWithUV(xMin, yMin, zMax, minU, maxV);
		tessellator.addVertexWithUV(xMax, yMin, zMax, maxU, maxV);
		tessellator.addVertexWithUV(xMax, yMax, zMax, maxU, minV);
		
        minU = te.face==0?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==0?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==0?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==0?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMax, yMin, zMin, minU, minV);
		tessellator.addVertexWithUV(xMax, yMin, zMax, minU, maxV);
		tessellator.addVertexWithUV(xMin, yMin, zMax, maxU, maxV);
		tessellator.addVertexWithUV(xMin, yMin, zMin, maxU, minV);
		
        minU = te.face==1?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==1?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==1?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==1?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMin, yMax, zMin, maxU, minV);
		tessellator.addVertexWithUV(xMin, yMax, zMax, maxU, maxV);
		tessellator.addVertexWithUV(xMax, yMax, zMax, minU, maxV);
		tessellator.addVertexWithUV(xMax, yMax, zMin, minU, minV);
		
        minU = te.face==4?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==4?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==4?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==4?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMin, yMax, zMin, maxU, minV);
		tessellator.addVertexWithUV(xMin, yMin, zMin, maxU, maxV);
		tessellator.addVertexWithUV(xMin, yMin, zMax, minU, maxV);
		tessellator.addVertexWithUV(xMin, yMax, zMax, minU, minV);
		
        minU = te.face==5?iconFront.getMinU():iconSide.getMinU();
        maxU = te.face==5?iconFront.getMaxU():iconSide.getMaxU();
        minV = te.face==5?iconFront.getMinV():iconSide.getMinV();
        maxV = te.face==5?iconFront.getMaxV():iconSide.getMaxV();
		tessellator.addVertexWithUV(xMax, yMax, zMax, minU, minV);
		tessellator.addVertexWithUV(xMax, yMin, zMax, minU, maxV);
		tessellator.addVertexWithUV(xMax, yMin, zMin, maxU, maxV);
		tessellator.addVertexWithUV(xMax, yMax, zMin, maxU, minV);
		
		tessellator.draw();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		renderItem(renderer,block,metadata,0);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.pumpRenderType;
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
