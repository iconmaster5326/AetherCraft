package com.iconmaster.aec.common;

import com.iconmaster.aec.client.gui.GuiEnergyContainer;
import com.iconmaster.aec.client.gui.GuiEnergyManipulator;
import com.iconmaster.aec.common.event.FlyingRingEventReciever;
import com.iconmaster.aec.common.gui.ContainerEnergyContainer;
import com.iconmaster.aec.common.gui.ContainerEnergyManipulator;
import com.iconmaster.aec.common.handler.tick.FlyingRingTickHandler;
import com.iconmaster.aec.common.tileentity.TileEntityEnergyContainer;
import com.iconmaster.aec.common.tileentity.TileEntityEnergyManipulator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {
	public void registerRenderInformation() {
	}

	public void registerTiles() {
		GameRegistry.registerTileEntity(TileEntityEnergyManipulator.class,
				"gra.tileentityem");
		GameRegistry.registerTileEntity(TileEntityEnergyContainer.class,
				"gra.tileentityec");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(AetherCraft.blockEnergyManipulator,
				"energyManipulator");
		GameRegistry.registerBlock(AetherCraft.blockEnergyContainer,
				"energyContainer");
		GameRegistry.registerItem(AetherCraft.itemEnergyBattery,
				"energyBattery");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			GameRegistry.registerItem(AetherCraft.itemFlyingRing,
					"flyingRing");
		}
	}

	public void addNames() {
		LanguageRegistry.addName(AetherCraft.blockEnergyManipulator,
				"Energy Manipulator");
		LanguageRegistry.addName(AetherCraft.blockEnergyContainer,
				"Energy Container");
		LanguageRegistry.addName(AetherCraft.itemEnergyBattery,
				"Energy Battery");
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			LanguageRegistry.addName(AetherCraft.itemFlyingRing,
					"Flying Ring");
		}
	}

	public void addRecipes() {
		int[] emrecipe = AetherCraft
				.stringToCraftingArray(AetherCraft.getOptions("emrecipe"));
		int[] ecrecipe = AetherCraft
				.stringToCraftingArray(AetherCraft.getOptions("ecrecipe"));
		int[] ebrecipe = AetherCraft
				.stringToCraftingArray(AetherCraft
						.getOptions("ebatteryrecipe"));

		ItemStack[] emStack = AetherCraft
				.craftingArrayToItemStack(emrecipe);
		ItemStack[] ecStack = AetherCraft
				.craftingArrayToItemStack(ecrecipe);
		ItemStack[] ebStack = AetherCraft
				.craftingArrayToItemStack(ebrecipe);

		// Energy Manipulator
		GameRegistry.addShapedRecipe(new ItemStack(
				AetherCraft.blockEnergyManipulator), "abc", "def", "ghi",
				'a', emStack[0], 'b',
				emStack[1], 'c', emStack[2], Character
						.valueOf('d'), emStack[3], 'e',
				emStack[4], 'f', emStack[5], Character
						.valueOf('g'), emStack[6], 'h',
				emStack[7], 'i', emStack[8]);

		// Energy Container
		GameRegistry.addShapedRecipe(new ItemStack(
				AetherCraft.blockEnergyContainer), "abc", "def", "ghi",
				'a', ecStack[0], 'b',
				ecStack[1], 'c', ecStack[2], Character
						.valueOf('d'), ecStack[3], 'e',
				ecStack[4], 'f', ecStack[5], Character
						.valueOf('g'), ecStack[6], 'h',
				ecStack[7], 'i', ecStack[8]);

		// Energy Battery
		GameRegistry.addShapedRecipe(new ItemStack(
				AetherCraft.itemEnergyBattery), "abc", "def", "ghi",
				'a', ebStack[0], 'b',
				ebStack[1], 'c', ebStack[2], Character
						.valueOf('d'), ebStack[3], 'e',
				ebStack[4], 'f', ebStack[5], Character
						.valueOf('g'), ebStack[6], 'h',
				ebStack[7], 'i', ebStack[8]);

		// Flying Ring
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			int[] frrecipe = AetherCraft
					.stringToCraftingArray(AetherCraft
							.getOptions("flyingringrecipe"));

			ItemStack[] frStack = AetherCraft
					.craftingArrayToItemStack(frrecipe);

			GameRegistry.addShapedRecipe(new ItemStack(
					AetherCraft.itemFlyingRing), "abc", "def", "ghi",
					'a', frStack[0], 'b',
					frStack[1], 'c', frStack[2], Character
							.valueOf('d'), frStack[3], 'e',
					frStack[4], 'f', frStack[5], Character
							.valueOf('g'), frStack[6], 'h',
					frStack[7], 'i', frStack[8]);
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityEnergyManipulator
				&& ID == AetherCraft.GUI_ID_EM) {
			return new ContainerEnergyManipulator(player.inventory,
					(TileEntityEnergyManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityEnergyContainer
				&& ID == AetherCraft.GUI_ID_EC) {
			return new ContainerEnergyContainer(player.inventory,
					(TileEntityEnergyContainer) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityEnergyManipulator
				&& ID == AetherCraft.GUI_ID_EM) {
			return new GuiEnergyManipulator(player.inventory,
					(TileEntityEnergyManipulator) tileEntity);
		} else if (tileEntity instanceof TileEntityEnergyContainer
				&& ID == AetherCraft.GUI_ID_EC) {
			return new GuiEnergyContainer(player.inventory,
					(TileEntityEnergyContainer) tileEntity);
		}
		return null;
	}

	public void registerTickHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			TickRegistry.registerScheduledTickHandler(
					new FlyingRingTickHandler(), Side.SERVER);
		}
	}

	public void registerEventHandlers() {
		if (Boolean.parseBoolean(AetherCraft.getOptions("enableflyring"))) {
			MinecraftForge.EVENT_BUS.register(new FlyingRingEventReciever());
		}
	}
}
