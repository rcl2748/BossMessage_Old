package net.pixelizedmc.bossmessage.lang;

import java.util.ArrayList;
import java.util.List;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.commands.HelpCommand;
import net.pixelizedmc.bossmessage.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LangUtils {
	
	public static void broadcastError(String msg) {
		Bukkit.broadcast(Main.PREFIX_ERROR + msg, "bossmessage.seeerrors");
	}
	
	public static void sendError(CommandSender sender, String msg) {
		sender.sendMessage(Main.PREFIX_ERROR + msg);
	}
	
	public static void sendMessage(CommandSender sender, String msg) {
		sender.sendMessage(Main.PREFIX_NORMAL + msg);
	}
	
	public static void sendLiveMessage(CommandSender sender, String msg) {
		sender.sendMessage(Main.PREFIX_LIVE + msg);
	}
	
	public static void sendHelpMessage(CommandSender sender, HelpCommand cmd) {
		if (sender instanceof Player) {
			new FancyMessage(cmd.getCommand()).color(ChatColor.YELLOW).itemTooltip(getDescItem(cmd)).suggest(cmd.getCommand()).send((Player) sender);
		} else {
			sender.sendMessage(ChatColor.YELLOW + cmd.getCommand());
		}
	}
	
	public static void updateMessage(CommandSender s) {
		s.sendMessage("");
		s.sendMessage(Main.PREFIX_NORMAL_MULTILINE);
		if (s instanceof Player) {
			s.sendMessage("§eA new update (" + Main.updater_name + ") is available!");
			s.sendMessage("§ePlease choose an option below:");
			s.sendMessage("§3-----------------------------------------");
			new FancyMessage("||           ").color(ChatColor.DARK_AQUA).then("[Update]").color(ChatColor.GREEN).style(ChatColor.BOLD).command("/bm update").tooltip("§aClick to §o§nupdate automatically").then("          ").then("[Download]").color(ChatColor.RED).style(ChatColor.BOLD).link(Main.BUKKIT_LINK).tooltip("§cClick to §o§ndownload manually").then("           ||").color(ChatColor.DARK_AQUA).send(s);
		} else {
			s.sendMessage("§eA new update (" + Main.updater_name + ") is available!");
			s.sendMessage("§ePlease do §b/bm update §eto update automatically, or follow the link below to download manually: ");
			
		}
		s.sendMessage("§3-----------------------------------------");
		s.sendMessage("");
	}
	
	public static ItemStack getDescItem(HelpCommand cmd) {
		ItemStack output = new ItemStack(Material.STONE);
		ItemMeta meta = output.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + cmd.getCommand());
		List<String> lore = new ArrayList<>();
		for (String descLine : cmd.getDescription().split("\n")) {
			lore.add(ChatColor.BLUE + descLine);
		}
		lore.add(" ");
		lore.add(ChatColor.DARK_RED + "Example: " + ChatColor.GOLD + cmd.getExample());
		lore.add(" ");
		lore.add(ChatColor.RED + "" + ChatColor.ITALIC + "[Click to paste in chat]");
		meta.setLore(lore);
		output.setItemMeta(meta);
		return output;
	}
}
