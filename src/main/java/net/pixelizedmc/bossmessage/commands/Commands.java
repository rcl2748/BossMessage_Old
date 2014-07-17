package net.pixelizedmc.bossmessage.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.Task;
import net.pixelizedmc.bossmessage.Updater;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.lang.Lang;
import net.pixelizedmc.bossmessage.lang.LangUtils;
import net.pixelizedmc.bossmessage.utils.GroupManager;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.Messager;
import net.pixelizedmc.bossmessage.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class Commands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args) {
		
		if (cmd.equalsIgnoreCase("bm") || cmd.equalsIgnoreCase("bmessage") || cmd.equalsIgnoreCase("bossmessage")) {
			String sendername = sender.getName();
			if (args.length == 0) {
				printHelp(sender);
				
			} else {
				// Command: ADD
				if (args[0].equalsIgnoreCase("add")) {
					if (!GroupManager.hasPermission(sender, "bossmessage.add")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 5) {
						String group = args[1].toLowerCase();
						if (GroupManager.groupExists(group)) {
							if (Utils.isInteger(args[3])) {
								if (Utils.isInteger(args[4])) {
									List<String> listmsg = new ArrayList<>();
									for (int i = 5 ; i < args.length ; i++) {
										listmsg.add(args[i]);
									}
									String textmsg = StringUtils.join(listmsg, " ");
									Message message = new Message(textmsg, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
									CM.rawmessages.get(group).add(message);
									CM.messages.get(group).add(message.color());
									CM.config.set("BossMessage.Messages." + group, CM.rawmessages.get(group));
									CM.save();
									sender.sendMessage(ChatColor.GREEN + "Your message was successfully added!");
									
								} else {
									sender.sendMessage(ChatColor.RED + args[3] + ChatColor.DARK_RED + " is not a valid number!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + args[2] + ChatColor.DARK_RED + " is not a valid number!");
							}
						} else {
							LangUtils.sendError(sender, "Group " + group + " does not exist!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm add <group> <precent> <show> <interval> <message>");
					}
				}
				// Command: REMOVE
				else if (args[0].equalsIgnoreCase("remove")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.remove")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 3) {
						String group = args[1].toLowerCase();
						if (GroupManager.groupExists(group)) {
							if (Utils.isInteger(args[2])) {
								int num = Integer.parseInt(args[2]);
								if (CM.messages.get(group).size() >= num && num > 0) {
									CM.rawmessages.get(group).remove(num - 1);
									CM.messages.get(group).remove(num - 1);
									CM.save();
									Lib.resetCount();
									sender.sendMessage(ChatColor.GREEN + "Message #" + num + " was successfully removed!");
								} else {
									LangUtils.sendError(sender, "Message " + args[2] + " was not found!");
								}
							} else {
								LangUtils.sendError(sender, args[2] + " is not a number!");
							}
						} else {
							LangUtils.sendError(sender, "Group " + group + " does not exist!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm remove <group> <#>");
					}
					
				}
				// AddGroup
				else if (args[0].equalsIgnoreCase("addgroup")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.addgroup")) {
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
							LangUtils.sendMessage(sender, "Group " + group + " was created!");
						} else {
							LangUtils.sendError(sender, "Group " + group + " already exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm addgroup <group>");
					}
					
				}
				// DelGroup
				else if (args[0].equalsIgnoreCase("delgroup")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.delgroup")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						String group = args[1].toLowerCase();
						if (CM.groups.contains(group)) {
							CM.groups.remove(group);
							CM.messages.remove(group);
							CM.rawmessages.remove(group);
							Main.messagers.remove(group).stop();
							CM.config.set("BossMessage.Messages." + group, null);
							CM.save();
							LangUtils.sendMessage(sender, "Group " + group + " was deleted!");
						} else {
							LangUtils.sendError(sender, "Group " + group + " doesn't exist!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm delgroup <group>");
					}
					
				}
				// List
				else if (args[0].equalsIgnoreCase("list")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.list")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					
					if (args.length == 1) {
						LangUtils.sendError(sender, "/bm list <group>");
						sender.sendMessage(ChatColor.YELLOW + "=== Group list ===");
						int i = 0;
						for (String group : CM.groups) {
							i++;
							sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + group);
						}
					} else if (args.length == 2) {
						String group = args[1].toLowerCase();
						if (GroupManager.groupExists(group)) {
							sender.sendMessage(ChatColor.YELLOW + "=== Message list for " + ChatColor.GOLD + group + ChatColor.YELLOW + " ===");
							int i = 0;
							for (Message msg : CM.messages.get(group)) {
								i++;
								sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.getMessage());
							}
						} else {
							LangUtils.sendError(sender, "Group " + group + " does not exist!");
						}
					}
				}
				// Command WHITELIST
				else if (args[0].equalsIgnoreCase("whitelist")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.whitelist")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						if (Utils.isBoolean(args[1])) {
							CM.whitelist = Boolean.parseBoolean(args[1]);
							CM.config.set("BossMessage.Whitelist", CM.whitelist);
							CM.save();
							sender.sendMessage(ChatColor.DARK_GREEN + "Whitelist is set to " + ChatColor.GREEN + args[1]);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm whitelist <true/false>");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm whitelist <true/false>");
					}
					
				}
				// RRC
				else if (args[0].equalsIgnoreCase("rrc")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.rrc")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						if (Utils.isBoolean(args[1])) {
							CM.repeatrdmcolors = Boolean.parseBoolean(args[1]);
							CM.config.set("BossMessage.RepeatRandomColors", CM.repeatrdmcolors);
							CM.save();
							sender.sendMessage(ChatColor.DARK_GREEN + "RepeatRandomColors is set to " + ChatColor.GREEN + args[1]);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrc <true/false>");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrc <true/false>");
					}
					
				}
				// RRP
				else if (args[0].equalsIgnoreCase("rrp")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.rrp")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						if (Utils.isBoolean(args[1])) {
							CM.repeatrdmplayers = Boolean.parseBoolean(args[1]);
							CM.config.set("BossMessage.RepeatRandomColors", CM.repeatrdmplayers);
							CM.save();
							sender.sendMessage(ChatColor.DARK_GREEN + "RepeatRandomColors is set to " + ChatColor.GREEN + args[1]);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrp <true/false>");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm rrp <true/false>");
					}
					
				}
				// Command: SETCOLORS
				else if (args[0].equalsIgnoreCase("setcolors")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.setcolors")) {
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
					
					if (!GroupManager.hasPermission(sender, "bossmessage.noperm")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 1) {
						
						List<String> listmsg = new ArrayList<>();
						for (int i = 1 ; i < args.length ; i++) {
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
						if (!GroupManager.hasPermission(sender, "bossmessage.toggle")) {
							sender.sendMessage(CM.noperm);
							return true;
						}
						if (args.length == 1) {
							if (CM.ignoreplayers.contains(sendername)) {
								CM.ignoreplayers.remove(sendername);
								CM.config.set("BossMessage.IgnoredPlayers", CM.ignoreplayers);
								CM.save();
								String group = GroupManager.getPlayerGroup(p);
								Lib.setPlayerMsg(p, Main.messagers.get(group).getCurrentMessage());
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
					
					if (!GroupManager.hasPermission(sender, "bossmessage.delworld")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						boolean contains = false;
						List<String> worlds = CM.worlds;
						String world = null;
						for (int i = 0 ; i < worlds.size() ; i++) {
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
					
					if (!GroupManager.hasPermission(sender, "bossmessage.addworld")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						boolean contains = false;
						List<String> worlds = CM.worlds;
						for (String world : worlds) {
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
					
					if (!GroupManager.hasPermission(sender, "bossmessage.random")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 1) {
						if (Utils.isBoolean(args[1])) {
							boolean random = Boolean.parseBoolean(args[1]);
							CM.random = random;
							CM.config.set("BossMessage.Random", random);
							CM.save();
							LangUtils.sendMessage(sender, ChatColor.DARK_GREEN + "Random is set to " + ChatColor.GREEN + args[1]);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm random <true/false>");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm random <true/false>");
					}
					
				}
				// Reload
				else if (args[0].equalsIgnoreCase("reload")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.reload")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length <= 1) {
						Main.scr.cancelAllTasks();
						CM.reloadConfig();
						Main.stopProcess();
						Lib.resetCount();
						Main.startProcess();
						LangUtils.sendMessage(sender, "BossMessage was successfully reloaded");
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm reload");
					}
					
				}
				// Update
				else if (args[0].equalsIgnoreCase("update")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.update.perform")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length <= 1) {
						if (Main.updater_available) {
							new Updater(Main.getInstance(), 64888, Main.file, Updater.UpdateType.NO_VERSION_CHECK, true);
							LangUtils.sendMessage(sender, "BossMessage is updated! It will be working upon restart!");
						} else {
							LangUtils.sendError(sender, "No update currently available!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm update");
					}
					
				}
				// Check
				else if (args[0].equalsIgnoreCase("check")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.update.check")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 1) {
						if (Main.updater_available) {
							Main.checkUpdate();
							if (Main.updater_available) {
								LangUtils.sendMessage(sender, "A new update (" + Main.updater_name + ") is available!");
								LangUtils.sendMessage(sender, "Please type /bm update to update it automatically, or click the link below do download it manually:");
								LangUtils.sendMessage(sender, Main.updater_link);
							} else {
								LangUtils.sendMessage(sender, "Your BossMessage is up to date.");
							}
						} else {
							LangUtils.sendMessage(sender, "Your BossMessage is up to date.");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm check");
					}
					
				}
				// Broadcast
				else if (args[0].equalsIgnoreCase("broadcast")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.broadcast.normal")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 2) {
						if (Utils.isInteger(args[1])) {
							int show = Integer.parseInt(args[1]);
							if (show > 0) {
								List<String> listmsg = new ArrayList<>();
								for (int i = 2 ; i < args.length ; i++) {
									listmsg.add(args[i]);
								}
								String textmsg = StringUtils.join(listmsg, " ");
								Message msg = new Message(textmsg, CM.broadcastPercent, show * 20, 0).color();
								for (String group : CM.groups) {
									Main.messagers.get(group).broadcast(msg);
								}
								Main.nullGroupMsgr.broadcast(msg);
								LangUtils.sendMessage(sender, "Broadcasting your message for " + show + " seconds.");
							} else {
								LangUtils.sendError(sender, "Show time must be more than 0!");
							}
						} else {
							LangUtils.sendError(sender, args[1] + " is not a valid show time!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm broadcast <sec> <message>");
					}
					
				}
				// Qb
				else if (args[0].equalsIgnoreCase("qb")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.broadcast.quick")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 1) {
						List<String> listmsg = new ArrayList<>();
						for (int i = 1 ; i < args.length ; i++) {
							listmsg.add(args[i]);
						}
						String textmsg = StringUtils.join(listmsg, " ");
						Message msg = new Message(textmsg, CM.broadcastPercent, CM.broadcastDefaultTime * 20, 0).color();
						for (String group : CM.groups) {
							Main.messagers.get(group).broadcast(msg);
						}
						LangUtils.sendMessage(sender, "QuickBroadcasting your message.");
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm qb <message>");
					}
					
				}
				// Gb
				else if (args[0].equalsIgnoreCase("gb")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.broadcast.group")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 3) {
						if (CM.groups.contains(args[1])) {
							if (Utils.isInteger(args[2])) {
								int show = Integer.parseInt(args[2]);
								if (show > 0) {
									List<String> listmsg = new ArrayList<>();
									for (int i = 3 ; i < args.length ; i++) {
										listmsg.add(args[i]);
									}
									String textmsg = StringUtils.join(listmsg, " ");
									Message msg = new Message(textmsg, CM.broadcastPercent, show * 20, 0).color();
									Main.messagers.get(args[1]).broadcast(msg);
									LangUtils.sendMessage(sender, "Broadcasting your message to group " + args[1] + " for " + args[2] + " seconds.");
								} else {
									LangUtils.sendError(sender, "Show time must be more than 0!");
								}
							} else {
								LangUtils.sendError(sender, args[2] + " is not a valid show time!");
							}
						} else {
							LangUtils.sendError(sender, args[1] + " is not a valid group!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm gb <group> <sec> <message>");
					}
					
				}
				// SetRegion
				else if (args[0].equalsIgnoreCase("setregion")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.setregion")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 4) {
						if (Main.useWorldGuard) {
							String world = args[1].toLowerCase();
							String region = args[2].toLowerCase();
							String group = args[3].toLowerCase();
							RegionManager mngr = null;
							try {
								mngr = Main.worldGuard.getRegionManager(Bukkit.getWorld(world));
							} catch (NullPointerException e) {
							}
							if (mngr != null) {
								if (mngr.hasRegion(region)) {
									Map<String, String> worldmap = CM.regions.get(world);
									if (worldmap == null) {
										CM.regions.put(world, new HashMap<String, String>());
									}
									CM.regions.get(world).put(region, group);
									CM.config.set("BossMessage.Regions." + world + "." + region, group);
									CM.save();
									LangUtils.sendMessage(sender, "Region " + region + " was set to message group " + group + " in the world " + world + "!");
								} else {
									LangUtils.sendError(sender, "Region " + region + " does not exist!");
								}
							} else {
								LangUtils.sendError(sender, "World " + world + " does not exist!");
							}
						} else {
							LangUtils.sendError(sender, "This feature is not available, as WorldGuard and/or WorldGuardRegionEvents are not installed!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm setregion <world> <region> <group>");
					}
				}
				// UnsetRegion
				else if (args[0].equalsIgnoreCase("unsetregion")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.unsetregion")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 3) {
						String world = args[1].toLowerCase();
						String region = args[2].toLowerCase();
						if (CM.config.contains("BossMessage.Regions." + world + "." + region)) {
							CM.regions.get(world).remove(region);
							CM.config.set("BossMessage.Regions." + world + "." + region, null);
							CM.save();
							LangUtils.sendMessage(sender, "Region " + region + " was unset in the world " + world + "!");
						} else {
							LangUtils.sendError(sender, "Region " + region + " in the world " + world + " either doesn't exist, or not set!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm unsetregion <world> <region>");
					}
				}
				// Schedule
				else if (args[0].equalsIgnoreCase("schedule")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.schedule")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 3) {
						if (Utils.isInteger(args[2])) {
							String sTask = args[1].toLowerCase();
							int time = Integer.parseInt(args[2]);
							if (CM.tasks.containsKey(sTask)) {
								Task task = CM.tasks.get(sTask);
								Message msg = new Message(task.getMessage(), CM.schedulePercent, time * 20, 0).color();
								for (Messager m : Main.messagers.values()) {
									m.schedule(msg, task.getCommands());
								}
							} else {
								LangUtils.sendError(sender, "Task " + sTask + " does not exists!");
							}
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm schedule <task> <sec>");
					}
				}
				// Qs
				else if (args[0].equalsIgnoreCase("qs")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.quick")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						String sTask = args[1].toLowerCase();
						if (CM.tasks.containsKey(sTask)) {
							Task task = CM.tasks.get(sTask);
							Message msg = new Message(task.getMessage(), CM.schedulePercent, CM.scheduleDefaultTime * 20, 0).color();
							for (Messager m : Main.messagers.values()) {
								m.schedule(msg, task.getCommands());
							}
						} else {
							LangUtils.sendError(sender, "Task " + sTask + " does not exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm qs <task>");
					}
				}
				// Addtask
				else if (args[0].equalsIgnoreCase("addtask")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.add")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 2) {
						String task = args[1].toLowerCase();
						List<String> listmsg = new ArrayList<>();
						for (int i = 2 ; i < args.length ; i++) {
							listmsg.add(args[i]);
						}
						String message = StringUtils.join(listmsg, ' ');
						
						if (!CM.tasks.containsKey(task)) {
							CM.tasks.put(task, new Task(message, new ArrayList<String>()));
							CM.config.set("BossMessage.Tasks." + task + ".Message", message);
							CM.config.set("BossMessage.Tasks." + task + ".Commands", new ArrayList<>());
							CM.save();
							LangUtils.sendMessage(sender, "Task " + task + " was created with a message: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message));
						} else {
							LangUtils.sendError(sender, "Task " + task + " already exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm addtask <task>");
					}
				}
				// Deltask
				else if (args[0].equalsIgnoreCase("deltask")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.remove")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						String task = args[1].toLowerCase();
						
						if (CM.tasks.containsKey(task)) {
							CM.tasks.remove(task);
							CM.config.set("BossMessage.Task." + task, null);
							CM.save();
							LangUtils.sendMessage(sender, "Task " + task + " was removed!");
						} else {
							LangUtils.sendError(sender, "Task " + task + " does not exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm deltask <task>");
					}
				}
				// Addtaskcmd
				else if (args[0].equalsIgnoreCase("addtaskcmd")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.addcmd")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length > 2) {
						String task = args[1].toLowerCase();
						List<String> listcmd = new ArrayList<>();
						for (int i = 2 ; i < args.length ; i++) {
							listcmd.add(args[i]);
						}
						String command = StringUtils.join(listcmd, ' ');
						
						if (CM.tasks.containsKey(task)) {
							Task s = CM.tasks.get(task);
							s.addCommand(command);
							CM.config.set("BossMessage.Tasks." + task + ".Commands", s.getCommands());
							CM.save();
							LangUtils.sendMessage(sender, "Added command " + ChatColor.AQUA + "/" + command + ChatColor.YELLOW + " to task " + task);
						} else {
							LangUtils.sendError(sender, "Task " + task + " does not exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm addtaskcmd <task> <cmd>");
					}
				}
				// Tasklist
				else if (args[0].equalsIgnoreCase("tasklist")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.list")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 1) {
						sender.sendMessage(ChatColor.YELLOW + "=== Task list ===");
						int i = 0;
						for (String task : CM.tasks.keySet()) {
							i++;
							sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + task);
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm tasklist");
					}
				}
				// Taskinfo
				else if (args[0].equalsIgnoreCase("taskinfo")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.task.info")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length == 2) {
						String task = args[1].toLowerCase();
						
						if (CM.tasks.containsKey(task)) {
							Task s = CM.tasks.get(task);
							sender.sendMessage(ChatColor.YELLOW + "=== " + ChatColor.GOLD + task + ChatColor.YELLOW + " info ===");
							sender.sendMessage(ChatColor.GREEN + "Message: " + ChatColor.WHITE + s.getMessage());
							sender.sendMessage(ChatColor.GREEN + "Command(s):");
							for (String command : s.getCommands()) {
								sender.sendMessage(ChatColor.AQUA + "/" + command);
							}
						} else {
							LangUtils.sendError(sender, "Task " + task + " does not exists!");
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm taskinfo <task>");
					}
				}
				// Info
				else if (args[0].equalsIgnoreCase("info")) {
					
					if (!GroupManager.hasPermission(sender, "bossmessage.info")) {
						sender.sendMessage(CM.noperm);
						return true;
					}
					if (args.length <= 1) {
						sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by Victor2748 " + ChatColor.DARK_AQUA + "===");
						sender.sendMessage(ChatColor.YELLOW + "Website: " + ChatColor.GREEN + "http://pixelizedmc.net");
						sender.sendMessage(ChatColor.YELLOW + "Official server: " + ChatColor.GREEN + "play.pixelizedmc.net");
						sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.GREEN + "Victor2748");
						sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + Main.version);
						sender.sendMessage(ChatColor.YELLOW + "BukkitDev page: " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/bossmessage/");
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/bm info");
					}
					
				}
				// Help
				else if (args[0].equalsIgnoreCase("help")) {
					
					printHelp(sender);
					
				} else {
					LangUtils.sendError(sender, ("Invalid command! Usage: " + ChatColor.RED + "/bm help"));
				}
			}
		}
		
		return false;
	}
	
	public static void printHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
		sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
		sender.sendMessage(ChatColor.ITALIC + "HOVER commands to see desc. and examples");
		for (HelpCommand cmd : Lang.commands) {
			if (GroupManager.hasPermission(sender, "bossmessage." + cmd.perm)) {
				LangUtils.sendHelpMessage(sender, cmd);
			}
		}
	}
}
