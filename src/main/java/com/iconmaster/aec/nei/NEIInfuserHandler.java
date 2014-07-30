package com.iconmaster.aec.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.iconmaster.aec.aether.InfuserRegistry;
import com.iconmaster.aec.util.NumberUtils;
import com.iconmaster.aec.util.UidUtils;

public class NEIInfuserHandler extends TemplateRecipeHandler {
	public static final int XOFFSET = 5;
	public static final int YOFFSET = 11;
	
	public class CachedInfuserRecipe extends CachedRecipe {
		private ItemStack input;
		private ItemStack output;
		
		public CachedInfuserRecipe(ItemStack input, ItemStack output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public PositionedStack getResult() {
			return new PositionedStack(output,135-XOFFSET,33-YOFFSET);
		}
		
		@Override
		public List<PositionedStack> getIngredients() {
			List<PositionedStack> list = new ArrayList<PositionedStack>();
			
			list.add(new PositionedStack(input,38-XOFFSET,33-YOFFSET));
			
			return list;
		}
	}

	@Override
	public String getRecipeName() {
		return "Aether Infuser";
	}

	@Override
	public String getGuiTexture() {
		return "aec:textures/gui/aetherInfuserGui.png";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("item"))
			loadCraftingRecipes((ItemStack)results[0]);
		else if (outputId.equals("allAetherInfuser")) {
			for (Object entry : InfuserRegistry.getRecipes().entrySet()) {
				Entry recipe = (Entry)entry;
				ItemStack input = UidUtils.getStackFromUid((List) recipe.getKey());
				ItemStack output = (ItemStack) recipe.getValue();
				arecipes.add(new CachedInfuserRecipe(input,output));
			}

		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result){
		for (Object entry : InfuserRegistry.getRecipes().entrySet()) {
			Entry recipe = (Entry)entry;
			ItemStack input = UidUtils.getStackFromUid((List) recipe.getKey());
			ItemStack output = (ItemStack) recipe.getValue();
			
			if (output.getItem()==result.getItem() && output.getItemDamage()==result.getItemDamage()) {
				arecipes.add(new CachedInfuserRecipe(input,output));
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients)
	{
		if (ingredients.length == 0) {return;}
		ItemStack finalInput = null;
		
		if (ingredients[0] instanceof ItemStack) {
			finalInput = (ItemStack) ingredients[0];
		} else if (ingredients[0] instanceof FluidStack) {
			Block block = ((FluidStack) ingredients[0]).getFluid().getBlock();
			if (block!=null) {
				finalInput = new ItemStack(block,((FluidStack) ingredients[0]).amount);
			}
		}
		if (finalInput==null) {return;}
		
		for (Object entry : InfuserRegistry.getRecipes().entrySet()) {
			Entry recipe = (Entry)entry;
			ItemStack input = UidUtils.getStackFromUid((List) recipe.getKey());
			ItemStack output = (ItemStack) recipe.getValue();
			
			if (input.getItem()==finalInput.getItem() && input.getItemDamage()==finalInput.getItemDamage()) {
				arecipes.add(new CachedInfuserRecipe(input,output));
			}
		}
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		CachedInfuserRecipe crecipe = (CachedInfuserRecipe) arecipes.get(recipe);
		
		//render AV amount
		GuiDraw.drawString("AV: "+NumberUtils.display(InfuserRegistry.getOutputAV(crecipe.input)), 68-XOFFSET,16-YOFFSET, 0x00FF00);
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(67-XOFFSET,27-YOFFSET,123-67-XOFFSET,55-27-YOFFSET),"allAetherInfuser"));
	}
}
