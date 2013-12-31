package com.iconmaster.aec.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.client.ClientProxy;
import com.iconmaster.aec.common.block.BlockAetherConduit;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAetherConduit implements ISimpleBlockRenderingHandler
{

    @Override
    public void renderInventoryBlock(Block tile, int metadata, int modelID, RenderBlocks renderer)
    {
            BlockAetherConduit block = (BlockAetherConduit)tile;

            Tessellator tessellator = Tessellator.instance;
            Icon icon;

            icon = block.getIcon(0, metadata);

            double minX = icon.getMinU();
            double maxX = icon.getMaxU();
            double minY = icon.getMinV();
            double maxY = icon.getMaxV();

            double offset = 0.001D;

            double xMin = 0, xMax = 1;
            double yMin = 0, yMax = 1;
            double zMid = 0.5;

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            tessellator.startDrawingQuads();

            tessellator.addVertexWithUV(xMin, yMax, zMid, minX, minY);
            tessellator.addVertexWithUV(xMin, yMin, zMid, minX, maxY);
            tessellator.addVertexWithUV(xMax, yMin, zMid, maxX, maxY);
            tessellator.addVertexWithUV(xMax, yMax, zMid, maxX, minY);

            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block tile, int modelId, RenderBlocks renderer) {
	    BlockAetherConduit block = (BlockAetherConduit)tile;
	
	    int metadata = blockAccess.getBlockMetadata(x, y, z);
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.setBrightness(tile.getMixedBrightnessForBlock(blockAccess, x, y, z));
	
	    Icon iconCenter, iconSide;
	
	    if (renderer.hasOverrideBlockTexture())
	    {
	            iconCenter =  iconSide = renderer.overrideBlockTexture;
	    }
	    else
	    {
	            iconCenter = block.centerIcon;
	            iconSide = block.sideIcon;
	    }
	
	    double minXCenter = iconCenter.getMinU();
	    double maxXCenter = iconCenter.getMaxU();
	    double minYCenter = iconCenter.getMinV();
	    double maxYCenter = iconCenter.getMaxV();
	
	    double minXSide = iconSide.getMinU();
	    double maxXSide = iconSide.getMaxU();
	    double minYSide = iconSide.getMinV();
	    double maxYSide = iconSide.getMaxV();
	    
	    double low = 4.0d/16;
	    double high = 12.0d/16;
	
	    double xMin = x;
	    double xLow = x + low;
	    double xHigh = x + high;
	    double xMax = x + 1;
	
	    double zMin = z;
	    double zLow = z + low;
	    double zHigh = z + high;
	    double zMax = z + 1;
	
	    double yMin = y;
	    double yLow = y + low;
	    double yHigh = y + high;
	    double yMax = y + 1;
	
	    boolean connectedNegZ = block.canConnect(blockAccess.getBlockId(x, y, z - 1));
	    boolean connectedPosZ = block.canConnect(blockAccess.getBlockId(x, y, z + 1));
	    boolean connectedNegY = block.canConnect(blockAccess.getBlockId(x , y - 1, z));
	    boolean connectedPosY = block.canConnect(blockAccess.getBlockId(x , y + 1, z));
	    boolean connectedNegX = block.canConnect(blockAccess.getBlockId(x - 1, y, z));
	    boolean connectedPosX = block.canConnect(blockAccess.getBlockId(x + 1, y, z));
	
	    if (connectedNegZ) {
	    	//bottom
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			//front
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zMin, maxXCenter, minYCenter);
	    } else {
			//middle front
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXCenter, minYCenter);
	    }
	    
	    if (connectedPosZ) {
	    	//bottom
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			
			//back
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zMax, maxXCenter, minYCenter);
	    } else {
			//middle back
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, maxXCenter, minYCenter);
	    }
	    
	    if (connectedNegX) {
	    	//bottom
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			//front
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMin, yLow, zLow, maxXCenter, minYCenter);
	    } else {
	    	//middle front
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXCenter, minYCenter);
	    }
	    
	    if (connectedPosX) {
	    	//bottom
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yLow, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			//front
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yLow, zLow, maxXCenter, minYCenter);
	    } else {
	    	//middle front
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, maxXCenter, minYCenter);
	    }
	    
	    if (connectedNegY) {
	    	//bottom
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yLow, zLow, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xLow,yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMin, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			
			//front
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yMin, zLow, maxXCenter, minYCenter);
	    } else {
	    	//middle front
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yLow, zLow, maxXCenter, minYCenter);
	    }
	    
	    if (connectedPosY) {
	    	//bottom
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yHigh, zLow, maxXSide, maxYSide);
	    	
			//right
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, maxXSide, maxYSide);
			
			//left
			tessellator.addVertexWithUV(xLow,yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			
			//top
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			
			//front
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yMax, zLow, maxXCenter, minYCenter);
	    } else {
	    	//middle front
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yHigh, zLow, maxXCenter, minYCenter);
	    }
	    
	    return true;
    }
  
  @Override
  public boolean shouldRender3DInInventory() {
          return false;
  }

	@Override
	public int getRenderId() {
		return ClientProxy.conduitRenderType;
	}
}