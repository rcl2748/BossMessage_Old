package net.pixelizedmc.bossmessage;

import java.util.ArrayList;
import java.util.List;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.configuration.Message;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Messager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args) {
    	
    	if (cmd.equalsIgnoreCase("bm") || cmd.equalsIgnoreCase("bmessage") || cmd.equalsIgnoreCase("bossmessage")) {
    		String sendername = sender.getName();
    		if (args.length == 0) {
    			printHelp(sender);
    			
    		} else {
    			// Command: ADD
    			if (args[0].equalsIgnoreCase("add")) {
    				if (!sender.hasPermission("bossmessage.add")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 5) {
    					String group = args[1].toLowerCase();
    					if (Lib.groupExists(group)) {
							if (Utils.isInteger(args[3])) {
								if (Utils.isInteger(args[4])) {
		    						List<String> listmsg = new ArrayList<>();
		    						for (int i = 5;i < args.length;i++) {
		    							listmsg.add(args[i]);
		    						}
		    						String textmsg = StringUtils.join(listmsg, " ");
		    						if (textmsg.length() > 64) {
		    	    					sender.sendMessage(ChatColor.RED + "Message is too long!");
		    	    					return true;
		    						}
		    						Message message = new Message(textmsg, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		    						CM.rawmessages.get(group).add(message);
		    						CM.messages.get(group).add(CM.colorMsg(message));
		    						CM.config.set("BossMessage.Messages." + group, CM.rawmessages);
		    						CM.save();
		    						sender.sendMessage(ChatColor.GREEN + "Your message was successfully added!");
			    				
								} else {
									sender.sendMessage(ChatColor.RED + args[3] + ChatColor.DARK_RED + " is not a valid number!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + args[2] + ChatColor.DARK_RED + " is not a valid number!");
							}
    					} else {
    						Lib.sendError(sender, "That group doesn't exist!");
    					}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm add <group> <precent> <show> <interval> <message>");
					}
    			}
    			// Command: REMOVE
    			else if (args[0].equalsIgnoreCase("remove")) {
    				
    				if (!sender.hasPermission("bossmessage.remove")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 3) {
    					String group = args[1].toLowerCase();
    					if (Lib.groupExists(group)) {
		    				if (Utils.isInteger(args[2])) {
		    					int num = Integer.parseInt(args[2]);
		    					if (CM.messages.get(group).size() >= num && num > 0) {
		    						CM.rawmessages.get(group).remove(num - 1);
		    						CM.messages.get(group).remove(num - 1);
		    						CM.save();
		    						Lib.resetCount();
		    						sender.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
		    					} else {
		    						Lib.sendError(sender, "Message " + args[2] + " was not found!");
		    					}
		    				} else {
		    					Lib.sendError(sender, args[2] + " is not a number!");
		    				}
    					} else {
    						Lib.sendError(sender, "That group doesn't exist!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm remove <group> <#>");
    				}
    				
    			}
    			//AddGroup
    			else if (args[0].equalsIgnoreCase("addgroup")) {
    				
    				if (!sender.hasPermission("bossmessage.addgroup")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    					String group = args[1].toLowerCase();
    					if (!CM.groups.contains(group)) {
    						CM.groups.add(group);
    						CM.messages.put(group, new ArrayList<Message>());
    						CM.rawmessages.put(group, new ArrayList<Message>());
    						Main.messagers.put(group, new Messager(group));
    						CM.config.set("BossMessage.Messages." + group, new ArrayList<>());
    						CM.save();
    						Lib.sendMessage(sender, "Group " + group + " was created!");
    					} else {
    						Lib.sendError(sender, "Group " + group + " already exists!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm addgroup <group>");
    				}
    				
    			}
    			//List
    			else if (args[0].equalsIgnoreCase("list")) {
    				
    				if (!sender.hasPermission("bossmessage.list")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				
    				if (args.length == 1) {
    					Lib.sendError(sender, "/bm list <group>");
	    				sender.sendMessage(ChatColor.YELLOW + "=== Group list ===");
	    				int i = 0;
	    				for (String group:CM.groups) {
	    					i++;
	    					sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + group);
	    				}
    				}
    				else if (args.length == 2) {
    					String group = args[1].toLowerCase();
    					if (Lib.groupExists(group)) {
		    				sender.sendMessage(ChatColor.YELLOW + "=== Message list for " + ChatColor.GOLD + group + ChatColor.YELLOW + " ===");
		    				int i = 0;
		    				for (Message msg:CM.messages.get(group)) {
		    					i++;
		    					sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.Message);
		    				}
    					}
    				}
    			}
    			// Command WHITELIST
    			else if (args[0].equalsIgnoreCase("whitelist")) {
    				
    				if (!sender.hasPermission("bossmessage.whitelist")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    					if (Utils.isBoolean(args[1])) {
    						boolean whitelist = Boolean.parseBoolean(args[1]);
    						CM.whitelist = whitelist;
    						CM.config.set("BossMessage.Whitelist", whitelist);
    						CM.save();
    						sender.sendMessage(ChatColor.DARK_GREEN + "Whitelist is set to " + ChatColor.GREEN + args[1]);
    					} else {
        					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm whitelist <true/false>");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm whitelist <true/false>");
    				}
    				
    			}
    			// Command: RRC
    			else if (args[0].equalsIgnoreCase("rrc")) {
    				
    				if (!sender.hasPermission("bossmessage.rrc")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    					if (Utils.isBoolean(args[1])) {
    						boolean rrc = Boolean.parseBoolean(args[1]);
    						CM.repeatrdmcolors = rrc;
    						CM.config.set("BossMessage.RepeatRandomColors", rrc);
    						CM.save();
    						sender.sendMessage(ChatColor.DARK_GREEN + "RepeatRandomColors is set to " + ChatColor.GREEN + args[1]);
    					} else {
        					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrc <true/false>");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrc <true/false>");
    				}
    				
    			}
    			// Command: SETCOLORS
    			else if (args[0].equalsIgnoreCase("setcolors")) {
    				
    				if (!sender.hasPermission("bossmessage.setcolors")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    						CM.colorcodes = args[1];
    						CM.config.set("BossMessage.ColorCodes", args[1]);
    						CM.save();
    						sender.sendMessage(ChatColor.DARK_GREEN + "Colorcodes are now: " + ChatColor.GREEN + args[1]);
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm setcolors <colorcodes>");
    				}
    				
    			}
    			// Command: NOPERM
    			else if (args[0].equalsIgnoreCase("noperm")) {
    				
    				if (!sender.hasPermission("bossmessage.noperm")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 1) {

						List<String> listmsg = new ArrayList<>();
						for (int i = 1;i < args.length;i++) {
							listmsg.add(args[i]);
						}
						String msg = StringUtils.join(listmsg, " ");
						
						CM.noperm = msg;
						CM.config.set("BossMessage.NoPermission", ChatColor.translateAlternateColorCodes('&', msg));
						CM.save();
    						sender.sendMessage(ChatColor.DARK_GREEN + "NoPermission message is now: " + ChatColor.RESET + msg);
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm noperm <msg>");
    				}
    				
    			}
    			// Command: TOGGLE
    			else if (args[0].equalsIgnoreCase("toggle")) {
    				if (sender instanceof Player) {
    					Player p = (Player) sender;
	    				if (!sender.hasPermission("bossmessage.toggle")) {
	    					sender.sendMessage(CM.noperm);
	    					return true;
	    				}
	    				if (args.length == 1) {
	    					if (CM.ignoreplayers.contains(sendername)) {
	    						CM.ignoreplayers.remove(sendername);
	    						CM.config.set("BossMessage.IgnoredPlayers", CM.ignoreplayers);
	    						CM.save();
	    						String group = Lib.getPlayerGroup(p);
	    						if (Main.messagers.get(group).isset) {
	    							Lib.setPlayerMsg(p, Main.messagers.get(group).current);
	    						}
	        					sender.sendMessage(ChatColor.GREEN + "BossMessages were successfully enabled");
	    					} else {
	    						CM.ignoreplayers.add(sendername);
	    						CM.config.set("BossMessage.IgnoredPlayers", CM.ignoreplayers);
	    						CM.save();
	    						BarAPI.removeBar(p);
	        					sender.sendMessage(ChatColor.GREEN + "BossMessages were successfully disabled");
	    					}
	    				} else {
	    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm toggle");
	    				}
    				} else {
    					sender.sendMessage(ChatColor.RED + "This command can only be used as a player");
    				}
    				
    			}
    			// Command: DELWORLD
    			else if (args[0].equalsIgnoreCase("delworld")) {
    				
    				if (!sender.hasPermission("bossmessage.delworld")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    					boolean contains = false;
						List<String> worlds = CM.worlds;
						String world = null;
    					for (int i=0;i<worlds.size();i++) {
    						if (worlds.get(i).equalsIgnoreCase(args[1])) {
    							world = worlds.remove(i);
        						CM.worlds = worlds;
        						CM.config.set("BossMessage.WhitelistedWorlds", worlds);
        						CM.save();
        						contains = true;
    						}
    					}
    					if (contains) {
    						sender.sendMessage(ChatColor.DARK_GREEN + "World " + ChatColor.GREEN + world + ChatColor.DARK_GREEN + " was removed from whitelist!");
    					} else {
        					sender.sendMessage(ChatColor.DARK_RED + "World " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " is not whitelisted!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm delworld <world>");
    				}
    				
    			}
    			// Command: ADDWORLD
    			else if (args[0].equalsIgnoreCase("addworld")) {
    				
    				if (!sender.hasPermission("bossmessage.addworld")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 2) {
    					boolean contains = false;
						List<String> worlds = CM.worlds;
    					for (String world:worlds) {
    						if (world.equalsIgnoreCase(args[1])) {
        						contains = true;
    						}
    					}
    					if (!contains) {
    						worlds.add(args[1].toLowerCase());
    						CM.worlds = worlds;
    						CM.config.set("BossMessage.WhitelistedWorlds", worlds);
    						CM.save();
    						sender.sendMessage(ChatColor.DARK_GREEN + "World " + ChatColor.GREEN + args[1] + ChatColor.DARK_GREEN + " was added to whitelist!");
    					} else {
        					sender.sendMessage(ChatColor.DARK_RED + "World " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " is already whitelisted!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm addworld <world>");
    				}
    				
    			}
    			// Command: RANDOM
    			else if (args[0].equalsIgnoreCase("random")) {
    				
    				if (!sender.hasPermission("bossmessage.random")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 1) {
    					if (Utils.isBoolean(args[1])) {
    						boolean random = Boolean.parseBoolean(args[1]);
    						CM.random = random;
    						CM.config.set("BossMessage.Random", random);
    						CM.save();
    						Lib.sendMessage(sender, ChatColor.DARK_GREEN + "Random is set to " + ChatColor.GREEN + args[1]);
    					} else {
        					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm random <true/false>");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm random <true/false>");
    				}
    				
    			}
    			//Reload
    			else if (args[0].equalsIgnoreCase("reload")) {
    				
    				if (!sender.hasPermission("bossmessage.reload")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length <= 1) {
    					Main.scr.cancelAllTasks();
    					CM.reloadConfig();
    					Main.stopProcess();
    					Lib.resetCount();
    					Main.startProcess();
    					Lib.sendMessage(sender, ChatColor.GREEN + "BossMessage was successfully reloaded");
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm reload");
    				}
    				
    			}
    			//Update
    			else if (args[0].equalsIgnoreCase("update")) {
    				
    				if (!sender.hasPermission("bossmessage.update.perform")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length <= 1) {
    					if (Main.updater_available) {
    						new Updater(Main.getInstance(), 64888, Main.file, Updater.UpdateType.NO_VERSION_CHECK, true);
    						Lib.sendMessage(sender, "BossMessage is updated! It will be working upon restart!");
    					} else {
    						Lib.sendError(sender, "No update currently available!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm update");
    				}
    				
    			}
    			//Check
    			else if (args[0].equalsIgnoreCase("check")) {
    				
    				if (!sender.hasPermission("bossmessage.update.check")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length == 1) {
    					if (Main.updater_available) {
    						Main.checkUpdate();
    						if (Main.updater_available) {
    				        	Lib.sendMessage(sender, "A new update (" + Main.updater_name + ") is available!");
    				        	Lib.sendMessage(sender, "Please type /bm update to update it automatically, or click the link below do download it manually:");
    				        	Lib.sendMessage(sender, Main.updater_link);
    						} else {
    				        	Lib.sendMessage(sender, "Your BossMessage is up to date.");
    						}
    					} else {
    						Lib.sendMessage(sender, "Your BossMessage is up to date.");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm check");
    				}
    				
    			}
    			//Broadcast
    			else if (args[0].equalsIgnoreCase("broadcast")) {
    				
    				if (!sender.hasPermission("bossmessage.broadcast.normal")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 2) {
    					if (Utils.isInteger(args[1])) {
    						int show = Integer.parseInt(args[1]);
    						if (show > 0) {
	    						List<String> listmsg = new ArrayList<>();
	    						for (int i = 2;i < args.length;i++) {
	    							listmsg.add(args[i]);
	    						}
	    						String textmsg = StringUtils.join(listmsg, " ");
	    						Message msg = new Message(textmsg, CM.broadcastPercent, show*20, 0);
	    						for (String group:CM.groups) {
	    							Main.messagers.get(group).broadcast(msg);
	    						}
    							Lib.sendMessage(sender, "Broadcasting your message for " + show + " seconds.");
    						} else {
    							Lib.sendError(sender, "Show time must be more than 0!");
    						}
    					} else {
    						Lib.sendError(sender, args[1] + " is not a valid show time!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm broadcast <sec> <message>");
    				}
    				
    			}
    			//Qb
    			else if (args[0].equalsIgnoreCase("qb")) {
    				
    				if (!sender.hasPermission("bossmessage.broadcast.quick")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 1) {
						List<String> listmsg = new ArrayList<>();
						for (int i = 1;i < args.length;i++) {
							listmsg.add(args[i]);
						}
						String textmsg = StringUtils.join(listmsg, " ");
						Message msg = new Message(textmsg, CM.broadcastPercent, CM.broadcastDefaultTime*20, 0);
						for (String group:CM.groups) {
							Main.messagers.get(group).broadcast(msg);
						}
						Lib.sendMessage(sender, "QuickBroadcasting your message.");
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm qb <message>");
    				}
    				
    			}
    			//Gb
    			else if (args[0].equalsIgnoreCase("gb")) {
    				
    				if (!sender.hasPermission("bossmessage.broadcast.group")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length > 3) {
    					if (CM.groups.contains(args[1])) {
	    					if (Utils.isInteger(args[2])) {
	    						int show = Integer.parseInt(args[2]);
	    						if (show > 0) {
		    						List<String> listmsg = new ArrayList<>();
		    						for (int i = 3;i < args.length;i++) {
		    							listmsg.add(args[i]);
		    						}
		    						String textmsg = StringUtils.join(listmsg, " ");
		    						Message msg = new Message(textmsg, CM.broadcastPercent, show*20, 0);
		    						Main.messagers.get(args[1]).broadcast(msg);
	    							Lib.sendMessage(sender, "Broadcasting your message to group " + args[1] + " for " + args[2] + " seconds.");
	    						} else {
	    							Lib.sendError(sender, "Show time must be more than 0!");
	    						}
	    					} else {
	    						Lib.sendError(sender, args[2] + " is not a valid show time!");
	    					}
    					} else {
    						Lib.sendError(sender, args[1] + " is not a valid group!");
    					}
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm gb <group> <sec> <message>");
    				}
    				
    			}
    			//Info
    			else if (args[0].equalsIgnoreCase("info")) {
    				
    				if (!sender.hasPermission("bossmessage.info")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length <= 1) {
				    	sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
				    	sender.sendMessage(ChatColor.YELLOW + "Website: " + ChatColor.GREEN + "http://pixelizedmc.net");
				    	sender.sendMessage(ChatColor.YELLOW + "Official server: " + ChatColor.GREEN + "play.pixelizedmc.net");
				    	sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.GREEN + "Victor2748");
				    	sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + Main.version);
				    	sender.sendMessage(ChatColor.YELLOW + "BukkitDev page: " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/bossmessage/");
    				} else {
    					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm info");
    				}
    				
    			}
    			// Command: HELP
    			else if (args[0].equalsIgnoreCase("help")) {
    				
    				printHelp(sender);
    				
    			} else {
    				Lib.sendError(sender, ("Invalid command! Usage: " + ChatColor.RED + "/bm help"));
    			}
    		}
    	}
    	
		return false;
    }
    
    public static void printHelp(CommandSender sender) {
    	sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
		sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
		if (sender.hasPermission("bossmessage.add")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm add <percent> <show> <interval> <message> " + ChatColor.RED + "-" + ChatColor.RESET + " adds a message");
		}
		if (sender.hasPermission("bossmessage.remove")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm remove <#> " + ChatColor.RED + "-" + ChatColor.RESET + " removes a message");
		}
		if (sender.hasPermission("bossmessage.reload")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm reload " + ChatColor.RED + "-" + ChatColor.RESET + " reloads the config");
		}
		if (sender.hasPermission("bossmessage.list")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm list " + ChatColor.RED + "-" + ChatColor.RESET + " lists the messages");
		}
		if (sender.hasPermission("bossmessage.whitelist")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm whitelist <true/false> " + ChatColor.RED + "-" + ChatColor.RESET + " toggles the whitelist");
		}
		if (sender.hasPermission("bossmessage.rrc")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm rrc <true/false> " + ChatColor.RED + "-" + ChatColor.RESET + " toggles random color repeating");
		}
		if (sender.hasPermission("bossmessage.random")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm random <true/false> " + ChatColor.RED + "-" + ChatColor.RESET + " toggles random message picking");
		}
		if (sender.hasPermission("bossmessage.addworld")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm addworld <world> " + ChatColor.RED + "-" + ChatColor.RESET + " adds a world to the whitelist");
		}
		if (sender.hasPermission("bossmessage.delworld")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm delworld <world> " + ChatColor.RED + "-" + ChatColor.RESET + " removes a world from the whitelist");
		}
		if (sender.hasPermission("bossmessage.noperm")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm noperm <msg> " + ChatColor.RED + "-" + ChatColor.RESET + " sets the NoPermission message");
		}
		if (sender.hasPermission("bossmessage.setcolors")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm setcolors <colorcodes> " + ChatColor.RED + "-" + ChatColor.RESET + " sets the random color list");
		}
		if (sender.hasPermission("bossmessage.setcolors")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm toggle " + ChatColor.RED + "-" + ChatColor.RESET + " toggles your message view");
		}
		if (sender.hasPermission("bossmessage.update.perform")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm update " + ChatColor.RED + "-" + ChatColor.RESET + " updates BossMessage to the latest version");
		}
		if (sender.hasPermission("bossmessage.update.check")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm check " + ChatColor.RED + "-" + ChatColor.RESET + " checks for updates");
		}
		if (sender.hasPermission("bossmessage.broadcast.normal")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm broadcast <sec> <message> " + ChatColor.RED + "-" + ChatColor.RESET + " broadcasts the message");
		}
		if (sender.hasPermission("bossmessage.broadcast.quick")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm qb <message> " + ChatColor.RED + "-" + ChatColor.RESET + " quick-broadcasts the message");
		}
		if (sender.hasPermission("bossmessage.broadcast.group")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm gb <group> <sec> <message> " + ChatColor.RED + "-" + ChatColor.RESET + " broadcasts the message for a group");
		}
		if (sender.hasPermission("bossmessage.info")) {
			sender.sendMessage(ChatColor.YELLOW + "/bm info " + ChatColor.RED + "-" + ChatColor.RESET + " displays the info");
		}
    }
}
