package net.PixelizedMC.BossMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

@SuppressWarnings("deprecation")
public class Commands {
	
	public static PluginManager plm = Bukkit.getPluginManager();
	
    public static boolean Command(CommandSender sender, Command c, String cmd, String[] args) {
    	
    	if (cmd.equalsIgnoreCase("bm") || cmd.equalsIgnoreCase("bmessage") || cmd.equalsIgnoreCase("bossmessage")){
    		if (args.length == 0) {
    			printHelp(sender);
    			
    		} else {
    			if (args[0].equalsIgnoreCase("add")) {
    				
    				if (!sender.hasPermission("bossmessage.add")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 2) {
    					if (NumberUtils.isNumber(args[1])) {
    						
    						List<String> arraymsg = Arrays.asList(args);
    						String textmsg = StringUtils.join(arraymsg, " ");
    						Bukkit.broadcastMessage(textmsg);
    						List<String> rawmessage = new ArrayList<>();
    						rawmessage.add(textmsg);
    						rawmessage.add(args[1]);
    						

    						List<String> message = new ArrayList<>();
    						message.add(textmsg);
    						message.add(args[1]);
    						message.set(0, ChatColor.translateAlternateColorCodes('&', message.get(0)));
    						
    						CM.messages.add(message);
    						CM.rawmessages.add(rawmessage);
    						CM.config.set("BossMessage.Messages", CM.rawmessages);
    						CM.save();
    						sender.sendMessage(ChatColor.GREEN + "Your message was successfully added!");
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm add <percent> <message>");
    				}
    				
    			} else if (args[0].equalsIgnoreCase("remove")) {
    				
    				if (!sender.hasPermission("bossmessage.remove")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
	    				if (NumberUtils.isNumber(args[1])) {
	    					int num = Integer.parseInt(args[1]);
	    					if (CM.messages.size() >= num && num > 0) {
	    						CM.messages.remove(num - 1);
	    						CM.rawmessages.remove(num - 1);
	    						CM.config.set("BossMessage.Messages", CM.rawmessages);
	    						for (List<String> msg:CM.rawmessages) {
	    							Bukkit.broadcastMessage(msg.get(0));
	    						}
	    						CM.save();
	    						Lib.resetCount();
	    						sender.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
	    					}
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm remove <#>");
    				}
    				
    				
    			} else if (args[0].equalsIgnoreCase("list")) {
    				
    				if (!sender.hasPermission("bossmessage.list")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				
    				sender.sendMessage(ChatColor.GREEN + "=== Message list ===");
    				int i = 0;
    				for (List<String> msg:CM.messages) {
    					i++;
    					sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.get(0));
    				}
    			} else if (args[0].equalsIgnoreCase("reload")) {
    				
    				if (!sender.hasPermission("bossmessage.reload")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				
    				plm.disablePlugin(Main.getInstance());
    				plm.enablePlugin(Main.getInstance());
    				
    			} else {
    				sender.sendMessage(ChatColor.RED + "");
    			}
    		}
    	}
    	
		return false;
    }
    
    public static void printHelp(CommandSender sender) {

		sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
		sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
		if (sender.hasPermission("bossmessage.add")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm add <message> <percent> - adds a message");
		}
		if (sender.hasPermission("bossmessage.remove")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm remove <#> - removes a message");
		}
		if (sender.hasPermission("bossmessage.list")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm list - lists the messages");
		}
    }
}
