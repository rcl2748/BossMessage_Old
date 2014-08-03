package net.pixelizedmc.bossmessage.events;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.Messager;
import net.pixelizedmc.bossmessage.utils.PlayerMessager;
import org.bukkit.entity.Player;

public class Events {
	public static void onPVPDeath(Player killer, Player victim) {
		if (CM.onPVPDeath.isEnabled() && killer != null && victim != null) {
			Message message = CM.onPVPDeath.getMessage().color();
			String msg = message.getMessage();
			String rawmsg = msg;
			if (rawmsg.toLowerCase().contains("%killer%".toLowerCase())) {
				msg = msg.replaceAll("(?i)%killer%", killer.getName());
			}
			if (rawmsg.toLowerCase().contains("%victim%".toLowerCase())) {
				msg = msg.replaceAll("(?i)%victim%", victim.getName());
			}
			message.setMessage(msg);
			if (CM.onPVPDeath.getLevel() == EventBroadcastLevel.EVERYONE) {
				for (Messager m : Main.messagers.values()) {
					m.broadcastEvent(message);
				}
				Main.nullGroupMsgr.broadcastEvent(message);
			} else if (CM.onPVPDeath.getLevel() == EventBroadcastLevel.TARGET) {
				PlayerMessager.getPlayerMessager(victim).broadcastEvent(message);
			}
		}
	}
	
	public static void onPlayerJoin(Player player) {
		if (CM.onPlayerJoin.isEnabled() && player != null) {
			Message message = CM.onPlayerJoin.getMessage().color();
			String msg = message.getMessage();
			msg = msg.replaceAll("(?i)%target%", player.getName());
			message.setMessage(msg);
			if (CM.onPlayerJoin.getLevel() == EventBroadcastLevel.EVERYONE) {
				for (Messager m : Main.messagers.values()) {
					m.broadcastEvent(message);
				}
				Main.nullGroupMsgr.broadcastEvent(message);
			} else if (CM.onPlayerJoin.getLevel() == EventBroadcastLevel.TARGET) {
				PlayerMessager.getPlayerMessager(player).broadcastEvent(message);
			}
		}
	}
	
	public static void onPlayerQuit(Player player) {
		System.out.println("1234");
		if (CM.onPlayerQuit.isEnabled() && player != null) {
			System.out.println("123");
			Message message = CM.onPlayerQuit.getMessage().color();
			String msg = message.getMessage();
			msg = msg.replaceAll("(?i)%target%", player.getName());
			message.setMessage(msg);
			if (CM.onPlayerQuit.getLevel() == EventBroadcastLevel.EVERYONE) {
				for (Messager m : Main.messagers.values()) {
					m.broadcastEvent(message);
				}
				System.out.println("12");
				Main.nullGroupMsgr.broadcastEvent(message);
			} 
		}
	}
}
