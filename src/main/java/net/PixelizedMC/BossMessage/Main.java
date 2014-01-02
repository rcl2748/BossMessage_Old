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
import java.util.Timer;
import java.util.TimerTask;


public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static String PREFIX = "§f[§bBossMessage§f] ";
	public static PluginManager plm = Bukkit.getPluginManager();
	public static BukkitScheduler scr = Bukkit.getScheduler();
    public static List<String> current = new ArrayList<>();
    public static boolean isset = false;
    
    public void onEnable(){
        instance = this;
        if (plm.getPlugin("BarAPI") == null) {
        	System.out.println(ChatColor.DARK_RED + "THIS PLUGIN REQUIRES BARAPI TO BE INSTALLED!!!");
        	System.out.println(ChatColor.DARK_RED + "BOSSMESSAGE IS NOW BEING DISABLED!!!");
        	plm.disablePlugin(this);
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
    
    public static void rsp() {
    	scr.cancelAllTasks();
    	instance.startProcess();
    }
    
    private void startProcess() {
        if (!CM.enabled) {
            for (int i = 0; 3 > i ;i++)
                getLogger().warning("disabled: to enable set 'enabled' in the BossMessage config to 'true'");
            return;
        }
/*        Runnable run = new Runnable() {
    		@Override
	        public void run() {
	            current = Lib.getMessage();
	            Lib.setMsg(current);
	            isset = true;
	            Timer timer = new Timer();
	            timer.schedule(new TimerTask() {
	            	public void run() {
	            		for (Player p:Bukkit.getOnlinePlayers()) {
	            			BarAPI.removeBar(p);
	            		}
	        			isset = false;
	            	}
	            }, CM.show*50);
	        }
        };*/
    	scr.scheduleSyncRepeatingTask(this, new Runnable() {
    		@Override
	        public void run() {
	            current = Lib.getMessage();
	            Lib.setMsg(current);
	            isset = true;
	            Timer timer = new Timer();
	            timer.schedule(new TimerTask() {
	            	public void run() {
	            		for (Player p:Bukkit.getOnlinePlayers()) {
	            			BarAPI.removeBar(p);
	            		}
	        			isset = false;
	            	}
	            }, CM.show*50);
	        }
        }, 20L, CM.interval + CM.show + 2L);


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
        
        if (CM.whitelist) {
        	List<String> worlds = CM.worlds;
        	if (worlds.contains(e.getTo().getWorld().getName())) {
        		Lib.setPlayerMsg(p, current);
        	} else {
        		BarAPI.removeBar(p);
        	}
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (!CM.enabled) {
            return;
        }
        
        if (CM.whitelist) {
        	List<String> worlds = CM.worlds;
        	if (worlds.contains(e.getTo().getWorld().getName())) {
        		Lib.setPlayerMsg(p, current);
        	} else {
        		BarAPI.removeBar(p);
        	}
        }
    }
    
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!CM.enabled) {
            return;
        }
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
    
    public static Main getInstance() {
        return instance;
    }
    
}
