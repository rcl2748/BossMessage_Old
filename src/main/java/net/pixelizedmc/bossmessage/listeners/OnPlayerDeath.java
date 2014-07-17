package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.events.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void event(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        Events.onPlayerDeathByPlayer(killer, victim);
	}
}
