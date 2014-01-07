package net.PixelizedMC.BossMessage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import me.confuser.barapi.BarAPI;

public class Lib {
	
	static int count = 0;
	
	public static List<String> getMessage() {
		if (CM.messages.size() > 0) {
			if (CM.random) {
				int r = Utils.randInt(0, CM.messages.size() - 1);
				List<String> message = CM.messages.get(r);
				return message;
			} else {
				List<String> message = CM.messages.get(count);
				count++;
				if (count >= CM.messages.size()) {
					resetCount();
				}
				return message;
			}
		} else {
			List<String> message = new ArrayList<>();
			message.add("§cNo messages were found! Please check your §bconfig.yml§c!");
			message.add("100");
			message.add("100");
			message.add("0");
			return message;
		}
	}
	
	public static void setPlayerMsg(Player p, List<String> msg) {
		if (msg.size() == 4) {
			if (msg.get(0) != null && NumberUtils.isNumber(msg.get(1))) {
				
				String message = generateMsg(p.getName(), msg);
				float percent = Float.parseFloat(msg.get(1));
				
				BarAPI.setMessage(p, message, percent);
			}
		}
	}
	
	public static String generateMsg(String p, List<String> m) {
		List<String> msg = m;
		String message = msg.get(0);
		
		if (msg.get(0).toLowerCase().contains("%player%".toLowerCase())) {
			message = message.replaceAll("(?i)%player%", p);
		}
		if (msg.get(0).toLowerCase().contains("%rdm_color%".toLowerCase())) {
			String colorcode;
			String colorcodes = CM.colorcodes;
			while (message.toLowerCase().contains("%rdm_color%".toLowerCase())) {
				int randint = Utils.randInt(0, colorcodes.length() - 1);
				colorcode = ChatColor.COLOR_CHAR + Character.toString(colorcodes.charAt(randint));
				message = message.replaceFirst("(?i)%rdm_color%", colorcode);
				if (!CM.repeatrdmcolors) {
					StringBuilder sb = new StringBuilder(colorcodes);
					sb.deleteCharAt(randint);
					colorcodes = sb.toString();
				}
			}
		}
		if (msg.get(0).toLowerCase().contains("%rdm_player%".toLowerCase())) {
			Player[] players = Bukkit.getOnlinePlayers();
			int randint = Utils.randInt(0, players.length - 1);
			message = message.replaceAll("(?i)%rdm_player%", players[randint].getName());
		}
		if (msg.get(0).toLowerCase().contains("%online_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%online_players%", Integer.toString(Bukkit.getOnlinePlayers().length));
		}
		if (msg.get(0).toLowerCase().contains("%max_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%max_players%", Integer.toString(Bukkit.getMaxPlayers()));
		}
		return message;
	}
	
	public static void setMsg(List<String> msg) {
		if (CM.whitelist) {
			List<String> worlds = CM.worlds;
			List<Player> players;
			for (String w:worlds) {
				if (Bukkit.getWorld(w) != null) {
					players = Bukkit.getWorld(w).getPlayers();
					for (Player p:players) {
						setPlayerMsg(p, msg);
					}
					players.clear();
				}
			}
		} else {
			for (Player p:Bukkit.getOnlinePlayers()) {
				setPlayerMsg(p, msg);
			}
		}
	}
	
	public static void resetCount() {
		count = 0;
	}
}