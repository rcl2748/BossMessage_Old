package net.PixelizedMC.BossMessage;

import java.util.ArrayList;
import java.util.List;
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
				Bukkit.broadcastMessage(message.get(0));
				count++;
				if (count >= CM.messages.size()) {
					resetCount();
				}
				return message;
			}
		} else {
			List<String> message = new ArrayList<>();
			message.add("&cNo messages were found! Please check your &bconfig.yml&c!");
			message.add("100");
			return message;
		}
	}
	
	public static void setPlayerMsg(Player p, List<String> msg) {
		if (p.hasPermission("bossmessage.see")) {
			if (msg.size() == 2) {
				if (msg.get(0) != null && Utils.isInteger(msg.get(1))) {
					
					List<String> message = generateMsg(p.getName(), msg);
					String testmsg = message.get(0);
					float percent = Float.parseFloat(message.get(1));
					
					BarAPI.setMessage(p, testmsg, percent);
				}
			}
		}
	}
	
	public static List<String> generateMsg(String p, List<String> msg) {
		String message = msg.get(0);
		
		if (message.toLowerCase().contains("%player%".toLowerCase())) {
			message = message.replaceAll("(?i)%player%", p);
		}
		if (message.toLowerCase().contains("%rdm_color%".toLowerCase())) {
			String colorcode;
			String colorcodes = CM.colorcodes;
			while (message.toLowerCase().contains("%rdm_color%".toLowerCase())) {
				int randint = Utils.randInt(0, colorcodes.length()) - 1;
				colorcode = ChatColor.COLOR_CHAR + Character.toString(colorcodes.charAt(randint));
/*				if (CM.repeatrdmcolors) {
					StringBuilder sb = new StringBuilder(colorcodes);
					sb.deleteCharAt(1);
					colorcodes = sb.toString();
				}*/
				message = message.replaceFirst("(?i)%rdm_color%", colorcode);
			}
		}
		if (message.toLowerCase().contains("%online_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%online_players%", Integer.toString(Bukkit.getOnlinePlayers().length));
		}
		if (message.toLowerCase().contains("%max_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%max_players%", Integer.toString(Bukkit.getMaxPlayers()));
		}
		msg.set(0, message);
		Bukkit.broadcastMessage("Used!");
		return msg;
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