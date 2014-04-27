package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.lang.LangUtils;
import net.pixelizedmc.bossmessage.utils.GroupManager;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
	
	@EventHandler
	public void event(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Message msg = Lib.getPlayerMsg(p);
        if (CM.whitelist) {
        	if (CM.worlds.contains(p.getWorld().getName())) {
        		Lib.setPlayerMsg(p, msg);
        	}
        } else {
        	Lib.setPlayerMsg(p, msg);
        }
        if (GroupManager.hasPermission(p, "bossmessage.update.notify") && Main.updater_available) {
        	LangUtils.sendMessage(p, "A new update (" + Main.updater_name + ") is available!");
        	LangUtils.sendMessage(p, "Please type /bm update to update it automatically, or click the link below do download it manually:");
        	LangUtils.sendMessage(p, Main.updater_link);
        }
	}
}
