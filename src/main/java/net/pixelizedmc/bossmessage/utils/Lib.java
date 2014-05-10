package net.pixelizedmc.bossmessage.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.lang.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import me.confuser.barapi.BarAPI;

@SuppressWarnings("deprecation")
public class Lib {
	
	static Map<String, Integer> count = new HashMap<String, Integer>();
	
	public static Message getMessage(String group) {
		if (CM.messages.get(group).size() > 0) {
			if (CM.random) {
				int r = Utils.randInt(0, CM.messages.size() - 1);
				Message message = preGenMsg(CM.messages.get(group).get(r).clone());
				return message;
			} else {
				int c = count.get(group) != null ? count.get(group) : 0;
				Message message = preGenMsg(CM.messages.get(group).get(c).clone());
				c++;
				count.put(group, c);
				if (c >= CM.messages.get(group).size()) {
					resetCount(group);
				}
				return message;
			}
		} else {
			return new Message("§cNo messages were found! Please check your §bconfig.yml§c!", "100", 100, 0);
		}
	}
	
	public static Message preGenMsg(Message m) {
		//Generate string output message
		String rawmsg = m.Message;
		String message = m.Message;
		if (rawmsg.toLowerCase().contains("%rdm_color%".toLowerCase())) {
			String colorcode = null;
			int randint = 0;
			String colorcodes = CM.colorcodes;
			while (message.toLowerCase().contains("%rdm_color%".toLowerCase())) {
				if (colorcodes.length() > 0) {
					randint = Utils.randInt(0, colorcodes.length() - 1);
					colorcode = ChatColor.COLOR_CHAR + Character.toString(colorcodes.charAt(randint));
				} else {
					colorcode = ChatColor.COLOR_CHAR + "r";
				}
				message = message.replaceFirst("(?i)%rdm_color%", colorcode);
				if (!CM.repeatrdmcolors) {
					StringBuilder sb = new StringBuilder(colorcodes);
					sb.deleteCharAt(randint);
					colorcodes = sb.toString();
				}
			}
		}
		if (rawmsg.toLowerCase().contains("%rdm_player%".toLowerCase())) {
			String playername;
			List<String> playernames = getRdmPlayers();
			int randint;
			while (message.toLowerCase().contains("%rdm_player%".toLowerCase())) {
				if (playernames.size() > 0) {
					randint = Utils.randInt(0, playernames.size() - 1);
					playername = playernames.get(randint);
				} else {
					randint = -1;
					playername = "";
				}
				message = message.replaceFirst("(?i)%rdm_player%", playername);
				if (!CM.repeatrdmcolors&&randint >= 0) {
					playernames.remove(randint);
				}
			}
		}
		if (rawmsg.toLowerCase().contains("%online_players%".toLowerCase())) {
			int vplayers = 0;
			if (CM.useVNP) {
				try {
					vplayers = VanishNoPacket.numVanished();
				} catch (Exception e) {
					Main.logger.warning(Main.PREFIX_CONSOLE + "VanishNotLoadedException occured while trying to filter vanished players from %online_players%");
				}
			}
			message = message.replaceAll("(?i)%online_players%", Integer.toString(Bukkit.getOnlinePlayers().length - vplayers));
		}
		if (rawmsg.toLowerCase().contains("%max_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%max_players%", Integer.toString(Bukkit.getMaxPlayers()));
		}
		if (rawmsg.toLowerCase().contains("%server_name%".toLowerCase())) {
			message = message.replaceAll("(?i)%server_name%", Bukkit.getServerName());
		}
		m.setMessage(message);
		//Generate precentage
		String percent = m.Percent;
		if (percent.toLowerCase().contains("online_players".toLowerCase())) {
			int vplayers = 0;
			if (CM.useVNP) {
				try {
					vplayers = VanishNoPacket.numVanished();
				} catch (Exception e) {
					Main.logger.warning(Main.PREFIX_CONSOLE + "VanishNotLoadedException occured while trying to filter vanished players from ONLINE_PLAYERS in bossbar percentage");
				}
			}
			percent = percent.replaceAll("(?i)online_players", Integer.toString(Bukkit.getOnlinePlayers().length - vplayers));
		}
		if (rawmsg.toLowerCase().contains("max_players".toLowerCase())) {
			percent = percent.replaceAll("(?i)max_players", Integer.toString(Bukkit.getMaxPlayers()));
		}
		m.setPercent(percent);
		return m;
	}

	public static void setPlayerMsg(Player p, Message msg) {
		if (msg == null) {
			BarAPI.removeBar(p);
			return;
		}
		if (!CM.ignoreplayers.contains(p.getName())) {
			if (Utils.isInteger(msg.Percent)) {
				float pst = Float.parseFloat(msg.Percent);
				if (pst>100) {
					msg.setPercent("100");
				} else if (pst < 0) {
					msg.setPercent("0");
				}
			}
			Message message = generateMsg(p, msg);
			String percent = msg.Percent;
			if (msg.Percent.contains(".")||msg.Percent.contains("/")||msg.Percent.contains("*")||msg.Percent.contains("+")||msg.Percent.contains("-")||msg.Percent.contains("(")||msg.Percent.contains(")")) {
				percent = calculatePct(percent);
			}
			int time = msg.Show;
			if (!Utils.isInteger(percent)&&!msg.Percent.equalsIgnoreCase("auto")) {
	    		LangUtils.broadcastError("FAILED to parse message: output bossbar percent is NOT A NUMBER!");
	    		percent = "100";
			}
			if (msg.Percent.equalsIgnoreCase("auto")) {
				BarAPI.setMessage(p, message.Message, time/20);
			} else {
				BarAPI.setMessage(p, message.Message, Float.parseFloat(percent));
			}
		}
	}
	
	public static void setMsgs() {
		for (String group:CM.groups) {
			if (Main.messagers.get(group).isset) {
				setMsg(Main.messagers.get(group).current, group);
			} else {
				for (Player p:GroupManager.getPlayersInGroup(group)) {
					BarAPI.removeBar(p);
				}
			}
		}
	}
	
	public static Message generateMsg(Player p, Message current) {
		String playername = p.getName();
		Message msg = current;
		//Generate msg
		String message = msg.Message;
		String rawmsg = msg.Message;
		if (rawmsg.toLowerCase().contains("%player%".toLowerCase())) {
			message = message.replaceAll("(?i)%player%", playername);
		}
		if (rawmsg.toLowerCase().contains("%world%".toLowerCase())) {
			message = message.replaceAll("(?i)%world%", Bukkit.getPlayerExact(playername).getWorld().getName());
		}
		if (rawmsg.toLowerCase().contains("%world%".toLowerCase())) {
			message = message.replaceAll("(?i)%world%", Bukkit.getPlayerExact(playername).getWorld().getName());
		}
		if (rawmsg.toLowerCase().contains("%rank%".toLowerCase())) {
			if (Main.useVault) {
				message = message.replaceAll("(?i)%rank%", Main.permManager.getPrimaryGroup(p));
			} else {
				message = "§cVault is not enabled!";
			}
		}
		if (rawmsg.toLowerCase().contains("%econ_dollars%".toLowerCase())) {
			if (Main.useVault) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String dollars = money.split("\\.")[0];
				message = message.replaceAll("(?i)%econ_dollars%", dollars);
			} else {
				message = "§cVault is not enabled!";
			}
		}
		if (rawmsg.toLowerCase().contains("%econ_cents%".toLowerCase())) {
			if (Main.useVault) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String cents = money.split("\\.")[1];
				message = message.replaceAll("(?i)%econ_cents%", cents.length() > 1 ? cents : cents + "0");
			} else {
				message = "§cVault economy is not enabled!";
			}
		}
		//Generate pst
		String percent = msg.Percent;
		if (percent.toLowerCase().contains("health".toLowerCase())) {
			percent = percent.replaceAll("(?i)health", Double.toString(p.getHealth()));
		}
		if (percent.toLowerCase().contains("max_health".toLowerCase())) {
			percent = percent.replaceAll("(?i)health", Double.toString(p.getMaxHealth()));
		}
		if (percent.toLowerCase().contains("econ_dollars".toLowerCase())) {
			if (Main.useVault) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String dollars = money.split("\\.")[0];
				percent = percent.replaceAll("(?i)econ_dollars", dollars);
			} else {
				message = "§cVault economy is not enabled!";
				percent = "100";
			}
		}
		if (percent.toLowerCase().contains("econ_cents".toLowerCase())) {
			if (Main.useVault) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String cents = money.split("\\.")[1];
				percent = percent.replaceAll("(?i)econ_cents", cents.length() > 1 ? cents : cents + "0");
			} else {
				message = "§cVault economy is not enabled!";
				percent = "100";
			}
		}
		msg.setMessage(message);
		msg.setPercent(percent);
		
		return msg;
	}
	
	public static List<String> getRdmPlayers() {
		List<String> players = new ArrayList<>();
		List<String> vplayers = new ArrayList<>();
		if (CM.useVNP) {
			try {
				vplayers = new ArrayList<>(VanishNoPacket.getManager().getVanishedPlayers());
			} catch (Exception e) {
				Main.logger.warning(Main.PREFIX_CONSOLE + "VanishNotLoadedException occured while trying to filter vanished players from %rdm_player% possibilities");
			}
		}
		for (Player p:Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("bossmessage.exemptrdm") && !vplayers.contains(p.getName())) {
				players.add(p.getName());
			}
		}
		return players;
	}
	
	public static void setMsg(Message msg, String group) {
		if (CM.whitelist) {
			List<String> worlds = CM.worlds;
			List<Player> players;
			for (String w:worlds) {
				if (Bukkit.getWorld(w) != null) {
					players = Bukkit.getWorld(w).getPlayers();
					for (Player p:players) {
						if (GroupManager.getPlayerGroup(p) == group) {
							setPlayerMsg(p, preGenMsg(msg.clone()));
						}
					}
				}
			}
		} else {
			for (Player p:Bukkit.getOnlinePlayers()) {
				if (GroupManager.getPlayerGroup(p) == group) {
					setPlayerMsg(p, preGenMsg(msg.clone()));
				}
			}
		}
	}
	
	public static Message getPlayerMsg(Player player) {
		if (Main.useWorldGuard && WorldGuardManager.getRegion(player) != null) {
			if (WorldGuardManager.hasRegion(player.getWorld().getName(), WorldGuardManager.getRegion(player))) {
				return WorldGuardManager.getRegionMessage(player.getWorld().getName(), WorldGuardManager.getRegion(player));
			}
		}
		String group = GroupManager.getPlayerGroup(player);
		if (group != null) {
			return Main.messagers.get(group).getCurrentMessage();
		}
		return null;
	}
	
	public static void resetCount(String group) {
		count.remove(group);
	}
	
	public static void resetCount() {
		for (String group:CM.groups) {
			count.remove(group);
		}
	}
	
	public static String calculatePct(String percent) {
        try {
			String output = Double.toString((double) Main.engine.eval(percent)).split("\\.")[0];
			if (!Utils.isInteger(output)) {
	    		LangUtils.broadcastError("FAILED to parse message: output bossbar percent script returned NOT A NUMBER!");
				return "100";
			}
			return output;
		} catch (ScriptException e) {
    		LangUtils.broadcastError("FAILED to parse message: output bossbar script is INVALID!");
			return "100";
		}
	}
}