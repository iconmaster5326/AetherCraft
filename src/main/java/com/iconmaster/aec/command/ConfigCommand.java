package com.iconmaster.aec.command;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.iconmaster.aec.AetherCraft;
import com.iconmaster.aec.aether.AVRegistry;
import com.iconmaster.aec.config.AVConfig;
import com.iconmaster.aec.network.AetherCraftPacketHandler;
import com.iconmaster.aec.network.TransferConfigsPacket;

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
		this.tabCompletionOptions.add("addall");
		this.tabCompletionOptions.add("save");
		this.tabCompletionOptions.add("reload");
		this.tabCompletionOptions.add("help");
		this.tabCompletionOptions.add("discard");
		this.tabCompletionOptions.add("get");
		this.tabCompletionOptions.add("dump");
		this.tabCompletionOptions.add("del");
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
		ChatComponentText cmc = new ChatComponentText("");
		if (astring.length == 0) {
			cmc.appendText(EnumChatFormatting.RED + "Type '/aec help' for usage help.");
			icommandsender.addChatMessage(cmc);
			return;
		} else {
			ItemStack item = null;
			if (icommandsender instanceof EntityPlayerMP) {
				item = ((EntityPlayerMP) icommandsender).inventory.getCurrentItem();
			}
			if (astring[0].equalsIgnoreCase("name") && item != null) {
				String name = Item.itemRegistry.getNameForObject(item.getItem());
				cmc = new ChatComponentText("");
				cmc.appendText(EnumChatFormatting.GREEN  + "The item's name is " + name);
				icommandsender.addChatMessage(cmc);
				return;
			} else if (astring[0].equalsIgnoreCase("edit")) {
				if (astring.length < 2) {
					cmc.appendText(EnumChatFormatting.RED  + "Usage: /aec edit configname");
					icommandsender.addChatMessage(cmc);
					return;
				}
				if (config != null) {
					cmc.appendText(EnumChatFormatting.RED  + "Close "+configName+" with /aec save or /aec discard before you open a new one.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				configName = astring[1];
				config = new AVConfig(new File(AetherCraft.getConfigDir(),configName+".cfg"));
				config.loadValues();
				cmc.appendText(EnumChatFormatting.GREEN  + "Now editing the config file "+configName+".");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("add")) {
				if (astring.length < 2) {
					cmc.appendText(EnumChatFormatting.RED  + "Usage: /aec add [item] av");
					icommandsender.addChatMessage(cmc);
					return;
				}
				if (config==null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
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
					cmc.appendText(EnumChatFormatting.RED  + "Item specified not found!");
					icommandsender.addChatMessage(cmc);
					return;
				}
				config.addValue(item, av);
				cmc.appendText(EnumChatFormatting.GREEN + item.getUnlocalizedName() + " is now worth " + av + " AV.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("save")) {
				if (config == null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				config.saveValues();
				config = null;
				cmc.appendText(EnumChatFormatting.GREEN +configName+" has been saved. Use /aec reload to apply changes.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("reload")) {
				if (config != null) {
					cmc.appendText(EnumChatFormatting.RED  + "The current config file has not yet been saved. Use /aec save or /aec discard first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				AVRegistry.reloadAllValues();
				cmc.appendText(EnumChatFormatting.GREEN +" All AV values have been updated.");
				icommandsender.addChatMessage(cmc);
				
				AetherCraftPacketHandler.HANDLER.sendToAll(new TransferConfigsPacket().setState());
			} else if (astring[0].equalsIgnoreCase("discard")) {
				if (config == null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				config = null;
				cmc.appendText(EnumChatFormatting.GREEN +"The changes to "+configName+" have been discarded.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("help")) {
				cmc.appendText("To begin editing a custom AV config file, use /aec edit configname.\n");
				cmc.appendText("To add a custom AV value, use /aec add itemname av. The item name is optional; if omitted, it will use the item in your hand.\n");
				cmc.appendText("When you're done adding AV values to the file, call /aec save to save it to the file system.\n");
				cmc.appendText("If you aren't happy with your changes, use /aec discard to cancel them.\n");
				cmc.appendText("When you've saved you configs, use /aec reload to apply your changes.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("get")) {
				if (config == null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				if (astring.length >= 2) {
					item = AVRegistry.getItemFromString(astring[1]);
				}
				if (!config.isEntry(item)) {
					cmc.appendText(EnumChatFormatting.RED+item.getUnlocalizedName()+" does not have an AV value set.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				float av = config.getValue(item);
				cmc.appendText(EnumChatFormatting.GREEN +item.getUnlocalizedName()+" is worth "+av+" AV.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("dump")) {
				if (config == null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				HashMap values = config.getValueMap();
				Iterator it = values.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry)it.next();
					item = new ItemStack((Item)Item.itemRegistry.getObject((String)((List)entry.getKey()).get(0)),1,(Integer)((List)entry.getKey()).get(1));
					cmc.appendText(EnumChatFormatting.YELLOW+""+item.getUnlocalizedName()+"="+entry.getValue()+"\n");
				}
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("del")) {
				if (config == null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				if (astring.length >= 2) {
					item = AVRegistry.getItemFromString(astring[1]);
				}
				config.deleteValue(item);
				cmc.appendText(EnumChatFormatting.GREEN +"The AV pairing for "+item.getUnlocalizedName()+" has been removed.");
				icommandsender.addChatMessage(cmc);
			} else if (astring[0].equalsIgnoreCase("addall")) {
				if (astring.length < 3) {
					cmc.appendText(EnumChatFormatting.RED  + "Usage: /aec addall av meta-to");
					icommandsender.addChatMessage(cmc);
					return;
				}
				if (config==null) {
					cmc.appendText(EnumChatFormatting.RED  + "There is no open config file. Use /aec edit first.");
					icommandsender.addChatMessage(cmc);
					return;
				}
				float av = Float.parseFloat(astring[1]);
				int metaTo =  Integer.parseInt(astring[2]);
				if (item == null) {
					cmc.appendText(EnumChatFormatting.RED  + "Item specified not found!");
					icommandsender.addChatMessage(cmc);
					return;
				}
				for (int i=0;i<=metaTo;i++) {
					config.addValue(new ItemStack(item.getItem(),1,i), av);
				}
				cmc.appendText(EnumChatFormatting.GREEN +"The subvalues of "+ item.getUnlocalizedName() + " are now worth " + av + " AV.");
				icommandsender.addChatMessage(cmc);
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			if (icommandsender instanceof EntityPlayerMP) {
				return icommandsender.canCommandSenderUseCommand(2, this.getCommandName());
			} else {
				return true;
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
