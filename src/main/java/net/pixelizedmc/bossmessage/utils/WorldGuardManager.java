package net.pixelizedmc.bossmessage.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardManager {
	
	public static Message getRegionMessage(String region) {
		if (region == null || !CM.regions.containsKey(region)) {
			return null;
		} else {
			return Main.messagers.get(CM.regions.get(region)).getCurrentMessage();
		}
	}
	
	public static boolean hasRegion(String region) {
		if (CM.regions.containsKey(region)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getRegion(Player player) {
		if (Main.useWorldGuard) {
			Location loc = player.getLocation();
			for (ProtectedRegion r:Main.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc)) {
				return r.getId();
			}
		}
		return null;
	}
	
	public static Map<String, List<String>> getWorldGuardRegions() {
		Map<String, List<String>> output = new HashMap<String, List<String>>();
		for (World w:Bukkit.getWorlds()) {
			List<String> regions = new ArrayList<>(Main.worldGuard.getRegionManager(w).getRegions().keySet());
			output.put(w.getName(), regions);
		}
		return output;
	}
}
