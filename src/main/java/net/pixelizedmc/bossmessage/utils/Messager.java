package net.pixelizedmc.bossmessage.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Lib;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.configuration.Message;

public class Messager {
	
	public Message current;
	public int show;
	public int interval;
	public boolean isset;
	public BukkitScheduler scr = Bukkit.getScheduler();
	public int showingTaskId;
	public int delayTaskId;
	public String group;
	
	public Messager(String group) {
		this.group = group;
    	startProcess();
	}
	
	public void startProcess() {
    	if (CM.mode.equalsIgnoreCase("AutoMessage")) {
	        current = Lib.getMessage(group);
	        show = current.Show;
	        interval = current.Interval;
	        Runnable run = new Runnable() {
	    		@Override
		        public void run() {
	    			if (!Main.isBroadcasting) {
	    				Lib.setMsg(current);
	    			}
		            isset = true;
		            showingTaskId = scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
		            	public void run() {
		            		for (Player p:Bukkit.getOnlinePlayers()) {
		            			if (!Main.isBroadcasting) {
		            				BarAPI.removeBar(p);
		            			}
		            		}
		        			isset = false;
		        			delayTaskId = scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
		            			public void run() {
		    	        			startProcess();
		            			}
		            		}, interval);
		            	}
		            }, show);
		        }
	        };
	        scr.runTask(Main.getInstance(), run);
    	}
	}
}
