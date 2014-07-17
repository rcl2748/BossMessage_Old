package net.pixelizedmc.bossmessage.events;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.Messager;

import org.bukkit.entity.Player;

public class Events {
	public static void onPlayerDeathByPlayer(Player killer, Player victim) {
		if (CM.onPlayerDeathByPlayer.isEnabled() && killer != null && victim != null) {
			Message message = CM.onPlayerDeathByPlayer.getMessage().color();
			String msg = message.getMessage();
			String rawmsg = msg;
			if (rawmsg.toLowerCase().contains("%killer%".toLowerCase())) {
				msg = msg.replaceAll("(?i)%killer%", killer.getName());
			}
			if (rawmsg.toLowerCase().contains("%victim%".toLowerCase())) {
				msg = msg.replaceAll("(?i)%victim%", victim.getName());
			}
			message.setMessage(msg);
			for (Messager m : Main.messagers.values()) {
				m.broadcastEvent(message);
			}
		}
	}
	
	public static void onPlayerJoin(Player player) {
		if (CM.onPlayerDeathByPlayer.isEnabled() && player != null) {
			Message message = CM.onPlayerDeathByPlayer.getMessage().color();
			String msg = message.getMessage();
			msg = msg.replaceAll("(?i)%target%", player.getName());
			message.setMessage(msg);
			for (Messager m : Main.messagers.values()) {
				m.broadcastEvent(message);
			}
		}
	}
}
