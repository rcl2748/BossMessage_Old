package me.michidk.BossMessage;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: ml
 * Date: 02.09.13
 * Time: 04:21
 * To change this template use File | Settings | File Templates.
 */
public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static String PREFIX = "§f[§bBossMessage§f] ";
    RandomExt random = new RandomExt(new Random());
    ConfigManager cm;

    public static List<String> current = new ArrayList<>();

    BarAPI barAPI;

    @Override
    public void onEnable(){
        instance = this;

        //reg api
        barAPI = new BarAPI();
        Bukkit.getPluginManager().registerEvents(barAPI, this);

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

    private void startProcess() {
        if (!cm.enabled) {
            for (int i = 0; 3 > i ;i++)
                getLogger().warning("disabled: to enable set 'enabled' in the BossMessage config to 'true'");
            return;
        }

        final Plugin plugin = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                current = getMessage();
                //BossBroadcastAPI.timedMessage(plugin, message, cm.interval*20);
                //Bukkit.broadcastMessage(current + " is now the brot");
                setMsg(current.get(0), Float.parseFloat(current.get(1)));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                	public void run() {
                		for (Player p:Bukkit.getOnlinePlayers()) {
                			BarAPI.removeBar(p);
                		}
                	}
                }, cm.show*1000);
            }
        }, 20L, (cm.interval + cm.show)*20L + 2L);


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
        		BarAPI.setMessage(p, current.get(0), Float.parseFloat(current.get(1)));
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
        		BarAPI.setMessage(p, current.get(0), Float.parseFloat(current.get(1)));
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
        
        if (cm.whitelist) {
        	if (cm.worlds.contains(p.getWorld().getName())) {
        		BarAPI.setMessage(p, current.get(0), Float.parseFloat(current.get(1)));
        	}
        } else {
        	BarAPI.setMessage(p, current.get(0), Float.parseFloat(current.get(1)));
        }
    }

    private int count = 0;

    public List<String> getMessage() {
        if (cm.random) {
            List<List<String>> messages = cm.messages;
            int r = random.randInt(0, messages.size() - 1);
            String coloredmsg =  ChatColor.translateAlternateColorCodes('&', messages.get(r).get(0));
            messages.get(r).set(0, coloredmsg);
            return messages.get(r);
        } else {
        	List<List<String>> messages = cm.messages;
            List<String> message = messages.get(count);
            count++;
            if (count >= messages.size())
                count = 0;
            String coloredmsg =  ChatColor.translateAlternateColorCodes('&', message.get(0));
            message.set(0, coloredmsg);
            return message;
        }
    }
    
    public void setMsg(String msg, float persent) {
    	if (cm.whitelist) {
    		List<String> worlds = cm.worlds;
    		List<Player> players;
    		for (String w:worlds) {
    			if (Bukkit.getWorld(w) != null) {
    				players = Bukkit.getWorld(w).getPlayers();
	    			for (Player p:players) {
	    				BarAPI.setMessage(p, msg, persent);
	    			}
	    			players.clear();
    			}
    		}
        } else {
        	for (Player p:Bukkit.getOnlinePlayers()) {
    			BarAPI.setMessage(p, msg, persent);
    		}
        }
    }

    public static Main getInstance() {
        return instance;
    }

}
