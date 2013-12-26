package net.PixelizedMC.BossMessage;

import me.confuser.barapi.BarAPI;

import org.apache.commons.lang.NumberUtils;
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


@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static String PREFIX = "§f[§bBossMessage§f] ";
    RandomExt random = new RandomExt(new Random());

    public static List<String> current = new ArrayList<>();
    public static boolean isset = false;

    @Override
    public void onEnable(){
        instance = this;
        
        Lib.onEnable();

        //config
        CM.createConfig();
        CM.readConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        //enabled

        startProcess();
    }
    
    @Override
    public void onDisable() {

    }

    private void startProcess() {
        if (!CM.enabled) {
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
	            }, CM.show*50);
	        }
        }, 20L, CM.interval + CM.show + 2L);


    }
    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args) {
    	
    	if (cmd.equalsIgnoreCase("bm") || cmd.equalsIgnoreCase("bmessage") || cmd.equalsIgnoreCase("bossmessage")){
    		if (args.length == 0) {
    			if (sender.hasPermission("bossmessage.help")) {
    				sender.sendMessage(ChatColor.DARK_AQUA + "===" + ChatColor.AQUA + " BossMessage by the Pixelized Network " + ChatColor.DARK_AQUA + "===");
    				sender.sendMessage(ChatColor.DARK_GREEN + "Usage: " + ChatColor.GREEN + "/bm <params>");
    				sender.sendMessage(ChatColor.YELLOW + "/bm add <message> <percent> - adds a message");
    				sender.sendMessage(ChatColor.YELLOW + "/bm remove <#> - removes a message");
    				sender.sendMessage(ChatColor.YELLOW + "/bm list - lists the messages");
    			}
    		} else {
    			if (args[0].equalsIgnoreCase("add")) {
    				
    				if (!sender.hasPermission("bossmessage.add")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				if (args.length != 3)
    				
    				sender.sendMessage("nyi");
    			} else if (args[0].equalsIgnoreCase("remove")) {
    				
    				if (!sender.hasPermission("bossmessage.remove")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				
    				if (NumberUtils.isNumber(args[1])) {
    					int num = Integer.parseInt(args[1]);
    					if (CM.messages.size() >= num && num > 0) {
    						CM.messages.remove(num - 1);
    						CM.rawmessages.remove(num - 1);
    						CM.config.set("BossMessage.Messages", CM.rawmessages);
    						CM.save();
    						Lib.resetCount();
    					}
    				}
    				
    				
    			} else if (args[0].equalsIgnoreCase("list")) {
    				
    				if (!sender.hasPermission("bossmessage.list")) {
    					sender.sendMessage(CM.noperm);
    					return true;
    				}
    				
    				sender.sendMessage(ChatColor.GREEN + "=== Message list ===");
    				int i = 0;
    				for (List<String> msg:CM.messages) {
    					i++;
    					sender.sendMessage(ChatColor.DARK_GREEN + "" + i + ". " + ChatColor.RESET + msg.get(0));
    				}
    			}
    		}
    	}
    	
		return false;
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
