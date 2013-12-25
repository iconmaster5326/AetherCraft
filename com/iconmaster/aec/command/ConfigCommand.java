package com.iconmaster.aec.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.common.AetherCraft;
import com.iconmaster.aec.config.AVConfig;

import cpw.mods.fml.common.FMLCommonHandler;

public class ConfigCommand implements ICommand {
	private List aliases, tabCompletionOptions;
	private AVConfig config;
	private String configName;

	public ConfigCommand() {
		this.aliases = new ArrayList();
		this.aliases.add("aec");
		this.tabCompletionOptions = new ArrayList();
		this.tabCompletionOptions.add("name");
		this.tabCompletionOptions.add("edit");
		this.tabCompletionOptions.add("add");
		this.tabCompletionOptions.add("save");
		this.tabCompletionOptions.add("reload");
		this.tabCompletionOptions.add("help");
		this.tabCompletionOptions.add("discard");
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
			cmc.addText(EnumChatFormatting.RED + "Type '/aec help' for usage help.");
			icommandsender.sendChatToPlayer(cmc);
			return;
		} else {
			ItemStack item = ((EntityPlayerMP) icommandsender).inventory.getCurrentItem();
			if (astring[0].equalsIgnoreCase("name") && item != null) {
				String name = item.getUnlocalizedName();
				cmc = new ChatMessageComponent();
				if (name==null) {
					cmc.addText(EnumChatFormatting.RED  + "Item has no unlocalized name!");
				} else {
					cmc.addText(EnumChatFormatting.GREEN  + "The item's name is " + name);
				}
				icommandsender.sendChatToPlayer(cmc);
				return;
			} else if (astring[0].equalsIgnoreCase("edit")) {
				if (astring.length < 2) {
					cmc.addText(EnumChatFormatting.RED  + "Usage: /aec edit configname");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				if (config != null) {
					cmc.addText(EnumChatFormatting.RED  + "Close "+configName+" with /aec save or /aec discard before you open a new one.");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				configName = astring[1];
				config = new AVConfig(new File(AetherCraft.getConfigDir(),configName+".cfg"));
				cmc.addText(EnumChatFormatting.GREEN  + "Now editing the config file "+configName+".");
				icommandsender.sendChatToPlayer(cmc);
			} else if (astring[0].equalsIgnoreCase("add")) {
				if (astring.length < 2) {
					cmc.addText(EnumChatFormatting.RED  + "Usage: /aec add [item] av");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				if (config==null) {
					cmc.addText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				float av;
				if (astring.length == 2) {
					av = Float.parseFloat(astring[1]);
				} else {
					item = AVRegistry.getItemFromString(astring[1]);
					av = Float.parseFloat(astring[2]);
				}
				if (item == null) {
					cmc.addText(EnumChatFormatting.RED  + "Item specified not found!");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				config.addValue(item, av);
				cmc.addText(EnumChatFormatting.GREEN + item.getUnlocalizedName() + " is now worth " + av + " AV.");
				icommandsender.sendChatToPlayer(cmc);
			} else if (astring[0].equalsIgnoreCase("save")) {
				if (config == null) {
					cmc.addText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				config.saveValues();
				config = null;
				cmc.addText(EnumChatFormatting.GREEN +configName+" has been saved. Use /aec reload to apply changes.");
				icommandsender.sendChatToPlayer(cmc);
			} else if (astring[0].equalsIgnoreCase("reload")) {
				if (config != null) {
					cmc.addText(EnumChatFormatting.RED  + "The current config file has not yet been saved. Use /aec save or /aec discard first.");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				AVRegistry.reloadAllValues();
				cmc.addText(EnumChatFormatting.GREEN +" All AV values have been updated.");
				icommandsender.sendChatToPlayer(cmc);
			} else if (astring[0].equalsIgnoreCase("discard")) {
				if (config == null) {
					cmc.addText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.sendChatToPlayer(cmc);
					return;
				}
				config = null;
				cmc.addText(EnumChatFormatting.GREEN +"The changes to "+configName+" have been discarded.");
				icommandsender.sendChatToPlayer(cmc);
			} else if (astring[0].equalsIgnoreCase("help")) {
				cmc.addText("To begin editing a custom AV config file, use /aec edit configname.\n");
				cmc.addText("To add a custom AV value, use /aec add itemname av. The item name is optional; if omitted, it will use the item in your hand.\n");
				cmc.addText("When you're done adding AV values to the file, call /aec save to save it to the file system.\n");
				cmc.addText("If you aren't happy with your changes, use /aec discard to cancel them.\n");
				cmc.addText("When you've saved you configs, use /aec reload to apply your changes.");
				icommandsender.sendChatToPlayer(cmc);
			}
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
