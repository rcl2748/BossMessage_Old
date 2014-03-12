package net.pixelizedmc.bossmessage;

import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptException;
import net.pixelizedmc.bossmessage.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishManager;

import me.confuser.barapi.BarAPI;

public class Lib {
	
	static int count = 0;
	
	public static Message getMessage() {
		if (CM.messages.size() > 0) {
			if (CM.random) {
				int r = Utils.randInt(0, CM.messages.size() - 1);
				Message message = preGenMsg(CM.messages.get(r));
				return message;
			} else {
				Message message;
				message = preGenMsg(CM.colorMsg(CM.messages.get(count)));
				count++;
				if (count >= CM.messages.size()) {
					resetCount();
				}
				return message;
			}
		} else {
			return new Message("§cNo messages were found! Please check your §bconfig.yml§c!", "100", 100, 0, false);
		}
	}
	
	public static Message preGenMsg(Message m) {
		//Generate string output message
		String rawmsg = m.msg;
		String message = m.msg;
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
		if (rawmsg.toLowerCase().contains("%online_players%".toLowerCase())) {
			int vplayers = CM.useVNP ? new VanishManager(Main.vp).getVanishedPlayers().size() : 0;
			for (String s : new VanishManager(Main.vp).getVanishedPlayers()) {
				Bukkit.broadcastMessage(s);
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
		String percent = m.percent;
		if (percent.toLowerCase().contains("online_players".toLowerCase())) {
			int vplayers = CM.useVNP ? Main.vm.getVanishedPlayers().size() : 0;
			percent = percent.replaceAll("(?i)online_players", Integer.toString(Bukkit.getOnlinePlayers().length - vplayers));
		}
		if (rawmsg.toLowerCase().contains("max_players".toLowerCase())) {
			percent = percent.replaceAll("(?i)max_players", Integer.toString(Bukkit.getMaxPlayers()));
		}
		m.setPercent(percent);
		return m;
	}

	public static void setPlayerMsg(Player p, Message msg) {
		if (p.hasPermission("bossmessage.see")&&!CM.ignoreplayers.contains(p.getName())) {
			if (Utils.isInteger(msg.percent)) {
				float pst = Float.parseFloat(msg.percent);
				if (pst>100) {
					msg.setPercent("100");
				} else if (pst < 0) {
					msg.setPercent("0");
				}
			}
			Message message = generateMsg(p, msg);
			String percent = msg.percent;
			if (msg.calcpct) {
				percent = calculatePct(percent);
			}
			int time = msg.show;
			if (!Utils.isInteger(percent)&&!msg.percent.equalsIgnoreCase("auto")) {
	    		broadcastError("FAILED to parse message: output bossbar percent is NOT A NUMBER!");
	    		percent = "100";
			}
			if (msg.percent.equalsIgnoreCase("auto")) {
				BarAPI.setMessage(p, message.msg, time/20);
			} else {
				BarAPI.setMessage(p, message.msg, Float.parseFloat(percent));
			}
		}
	}
	
	public static Message generateMsg(Player p, Message current) {
		String playername = p.getName();
		Message msg = current;
		//Generate msg
		String message = msg.msg;
		String rawmsg = msg.msg;
		if (rawmsg.toLowerCase().contains("%player%".toLowerCase())) {
			message = message.replaceAll("(?i)%player%", playername);
		}
		if (rawmsg.toLowerCase().contains("%world%".toLowerCase())) {
			message = message.replaceAll("(?i)%world%", Bukkit.getPlayerExact(playername).getWorld().getName());
		}
		if (rawmsg.toLowerCase().contains("%econ_dollars%".toLowerCase())) {
			if (Main.useEconomy) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String dollars = money.split("\\.")[0];
				message = message.replaceAll("(?i)%econ_dollars%", dollars);
			} else {
				message = "§cVault economy is not enabled!";
			}
		}
		if (rawmsg.toLowerCase().contains("%econ_cents%".toLowerCase())) {
			if (Main.useEconomy) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String cents = money.split("\\.")[0];
				message = message.replaceAll("(?i)%econ_cents%", cents.length() > 1 ? cents : cents + "0");
			} else {
				message = "§cVault economy is not enabled!";
			}
		}
		//Generate pst
		String percent = msg.percent;
		if (percent.toLowerCase().contains("health".toLowerCase())) {
			percent = percent.replaceAll("(?i)health", Double.toString(p.getHealth()));
		}
		if (percent.toLowerCase().contains("max_health".toLowerCase())) {
			percent = percent.replaceAll("(?i)health", Double.toString(p.getMaxHealth()));
		}
		if (percent.toLowerCase().contains("econ_dollars".toLowerCase())) {
			if (Main.useEconomy) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String dollars = money.split("\\.")[0];
				percent = percent.replaceAll("(?i)%econ_dollars%", dollars);
			} else {
				message = "§cVault economy is not enabled!";
				percent = "100";
			}
		}
		if (percent.toLowerCase().contains("econ_cents".toLowerCase())) {
			if (Main.useEconomy) {
				String money = Double.toString(Main.econ.getBalance(p.getName()));
				String cents = money.split("\\.")[0];
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
		List<String> vplayers = null;
		if (CM.useVNP) {
			vplayers = new ArrayList<>(Main.vm.getVanishedPlayers());
		}
		for (Player p:Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("bossmessage.exemptrdm")&&!vplayers.contains(p.getName())) {
				players.add(p.getName());
			}
		}
		if (players.size() < 1) {
			players.add("NullPlayer");
		}
		return players;
	}
	
	public static void setMsg(Message msg) {
		if (CM.whitelist) {
			List<String> worlds = CM.worlds;
			List<Player> players;
			for (String w:worlds) {
				if (Bukkit.getWorld(w) != null) {
					players = Bukkit.getWorld(w).getPlayers();
					for (Player p:players) {
						setPlayerMsg(p, msg.clone());
					}
					players.clear();
				}
			}
		} else {
			for (Player p:Bukkit.getOnlinePlayers()) {
				setPlayerMsg(p, msg.clone());
			}
		}
	}
	
	public static void resetCount() {
		count = 0;
	}

	public static void broadcastError(String msg) {
		Bukkit.broadcast(Main.prefix_error + msg, "bossmessage.seeerrors");
	}
	public static void sendError(CommandSender p, String msg) {
		p.sendMessage(Main.prefix_error + msg);
	}
	public static void sendMessage(CommandSender p, String msg) {
		p.sendMessage(Main.prefix_normal + msg);
	}
	public static List<String> cloneMsg(List<String> msg) {
		return new ArrayList<String>(msg);
	}
	public static String calculatePct(String percent) {
        try {
			String output = Double.toString((double) Main.engine.eval(percent)).split("\\.")[0];
			if (!Utils.isInteger(output)) {
	    		broadcastError("FAILED to parse message: output bossbar percent script returned NOT A NUMBER!");
				return "100";
			}
			return output;
		} catch (ScriptException e) {
    		broadcastError("FAILED to parse message: output bossbar script is INVALID!");
			return "100";
		}
	}
}