package net.pixelizedmc.bossmessage.listeners;

import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.WorldGuardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mewin.WGRegionEvents.events.RegionLeftEvent;

public class OnRegionLeft implements Listener {
	
	@EventHandler
	public void event(RegionLeftEvent e) {
		String region = e.getRegion().getId();
		Message msg = Lib.getPlayerMsg(e.getPlayer());
		if (WorldGuardManager.hasRegion(e.getPlayer().getWorld().getName(), region) && msg != null) {
			Lib.setPlayerMsg(e.getPlayer(), Lib.preGenMsg(msg));
		} else {
			BarAPI.removeBar(e.getPlayer());
		}
	}
}
