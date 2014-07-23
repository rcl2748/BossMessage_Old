package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
	
	@EventHandler
	public void event(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Events.onPlayerQuit(p);
	}
}
