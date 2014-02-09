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
				List<String> message = preGenMsg(CM.colorMsg(CM.messages.get(r)));
				return message;
			} else {
				List<String> message;
				message = preGenMsg(CM.colorMsg(CM.messages.get(count)));
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
	
	public static List<String> preGenMsg(List<String> m) {
		//Generate string output message
		String rawmsg = m.get(0);
		String message = m.get(0);
		if (rawmsg.toLowerCase().contains("%rdm_color%".toLowerCase())) {
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
		if (rawmsg.toLowerCase().contains("%rdm_player%".toLowerCase())) {
			String playername;
			List<String> playernames = getRdmPlayers();
			while (message.toLowerCase().contains("%rdm_player%".toLowerCase())) {
				int randint = Utils.randInt(0, playernames.size() - 1);
				playername = playernames.get(randint);
				message = message.replaceFirst("(?i)%rdm_player%", playername);
				if (!CM.repeatrdmcolors) {
					playernames.remove(randint);
				}
			}
		}
		if (rawmsg.toLowerCase().contains("%rdm_player%".toLowerCase())) {
			List<String> players = getRdmPlayers();
			int randint = Utils.randInt(0, players.size() - 1);
			message = message.replaceAll("(?i)%rdm_player%", players.get(randint));
		}
		if (rawmsg.toLowerCase().contains("%online_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%online_players%", Integer.toString(Bukkit.getOnlinePlayers().length));
		}
		if (rawmsg.toLowerCase().contains("%max_players%".toLowerCase())) {
			message = message.replaceAll("(?i)%max_players%", Integer.toString(Bukkit.getMaxPlayers()));
		}
		if (rawmsg.toLowerCase().contains("%server_name%".toLowerCase())) {
			message = message.replaceAll("(?i)%server_name%", Bukkit.getServerName());
		}
		m.set(0, message);
		//Generate precentage
		String percent = m.get(1);
		if (percent.equalsIgnoreCase("online")) {
			double onlineplayers = Bukkit.getOnlinePlayers().length;
			double maxplayers = Bukkit.getMaxPlayers();
			byte ratio = (byte) Math.round(onlineplayers/maxplayers*100);
			percent = percent.replaceAll("(?i)online", Byte.toString(ratio));
		}
		m.set(1, percent);
		return m;
	}

	public static void setPlayerMsg(Player p, List<String> msg) {
		//Do a check
		if (Utils.isInteger(msg.get(1))) {
			float pst = Float.parseFloat(msg.get(1));
			if (pst>100||pst<0) {
				broadcastError("FAILED to parse message: output bossbar percent is OUT OF RANGE!");
				return;
			}
		}
		if (p.hasPermission("bossmessage.see")&&!CM.ignoreplayers.contains(p.getName())) {
			if (msg.size() == 4) {
				List<String> message = generateMsg(p, msg);
				if (!Utils.isInteger(msg.get(1))) {
		    		broadcastError("FAILED to parse message: output bossbar percent is NOT A NUMBER!");
		    		return;
				}
				if (msg.get(0).length() > 64) {
		    		broadcastError("FAILED to parse message: output bossbar message length is OUT OF RANGE!");
				}
				float percent = Float.parseFloat(msg.get(1));
				BarAPI.setMessage(p, message.get(0), percent);
			}
		}
	}
	
	public static List<String> generateMsg(Player p, List<String> m) {
		String playername = p.getName();
		List<String> msg = m;
		//Generate msg
		String message = msg.get(0);
		String rawmsg = msg.get(0);
		if (rawmsg.toLowerCase().contains("%player%".toLowerCase())) {
			message = message.replaceAll("(?i)%player%", playername);
		}
		if (rawmsg.toLowerCase().contains("%world%".toLowerCase())) {
			message = message.replaceAll("(?i)%world%", Bukkit.getPlayerExact(playername).getWorld().getName());
		}
		msg.set(0, message);
		//Generate pst
		String percent = msg.get(1);
		if (percent.equalsIgnoreCase("health")) {
			percent = percent.replaceAll("(?i)health", Double.toString(Math.round(p.getHealth()/p.getMaxHealth()*100)));
		}
		m.set(1, percent);
		
		return msg;
	}
	
	public static List<String> getRdmPlayers() {
		List<String> players = new ArrayList<>();
		for (Player p:Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("bossmessage.exemptrdm")) {
				players.add(p.getName());
			}
		}
		if (players.size() < 1) {
			players.add("NullPlayer");
		}
		return players;
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
	
	public static void broadcastError(String msg) {
		Bukkit.broadcast(Main.prefix + msg, "bossmessage.seeerrors");
	}
}