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
	public boolean isBroadcasting;
	public Message broadcasting;
    public int broadcastTaskId = -1;
	
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
		    			if (!isBroadcasting) {
		    				Lib.setMsg(current, group);
		    			}
			            isset = true;
			            showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			            	public void run() {
			            		for (Player p:Bukkit.getOnlinePlayers()) {
			            			if (!isBroadcasting) {
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
	
	public void broadcast(final Message message) {
        Runnable run = new Runnable() {
    		@Override
	        public void run() {
    			Lib.setMsg(message, group);
	            broadcasting = message;
	            isBroadcasting = true;
	            if (broadcastTaskId != -1) {
	            	Main.scr.cancelTask(broadcastTaskId);
	            }
	            broadcastTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
	            	public void run() {
	        			isBroadcasting = false;
	        			Lib.setMsg(current, group);
	            	}
	            }, message.Show);
	        }
        };
        Main.scr.runTask(Main.getInstance(), run);
	}
	
	public void stop() {
		this.isClosed = true;
		if (!isset) {
			Main.scr.cancelTask(delayTaskId);
		}
		Main.scr.cancelTask(showingTaskId);
	}
}