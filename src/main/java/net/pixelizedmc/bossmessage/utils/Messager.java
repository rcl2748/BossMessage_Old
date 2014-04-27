package net.pixelizedmc.bossmessage.utils;

import org.bukkit.entity.Player;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;

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
		if (!isClosed) {
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
		            		if (!isBroadcasting) {
			            		for (Player p:GroupManager.getPlayersInGroup(group)) {
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
	
	public Message getCurrentMessage() {
		if (isBroadcasting) {
			return broadcasting;
		} else if (isset) {
			return current;
		}
		return null;
	}
	
	public void stop() {
		this.isClosed = true;
		if (!isset) {
			Main.scr.cancelTask(delayTaskId);
		}
		Main.scr.cancelTask(showingTaskId);
	}
}
