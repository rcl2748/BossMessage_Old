package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Messager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
	
	@EventHandler
	public void event(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String msgGroup = Lib.getPlayerGroup(p);
        Messager msgr = Main.messagers.get(msgGroup);
        if (msgGroup != null) {
	        if (msgr.isset) {
		        if (CM.whitelist) {
		        	if (CM.worlds.contains(p.getWorld().getName())) {
		        		if (msgr.isBroadcasting) {
		        			Lib.setPlayerMsg(p, msgr.broadcasting);
		        		} else if (msgr.isset) {
		        			Lib.setPlayerMsg(p, msgr.current);
		        		}
		        	}
		        } else {
		        	Lib.setPlayerMsg(p, msgr.current);
		        }
	        }
        }
        if (p.hasPermission("bossmessage.update.notify") && Main.updater_available) {
        	Lib.sendMessage(p, "A new update (" + Main.updater_name + ") is available!");
        	Lib.sendMessage(p, "Please type /bm update to update it automatically, or click the link below do download it manually:");
        	Lib.sendMessage(p, Main.updater_link);
        }
	}
}
