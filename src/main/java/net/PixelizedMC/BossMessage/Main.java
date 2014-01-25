package net.PixelizedMC.BossMessage;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin implements Listener {

    private static Main instance = null;
    public static String PREFIX = "§f[§bBossMessage§f] ";
	public static PluginManager plm = Bukkit.getPluginManager();
	public static BukkitScheduler scr = Bukkit.getScheduler();
    public static List<String> current = new ArrayList<>();
    public static boolean isset = false;
    public static int show;
    public static int interval;
    
    public void onEnable() {
        instance = this;
        if (plm.getPlugin("BarAPI") == null) {
        	System.out.println(ChatColor.DARK_RED + "THIS PLUGIN REQUIRES BARAPI TO BE INSTALLED!!!");
        	System.out.println(ChatColor.DARK_RED + "BOSSMESSAGE IS NOW BEING DISABLED!!!");
        	plm.disablePlugin(this);
        	scr.cancelAllTasks();
        }
        
        //config
        CM.createConfig();
        CM.readConfig();
        
        Bukkit.getPluginManager().registerEvents(this, this);
        //enabled
        startProcess();
    }
    
    public void onDisable() {

    }
    
    public void startProcess() {
        current = Lib.getMessage();
        show = Integer.parseInt(current.get(2));
        interval = Integer.parseInt(current.get(3));
        Runnable run = new Runnable() {
    		@Override
	        public void run() {
	            Lib.setMsg(current);
	            isset = true;
	            scr.scheduleSyncDelayedTask(instance, new Runnable() {
	            	public void run() {
	            		for (Player p:Bukkit.getOnlinePlayers()) {
	            			BarAPI.removeBar(p);
	            		}
	        			isset = false;
	        			scr.scheduleSyncDelayedTask(instance, new Runnable() {
	            			public void run() {
	    	        			startProcess();
	            			}
	            		}, interval);
	            	}
	            }, show);
	        }
        };
        scr.runTask(this, run);
    }
    public boolean onCommand(CommandSender a, Command b, String c, String[] d) {
    	return Commands.Command(a, b, c, d);
    }
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (!CM.enabled) {
            return;
        }
        if (p.hasPermission("bossmessage.see")) {
	        if (CM.whitelist) {
	        	List<String> worlds = CM.worlds;
	        	if (worlds.contains(e.getTo().getWorld().getName())) {
	        		Lib.setPlayerMsg(p, current);
	        	} else {
	        		BarAPI.removeBar(p);
	        	}
	        }
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (!CM.enabled) {
            return;
        }
        if (p.hasPermission("bossmessage.see")) {
	        if (CM.whitelist) {
	        	List<String> worlds = CM.worlds;
	        	if (worlds.contains(e.getTo().getWorld().getName())) {
	        		Lib.setPlayerMsg(p, current);
	        	} else {
	        		BarAPI.removeBar(p);
	        	}
	        }
        }
    }
    
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("bossmessage.see")) {
	        if (isset) {
		        if (CM.whitelist) {
		        	if (CM.worlds.contains(p.getWorld().getName())) {
		        		Lib.setPlayerMsg(p, current);
		        	}
		        } else {
		        	Lib.setPlayerMsg(p, current);
		        }
	        }
        }
    }
    
    public static Main getInstance() {
        return instance;
    }
    
}
