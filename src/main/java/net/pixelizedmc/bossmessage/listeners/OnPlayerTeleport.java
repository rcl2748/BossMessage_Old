package net.pixelizedmc.bossmessage.listeners;

import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.utils.GroupManager;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Messager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {
	
	@EventHandler
	public void event(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        String msgGroup = GroupManager.getPlayerGroup(p);
        Messager msgr = Main.messagers.get(msgGroup);
        if (msgGroup != null) {
	        if (CM.whitelist) {
	        	if (CM.worlds.contains(e.getTo().getWorld().getName())) {
	        		if (msgr.isBroadcasting) {
	        			Lib.setPlayerMsg(p, msgr.broadcasting);
	        		} else if (msgr.isset) {
	        			Lib.setPlayerMsg(p, msgr.current);
	        		}
	        	} else {
	        		BarAPI.removeBar(p);
	        	}
	        }
        } else {
    		BarAPI.removeBar(p);
    	}
	}
}
