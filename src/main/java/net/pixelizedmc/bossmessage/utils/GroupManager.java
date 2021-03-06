package net.pixelizedmc.bossmessage.utils;

import java.util.ArrayList;
import java.util.List;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class GroupManager {
	
	public static Messager getPlayerGroup(Player p) {
		PlayerMessager pm = PlayerMessager.getPlayerMessager(p);
		if (pm.isActive()) {
			return pm;
		}
		if (Main.useWorldGuard) {
			String region = WorldGuardManager.getRegion(p);
			if (region != null && WorldGuardManager.hasRegion(p.getWorld().getName(), region)) {
				return CM.regions.get(p.getWorld().getName()).get(region);
			}
		}
		for (String group : CM.groups) {
			if (hasAssignedPermission(p, "bossmessage.see." + group)) {
				return Main.messagers.get(group);
			}
		}
		return null;
	}
	
	public static boolean groupExists(String g) {
		return CM.groups.contains(g);
	}
	
	public static List<Player> getPlayersInGroup(String group) {
		List<Player> output = new ArrayList<Player>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			Messager g = getPlayerGroup(player);
			if (g != null && g.getGroup() == group) {
				output.add(player);
			}
		}
		return output;
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		if (sender.hasPermission("*") || sender.hasPermission(permission)) {
			return true;
		}
		for (int i = 0 ; i > -1 ; i++) {
			i = permission.indexOf(".", i);
			if (i > -1) {
				if (sender.hasPermission(permission.substring(0, i) + ".*")) {
					return true;
				}
			} else {
				break;
			}
		}
		return false;
	}
	
	public static boolean hasAssignedPermission(CommandSender sender, String permission) {
		return sender.hasPermission(new Permission(permission, PermissionDefault.FALSE));
	}
}
