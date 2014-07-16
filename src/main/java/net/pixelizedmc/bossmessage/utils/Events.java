package net.pixelizedmc.bossmessage.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.pixelizedmc.bossmessage.Main;

public class Events {
	public static void onPlayerDeath(Player p) {
		for (Messager m : Main.messagers.values()) {
			Bukkit.broadcastMessage("1");
			m.broadcastEvent(new Message("§ePlayer " + p.getName() + " died!", "auto", 100, 0));
		}
	}
}
