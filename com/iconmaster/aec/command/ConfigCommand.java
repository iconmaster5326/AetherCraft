package com.iconmaster.aec.command;

import java.util.ArrayList;
import java.util.List;

import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.util.NumberUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLCommonHandler;

public class ConfigCommand implements ICommand {
	private List aliases, tabCompletionOptions;

	public ConfigCommand() {
		this.aliases = new ArrayList();
		this.aliases.add("aec");
		this.tabCompletionOptions = new ArrayList();
		this.tabCompletionOptions.add("reload");
		this.tabCompletionOptions.add("addcurrent");
		this.tabCompletionOptions.add("addinventory");
		this.tabCompletionOptions.add("addrawentry");
		this.tabCompletionOptions.add("createconfig");
		this.tabCompletionOptions.add("loadconfig");
		this.tabCompletionOptions.add("saveconfig");
		this.tabCompletionOptions.add("unloadconfig");
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "aec";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return EnumChatFormatting.RED + "/aec <command>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ChatMessageComponent cmc = new ChatMessageComponent();
		if (astring.length == 0) {
			cmc.addText(EnumChatFormatting.RED + "Invalid arguments");
			icommandsender.sendChatToPlayer(cmc);
			return;
		} else {
			if (astring[0].equalsIgnoreCase("reload")) {
				AetherCraft.reloadConfigFiles();
				cmc = new ChatMessageComponent();
				cmc.addText(EnumChatFormatting.GREEN
						+ "Configurations Reloaded :)");
				icommandsender.sendChatToPlayer(cmc);
				return;
			} else if (astring[0].equalsIgnoreCase("addcurrent")
					&& astring.length >= 2
					&& NumberUtils.isInteger(astring[1])) {
				ItemStack stack = ((EntityPlayerMP) icommandsender).inventory
						.getCurrentItem();
				if (stack != null) {
					if (AetherCraft.addAVEntryToConfig(
							stack.getUnlocalizedName(),
							Float.parseFloat(astring[1]))) {
						// Chat Message
						cmc = new ChatMessageComponent();
						cmc.addText(EnumChatFormatting.GREEN
								+ "Block/Item: "
								+ EnumChatFormatting.GRAY
								+ stack.getDisplayName()
								+ EnumChatFormatting.GREEN
								+ " successfully registered with an AV value of "
								+ EnumChatFormatting.GRAY + astring[1]
								+ EnumChatFormatting.GREEN + " :)");
						icommandsender.sendChatToPlayer(cmc);
					} else {
						// Chat Message
						cmc = new ChatMessageComponent();
						cmc.addText(EnumChatFormatting.RED
								+ "No configuration is loaded! Load or create one before performing this command.");
						icommandsender.sendChatToPlayer(cmc);
					}
				} else {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED
							+ "You must hold an item/block in your hand before performing this command.");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("addinventory")
					&& astring.length >= 2
					&& NumberUtils.isInteger(astring[1])) {
				ItemStack[] stack = ((EntityPlayerMP) icommandsender).inventory.mainInventory;
				boolean nothingChanged = true;
				for (ItemStack is : stack) {
					if (is != null) {
						if (AetherCraft.addAVEntryToConfig(
								is.getUnlocalizedName(),
								Float.parseFloat(astring[1]))) {
							nothingChanged = false;
							// Chat Message
							cmc.addText(EnumChatFormatting.GREEN
									+ "Block/Item: "
									+ EnumChatFormatting.GRAY
									+ is.getDisplayName()
									+ EnumChatFormatting.GREEN
									+ " successfully registered with an AV value of "
									+ EnumChatFormatting.GRAY + astring[1]
									+ EnumChatFormatting.GREEN + " :)");
							icommandsender.sendChatToPlayer(cmc);
						} else {
							// Chat Message
							cmc = new ChatMessageComponent();
							cmc.addText(EnumChatFormatting.RED
									+ "No configuration is loaded! Load or create one before performing this command.");
							icommandsender.sendChatToPlayer(cmc);
							return;
						}
					}
				}
				if (nothingChanged) {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED
							+ "You must have at least 1 item/block in your inventory before performing this command.");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("addrawentry")
					&& astring.length >= 3) {
				if (NumberUtils.isInteger(astring[2])) {
					if (AetherCraft.addAVEntryToConfig(astring[1],
							Float.parseFloat(astring[2]))) {
						// Chat Message
						cmc = new ChatMessageComponent();
						cmc.addText(EnumChatFormatting.GREEN
								+ "Entry: "
								+ EnumChatFormatting.GRAY
								+ astring[1]
								+ EnumChatFormatting.GREEN
								+ " successfully registered with an AV value of "
								+ EnumChatFormatting.GRAY + astring[2]
								+ EnumChatFormatting.GREEN + " :)");
						icommandsender.sendChatToPlayer(cmc);
					} else {
						// Chat Message
						cmc = new ChatMessageComponent();
						cmc.addText(EnumChatFormatting.RED
								+ "No configuration is loaded! Load or create one before performing this command.");
						icommandsender.sendChatToPlayer(cmc);
					}
				} else {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED
							+ "The last argument must be a number.");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("loadconfig")
					&& astring.length >= 2) {
				if (AetherCraft.loadAVConfig(astring[1])) {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.GREEN + "Configuration: "
							+ EnumChatFormatting.GRAY + astring[1]
							+ EnumChatFormatting.GREEN
							+ " got successfully loaded :)");
					icommandsender.sendChatToPlayer(cmc);

					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.DARK_RED
							+ " REMEMBER TO SAVE IT AFTERWARDS, OR IT WILL GET DESTROYED!");
					icommandsender.sendChatToPlayer(cmc);
				} else {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED + "Configuration: "
							+ EnumChatFormatting.GRAY + astring[1]
							+ EnumChatFormatting.RED + " doesn't exist!");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("createconfig")
					&& astring.length >= 2) {
				if (AetherCraft.createNewAVConfig(astring[1])) {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.GREEN + "Configuration: "
							+ EnumChatFormatting.GRAY + astring[1]
							+ EnumChatFormatting.GREEN
							+ " got successfully created :)");
					icommandsender.sendChatToPlayer(cmc);

					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.DARK_RED
							+ " REMEMBER TO SAVE IT AFTERWARDS, OR IT WILL GET DESTROYED!");
					icommandsender.sendChatToPlayer(cmc);
				} else {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED + "Configuration: "
							+ EnumChatFormatting.GRAY + astring[1]
							+ EnumChatFormatting.RED + " already exists!");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("saveconfig")) {
				if (AetherCraft.getCurrentConfigFile() != null) {
					AetherCraft.saveCurrentAVConfig();
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.GREEN
							+ "Configuration: "
							+ EnumChatFormatting.GRAY
							+ AetherCraft.getCurrentConfigFile()
									.getName() + EnumChatFormatting.GREEN
							+ " got successfully saved :)");
					icommandsender.sendChatToPlayer(cmc);
				} else {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED
							+ "No configuration is loaded!");
					icommandsender.sendChatToPlayer(cmc);
				}
				return;
			} else if (astring[0].equalsIgnoreCase("unloadconfig")) {
				if (AetherCraft.getCurrentConfigFile() == null) {
					// Chat Message
					cmc = new ChatMessageComponent();
					cmc.addText(EnumChatFormatting.RED
							+ "No configuration is loaded!");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				String configName = AetherCraft.getCurrentConfigFile()
						.getName();
				AetherCraft.unloadCurrentConfig();

				// Chat Message
				cmc = new ChatMessageComponent();
				cmc.addText(EnumChatFormatting.GREEN + "Configuration: "
						+ EnumChatFormatting.GRAY + configName
						+ EnumChatFormatting.GREEN
						+ " got successfully unloaded :)");
				icommandsender.sendChatToPlayer(cmc);
				return;
			}
			// Chat Message
			cmc = new ChatMessageComponent();
			cmc.addText(EnumChatFormatting.RED + "Invalid arguments!");
			icommandsender.sendChatToPlayer(cmc);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			if (icommandsender instanceof EntityPlayerMP) {
				return true;
			} else {
				// Chat Message
				ChatMessageComponent cmc = new ChatMessageComponent();
				cmc.addText(EnumChatFormatting.RED
						+ "This command must be executed by a player, not from the console!");
				icommandsender.sendChatToPlayer(cmc);
			}
		}
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return this.tabCompletionOptions;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}
}
