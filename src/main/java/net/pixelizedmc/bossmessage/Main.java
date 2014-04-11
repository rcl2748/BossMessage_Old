package net.pixelizedmc.bossmessage;

import me.confuser.barapi.BarAPI;
import net.milkbowl.vault.economy.Economy;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.configuration.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class Main extends JavaPlugin implements Listener {

    private static Main instance = null;
    public static String PREFIX_ERROR = ChatColor.DARK_RED + "[" + ChatColor.RED + "BossMessage" + ChatColor.DARK_RED + "]" + ChatColor.GOLD + " ";
    public static String PREFIX_NORMAL = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "BossMessage" + ChatColor.DARK_GREEN + "]" + ChatColor.YELLOW + " ";
    public static String PREFIX_CONSOLE = "[BossMessage] ";
	public static PluginManager plm = Bukkit.getPluginManager();
	public static BukkitScheduler scr = Bukkit.getScheduler();
    public static Map<String, Message> current;
    public static Economy econ;
    public static boolean useEconomy = false;
    public static boolean isset = false;
    public static File file;
    public static boolean updater_available;
    public static String version;
    public static String updater_name;
    public static String updater_version;
    public static String updater_link;
    public static boolean isBroadcasting;
    public static Message broadcasting;
    public static int show;
    public static int interval;
    public static Logger logger = Bukkit.getLogger();
    public static ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
    public static int showingTaskId;
    public static int delayTaskId;
    public static int broadcastTaskId = -1;
    public static Map<String, Broadcaster> broadcaster
    
    public void onEnable() {
        instance = this;
        version = getDescription().getVersion();
        file = getFile();
        
        //Hook in BarAPI
        if (plm.getPlugin("BarAPI") == null) {
        	logger.severe("THIS PLUGIN REQUIRES BARAPI TO BE INSTALLED!!!");
        	logger.severe("BOSSMESSAGE IS NOW BEING DISABLED!!!");
        	plm.disablePlugin(this);
        	return;
        }
        
        //config
        CM.createConfig();
        CM.readConfig();
        
        //Register commands
        getCommand("bm").setExecutor(new Commands());
        
        //Hook in Metrics
        try {
        	Metrics metrics = new Metrics(this);
        	metrics.start();
        } catch (IOException e) {
        	logger.warning(ChatColor.RED + "BossMessage couldn't connect to Metrics :(");
        }
        
        //Hook in Vault
        if (setupEconomy()) {
        	useEconomy = true;
        	logger.info("Successfully hooked in to Vault Economy");
        } else {
        	logger.warning(PREFIX_CONSOLE + "Failed to hook in Vault's Economy! Is vault even installed?");
        }
        
        //Hook in VNP
        if (CM.useVNP) {
	        if (plm.getPlugin("VanishNoPacket") != null) {
	         	logger.info("Successfully hooked in to VanishNoPacket!");
	        } else {
	        	CM.useVNP = false;
	        	logger.warning("ATTENTION!!! VanishNoPacket is NOT installed!");
	        }
        }
        
        //Updater
        if (CM.checkUpdates) {
        	checkUpdate();
        }
        
        Bukkit.getPluginManager().registerEvents(this, this);
        //enabled
        startProcess();
    }
    
    public void onDisable() {
    	
    }
    
    public static void startProcess() {
    	if (CM.mode.equalsIgnoreCase("AutoMessage")) {
	        current = Lib.getMessage();
	        show = current.Show;
	        interval = current.Interval;
	        Runnable run = new Runnable() {
	    		@Override
		        public void run() {
	    			if (!isBroadcasting) {
	    				Lib.setMsg(current);
	    			}
		            isset = true;
		            showingTaskId = scr.scheduleSyncDelayedTask(instance, new Runnable() {
		            	public void run() {
		            		for (Player p:Bukkit.getOnlinePlayers()) {
		            			if (!isBroadcasting) {
		            				BarAPI.removeBar(p);
		            			}
		            		}
		        			isset = false;
		        			delayTaskId = scr.scheduleSyncDelayedTask(instance, new Runnable() {
		            			public void run() {
		    	        			startProcess();
		            			}
		            		}, interval);
		            	}
		            }, show);
		        }
	        };
	        scr.runTask(getInstance(), run);
    	}
    }
    
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("bossmessage.see")) {
	        if (CM.whitelist) {
	        	if (CM.worlds.contains(e.getTo().getWorld().getName())) {
	        		if (Main.isBroadcasting) {
	        			Lib.setPlayerMsg(p, broadcasting);
	        		} else if (Main.isset) {
	        			Lib.setPlayerMsg(p, current);
	        		}
	        	} else {
	        		BarAPI.removeBar(p);
	        	}
	        }
        } else {
    		BarAPI.removeBar(p);
    	}
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("bossmessage.see")) {
	        if (CM.whitelist) {
	        	if (CM.worlds.contains(e.getTo().getWorld().getName())) {
	        		if (Main.isBroadcasting) {
	        			Lib.setPlayerMsg(p, broadcasting);
	        		} else if (Main.isset) {
	        			Lib.setPlayerMsg(p, current);
	        		}
	        	} else {
	        		BarAPI.removeBar(p);
	        	}
	        }
        } else {
    		BarAPI.removeBar(p);
    	}
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("bossmessage.see")) {
	        if (isset) {
		        if (CM.whitelist) {
		        	if (CM.worlds.contains(p.getWorld().getName())) {
		        		if (Main.isBroadcasting) {
		        			Lib.setPlayerMsg(p, broadcasting);
		        		} else if (Main.isset) {
		        			Lib.setPlayerMsg(p, current);
		        		}
		        	}
		        } else {
		        	Lib.setPlayerMsg(p, current);
		        }
	        }
        }
        if (p.hasPermission("bossmessage.update.notify") && updater_available) {
        	Lib.sendMessage(p, "A new update (" + updater_name + ") is available!");
        	Lib.sendMessage(p, "Please type /bm update to update it automatically, or click the link below do download it manually:");
        	Lib.sendMessage(p, updater_link);
        }
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    public static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static void checkUpdate() {
        Updater updater = new Updater(getInstance(), 64888, file, Updater.UpdateType.NO_DOWNLOAD, false);
        updater_available = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
        updater_name = updater.getLatestName();
        updater_version = updater.getLatestGameVersion();
        updater_link = updater.getLatestFileLink();
    }
}
