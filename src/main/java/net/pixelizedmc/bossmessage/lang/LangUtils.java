package net.pixelizedmc.bossmessage.lang;

import java.util.ArrayList;
import java.util.List;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.commands.HelpCommand;
import net.pixelizedmc.bossmessage.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	public static void sendHelpMessage(CommandSender sender, HelpCommand cmd) {
		if (sender instanceof Player) {
			List<String> listdesc = new ArrayList<>();
			listdesc.add(ChatColor.YELLOW + "" + ChatColor.BOLD + cmd.command);
			listdesc.add(ChatColor.BLUE + cmd.description);
			listdesc.add("");
			listdesc.add(ChatColor.DARK_RED + "Example: " + ChatColor.GOLD + cmd.example);
			listdesc.add("");
			listdesc.add(ChatColor.RED + "" + ChatColor.ITALIC + "[Click to paste in chat]");
			new FancyMessage(cmd.command).color(ChatColor.YELLOW).tooltip(listdesc).suggest(cmd.command).send((Player) sender);
		} else {
			sender.sendMessage(ChatColor.YELLOW + cmd.command);
		}
	}
}
