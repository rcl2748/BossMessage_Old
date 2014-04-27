package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {
	
	@EventHandler
	public void event(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Message msg = Lib.getPlayerMsg(p);
        if (CM.whitelist) {
        	if (CM.worlds.contains(p.getWorld().getName())) {
        		Lib.setPlayerMsg(p, msg);
        	}
        } else {
        	Lib.setPlayerMsg(p, msg);
        }
	}
}
