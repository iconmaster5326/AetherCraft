package com.iconmaster.aec.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.iconmaster.aec.block.BlockAetherConduit;
import com.iconmaster.aec.client.ClientProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAetherConduit implements ISimpleBlockRenderingHandler
{
    
	    
    @Override
    public void renderInventoryBlock(Block tile, int metadata, int modelID, RenderBlocks renderer)
    {
            BlockAetherConduit block = (BlockAetherConduit)tile;

            Tessellator tessellator = Tessellator.instance;
            
    	    IIcon iconCenter, iconSide;
    	    if (renderer.hasOverrideBlockTexture())
    	    {
    	            iconCenter =  iconSide = renderer.overrideBlockTexture;
    	    }
    	    else
    	    {
    	            iconCenter = block.icon[metadata].getTop();
    	            iconSide = block.icon[metadata].getSide();
    	    }
    	    
    	    double low = 6.0d/16;
    	    double high = 10.0d/16;

    	    double minXCenter = iconCenter.getMinU();
    	    double maxXCenter = iconCenter.getMaxU();
    	    double minYCenter = iconCenter.getMinV();
    	    double maxYCenter = iconCenter.getMaxV();
            
    	    double minXSide = iconSide.getMinU();
    	    double maxXSide = iconSide.getMaxU();
    	    double minYSide = iconSide.getMinV();
    	    double maxYSide = iconSide.getMaxV();

            double offset = 0.001D;
    	
    	    double x=0,y=0,z=0;
    	    
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

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            tessellator.startDrawingQuads();

//            tessellator.addVertexWithUV(xMin, yMax, zMid, minX, minY);
//            tessellator.addVertexWithUV(xMin, yMin, zMid, minX, maxY);
//            tessellator.addVertexWithUV(xMax, yMin, zMid, maxX, maxY);
//            tessellator.addVertexWithUV(xMax, yMax, zMid, maxX, minY);
            
            
            //neg z
            tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xLow, yLow, zMin, maxXSide, minYSide);
            tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yLow, zMin, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXCenter, minYCenter);
			
			//pos z
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zMax, maxXCenter, minYCenter);
			
	    	//neg x
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMin, yLow, zLow, maxXCenter, minYCenter);
			
	    	//pos x
            tessellator.addVertexWithUV(xHigh, yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xMax, yLow, zLow, maxXSide, minYSide);
            tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xMax, yLow, zLow, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXCenter, minYCenter);
			
	    	//neg y
            tessellator.addVertexWithUV(xLow,yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xLow,yMin, zLow, maxXSide, minYSide);
            tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMin, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow,yMin, zLow, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXCenter, minYCenter);
			
	    	//pos y
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yHigh, zLow, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow,yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yMax, zLow, maxXCenter, minYCenter);

            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block tile, int modelId, RenderBlocks renderer) {
	    BlockAetherConduit block = (BlockAetherConduit)tile;
	
	    int metadata = blockAccess.getBlockMetadata(x, y, z);
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.setBrightness(tile.getMixedBrightnessForBlock(blockAccess, x, y, z));
	
	    IIcon iconCenter, iconSide;
	
	    if (renderer.hasOverrideBlockTexture())
	    {
	            iconCenter =  iconSide = renderer.overrideBlockTexture;
	    }
	    else
	    {
	            iconCenter = block.icon[metadata].getTop();
	            iconSide = block.icon[metadata].getSide();
	    }
	    
	    double low = 4.0d/16;
	    double high = 12.0d/16;
	
	    double minXCenter = iconCenter.getMinU();
	    double maxXCenter = iconCenter.getMaxU();
	    double minYCenter = iconCenter.getMinV();
	    double maxYCenter = iconCenter.getMaxV();
	
	    double minXSide = iconSide.getMinU();
	    double maxXSide = iconSide.getMaxU();
	    double minYSide = iconSide.getMinV();
	    double maxYSide = iconSide.getMaxV();
	    
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
	
	    boolean connectedNegZ = block.canConnect(blockAccess.getBlock(x, y, z - 1));
	    boolean connectedPosZ = block.canConnect(blockAccess.getBlock(x, y, z + 1));
	    boolean connectedNegY = block.canConnect(blockAccess.getBlock(x , y - 1, z));
	    boolean connectedPosY = block.canConnect(blockAccess.getBlock(x , y + 1, z));
	    boolean connectedNegX = block.canConnect(blockAccess.getBlock(x - 1, y, z));
	    boolean connectedPosX = block.canConnect(blockAccess.getBlock(x + 1, y, z));
	
	    if (connectedNegZ) {
            tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xLow, yLow, zMin, maxXSide, minYSide);
            tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yLow, zMin, minXCenter, minYCenter);
			tessellator.addVertexWithUV(xLow, yLow, zMin, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zMin, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zMin, minXCenter, maxYCenter);
			
	    } else {
			//middle front
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXCenter, minYCenter);
	    }
	    
	    if (connectedPosZ) {
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yLow, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zMax, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zMax, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			
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
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zLow, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yLow, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yLow, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMin, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow, yHigh, zHigh, minXSide, maxYSide);
			
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
            tessellator.addVertexWithUV(xHigh, yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xMax, yLow, zLow, maxXSide, minYSide);
            tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yLow, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xMax, yLow, zLow, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xMax, yLow, zHigh, minXCenter, minYCenter);
	    } else {
	    	//middle front
	    	tessellator.addVertexWithUV(xHigh, yLow, zLow, maxXCenter, minYCenter);
	    	tessellator.addVertexWithUV(xHigh, yHigh, zLow, maxXCenter, maxYCenter);
	    	tessellator.addVertexWithUV(xHigh, yHigh, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh, yLow, zHigh, minXCenter, minYCenter);
	    }
	    
	    if (connectedNegY) {
            tessellator.addVertexWithUV(xLow,yLow, zLow, maxXSide, maxYSide);
            tessellator.addVertexWithUV(xLow,yMin, zLow, maxXSide, minYSide);
            tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMin, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yLow, zLow, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yLow, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow,yMin, zLow, maxXCenter, minYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zLow, maxXCenter, maxYCenter);
			tessellator.addVertexWithUV(xHigh,yMin, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yMin, zHigh, minXCenter, minYCenter);
	    } else {
	    	//middle front
	    	tessellator.addVertexWithUV(xLow,yLow, zLow, maxXCenter, minYCenter);
	    	tessellator.addVertexWithUV(xHigh,yLow, zLow, maxXCenter, maxYCenter);
	    	tessellator.addVertexWithUV(xHigh,yLow, zHigh, minXCenter, maxYCenter);
			tessellator.addVertexWithUV(xLow,yLow, zHigh, minXCenter, minYCenter);
	    }
	    
	    if (connectedPosY) {
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yHigh, zLow, maxXSide, maxYSide);
	    	
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, maxXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xLow,yHigh, zHigh, minXSide, maxYSide);
			
			tessellator.addVertexWithUV(xLow,yHigh, zLow, minXSide, maxYSide);
			tessellator.addVertexWithUV(xLow,yMax, zLow, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			
			tessellator.addVertexWithUV(xHigh,yHigh, zLow, maxXSide, maxYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zLow, maxXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yMax, zHigh, minXSide, minYSide);
			tessellator.addVertexWithUV(xHigh,yHigh, zHigh, minXSide, maxYSide);
			
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
  public boolean shouldRender3DInInventory(int par1) {
          return true;
  }

	@Override
	public int getRenderId() {
		return ClientProxy.conduitRenderType;
	}
}
