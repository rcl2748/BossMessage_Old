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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static String PREFIX = "§f[§bBossMessage§f] ";
    RandomExt random = new RandomExt(new Random());
    ConfigManager cm;

    public static List<String> current = new ArrayList<>();
    public static boolean isset = false;

  

    @Override
    public void onEnable(){
        instance = this;
        
        Lib.onEnable();

        regBarAPI();

        //config
        cm = new ConfigManager();
        cm.createConfig();
        cm.readConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        //enabled

        startProcess();
    }
    
    @Override
    public void onDisable() {

    }
    
    public void regBarAPI(){
        Plugin pluginBarAPI = Bukkit.getPluginManager().getPlugin("BarAPI");
        if ((pluginBarAPI != null) && (pluginBarAPI.isEnabled())) {
            //succeesfully hooked into BarAPI
        } else {
            Bukkit.getLogger().warning(PREFIX + " §4YOU NEED BARAPI IN ORDER TO RUN THIS PLUGIN");
            Bukkit.getLogger().warning("http://dev.bukkit.org/bukkit-plugins/bar-api/");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void startProcess() {
        if (!cm.enabled) {
            for (int i = 0; 3 > i ;i++)
                getLogger().warning("disabled: to enable set 'enabled' in the BossMessage config to 'true'");
            return;
        }

        BukkitScheduler scr = Bukkit.getScheduler();
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
	            }, cm.show*50);
	        }
        }, 20L, cm.interval + cm.show + 2L);


    }
    public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args) {
    	
    	if (cmd.equalsIgnoreCase("bm")) {
    		if (args.length == 0) {
    			if (sender.hasPermission("bossmessage.help")) {
    				sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
    				sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
    				sender.sendMessage(ChatColor.YELLOW + "/bm add ");
    			}
    		}
    	}
    	
		return false;
    }
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (!cm.enabled) {
            return;
        }
        
        if (cm.whitelist) {
        	List<String> worlds = cm.worlds;
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
        if (!cm.enabled) {
            return;
        }
        
        if (cm.whitelist) {
        	List<String> worlds = cm.worlds;
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
        if (!cm.enabled) {
            return;
        }
        if (isset) {
	        if (cm.whitelist) {
	        	if (cm.worlds.contains(p.getWorld().getName())) {
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
