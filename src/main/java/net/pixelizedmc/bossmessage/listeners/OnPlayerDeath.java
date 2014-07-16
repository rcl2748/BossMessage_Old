package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.utils.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void event(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Events.onPlayerDeath(p);
	}
}
