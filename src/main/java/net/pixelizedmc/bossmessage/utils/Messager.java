package net.pixelizedmc.bossmessage.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.configuration.Message;

public class Messager {
	
	public Message current;
	public int show;
	public int interval;
	public boolean isset;
	public int showingTaskId;
	public int delayTaskId;
	public String group;
	public boolean isClosed = false;
	
	public Messager(String group) {
		this.group = group;
    	startProcess();
	}
	
	public void startProcess() {
		if (!this.isClosed) {
	    	if (CM.mode.equalsIgnoreCase("AutoMessage")) {
		        current = Lib.getMessage(group);
		        show = current.Show;
		        interval = current.Interval;
		        Runnable run = new Runnable() {
		    		@Override
			        public void run() {
		    			if (!Main.isBroadcasting) {
		    				Lib.setMsg(current, group);
		    			}
			            isset = true;
			            showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			            	public void run() {
			            		for (Player p:Bukkit.getOnlinePlayers()) {
			            			if (!Main.isBroadcasting) {
			            				BarAPI.removeBar(p);
			            			}
			            		}
			        			isset = false;
			        			delayTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			            			public void run() {
			    	        			startProcess();
			            			}
			            		}, interval);
			            	}
			            }, show);
			        }
		        };
		        Main.scr.runTask(Main.getInstance(), run);
	    	}
		}
	}
	
	public void stop() {
		this.isClosed = true;
		Main.scr.cancelTask(delayTaskId);
		Main.scr.cancelTask(showingTaskId);
	}
}
