package net.pixelizedmc.bossmessage.listeners;

import net.pixelizedmc.bossmessage.utils.Lib;
import net.pixelizedmc.bossmessage.utils.WorldGuardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.mewin.WGRegionEvents.events.RegionEnteredEvent;

public class OnRegionEntered implements Listener {
	
	@EventHandler
	public void event(RegionEnteredEvent e) {
		String region = e.getRegion().getId();
		if (WorldGuardManager.hasRegion(e.getPlayer().getWorld().getName(), region)) {
			Lib.setPlayerMsg(e.getPlayer(), Lib.getPlayerMsg(e.getPlayer()));
		}
	}
}
