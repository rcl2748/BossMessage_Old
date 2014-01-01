package net.PixelizedMC.BossMessage;

import java.util.ArrayList;
import java.util.List;
import net.PixelizedMC.BossMessage.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commands {
	
    public static boolean Command(CommandSender sender, Command c, String cmd, String[] args) {
    	
    	if (cmd.equalsIgnoreCase("bm") || cmd.equalsIgnoreCase("bmessage") || cmd.equalsIgnoreCase("bossmessage")){
    		if (args.length == 0) {
    			printHelp(sender);
    			
    		} else {
    			// Command: ADD
    			if (args[0].equalsIgnoreCase("add")) {
    				
    				if (!sender.hasPermission("bossmessage.add")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 2) {
    					if (Utils.isInteger(args[1])) {
    						int percent = Integer.parseInt(args[1]);
    						if (percent < 101 && percent > 0) {
	    						
	    						List<String> listmsg = new ArrayList<>();
	    						for (int i = 2;i <= (args.length-1);i++) {
	    							listmsg.add(args[i]);
	    						}
	    						
	    						String textmsg = StringUtils.join(listmsg, " ");
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
	    						
    						} else {
    	    					sender.sendMessage(ChatColor.RED + "The percent must be 1~100!");
    						}
	    				} else {
	    					sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm add <percent> <message>");
    				}
    				
    			}
    			// Command: REMOVE
    			else if (args[0].equalsIgnoreCase("remove")) {
    				
    				if (!sender.hasPermission("bossmessage.remove")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
	    				if (Utils.isInteger(args[1])) {
	    					int num = Integer.parseInt(args[1]);
	    					if (CM.messages.size() >= num && num > 0) {
	    						CM.messages.remove(num - 1);
	    						CM.rawmessages.remove(num - 1);
	    						CM.config.set("BossMessage.Messages", CM.rawmessages);
	    						CM.save();
	    						Lib.resetCount();
	    						sender.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
	    					} else {
	    						sender.sendMessage(ChatColor.DARK_RED + "Message " + ChatColor.RED + args[1] + ChatColor.DARK_RED + "was not found!");
	    					}
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm remove <#>");
    				}
    				
    				
    			}
    			// Command: LIST
    			else if (args[0].equalsIgnoreCase("list")) {
    				
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
    			}
    			// Command: RELOAD
    			else if (args[0].equalsIgnoreCase("reload")) {
    				
    				if (!sender.hasPermission("bossmessage.reload")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}

    				Main.plm.disablePlugin(Main.plm.getPlugin("BossMessage"));
    				Main.plm.enablePlugin(Main.plm.getPlugin("BossMessage"));
    				sender.sendMessage(ChatColor.GREEN + "Plugin was successfully reloaded!");
    				
    			}
    			// Command: HELP
    			else if (args[0].equalsIgnoreCase("help")) {
    				
    				printHelp(sender);
    				
    			}
    			// Command: INTERVAL
    			else if (args[0].equalsIgnoreCase("interval")) {
    				
    				if (!sender.hasPermission("bossmessage.interval")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
	    				if (Utils.isInteger(args[1])) {
	    					int interval = Integer.parseInt(args[1]);
	    					CM.config.set("BossMessage.Interval", interval);
	    					CM.save();
	    					CM.interval = interval;
	    					Main.rsp();
	    					sender.sendMessage(ChatColor.DARK_GREEN + "The Interval is now set to: " + ChatColor.GREEN + args[1]);
	    				} else {
	    					sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm interval <ticks>");
    				}
    				
    			}
    			// Command: SHOW
    			else if (args[0].equalsIgnoreCase("show")) {
    				
    				if (!sender.hasPermission("bossmessage.show")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
	    				if (Utils.isInteger(args[1])) {
	    					int show = Integer.parseInt(args[1]);
	    					CM.config.set("BossMessage.Show", show);
	    					CM.save();
	    					CM.show = show;
	    					Main.rsp();
	    					sender.sendMessage(ChatColor.DARK_GREEN + "The Show is now set to: " + ChatColor.GREEN + args[1]);
	    				} else {
	    					sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not a valid number!");
	    				}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm show <ticks>");
    				}
    				
    			} else {
    				sender.sendMessage(ChatColor.DARK_RED + "Invalid command! Usage: " + ChatColor.RED + "/bm help");
    			}
    		}
    	}
    	
		return false;
    }
    
    public static void printHelp(CommandSender sender) {

		sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
		sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
		if (sender.hasPermission("bossmessage.add")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm add <percent> <message> - adds a message");
		}
		if (sender.hasPermission("bossmessage.remove")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm remove <#> - removes a message");
		}
		if (sender.hasPermission("bossmessage.list")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm list - lists the messages");
		}
		if (sender.hasPermission("bossmessage.reload")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm reload - reloads the plugin");
		}
		if (sender.hasPermission("bossmessage.interval")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm interval <ticks> - sets the interval between messages");
		}
		if (sender.hasPermission("bossmessage.show")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm show <ticks> - sets the message broadcast length");
		}
    }
}
