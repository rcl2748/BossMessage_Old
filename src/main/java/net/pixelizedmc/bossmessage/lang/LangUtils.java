package net.pixelizedmc.bossmessage.lang;

import java.util.List;
import net.pixelizedmc.bossmessage.Main;
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
	public static void sendFancyMessage(CommandSender sender, String msg, String command, List<String> desc) {
		if (sender instanceof Player) {
			new FancyMessage(msg).color(ChatColor.YELLOW).tooltip(desc).suggest(command).send((Player) sender);
		} else {
			sender.sendMessage(ChatColor.YELLOW + msg);
		}
	}
}
