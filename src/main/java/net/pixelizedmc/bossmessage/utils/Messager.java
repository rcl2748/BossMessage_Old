package net.pixelizedmc.bossmessage.utils;

import org.bukkit.Bukkit;
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
	public boolean isBroadcasting = false;
	public boolean isScheduling = false;
	public Message broadcasting;
	public Message scheduling;
    public int broadcastTaskId = -1;
    public int scheduleTaskId = -1;
    public Thread thread;
	
	public Messager(String group) {
		this.group = group;
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				startProcess();
			}
		});
		thread.start();
	}
	
	public void startProcess() {
		if (!isClosed) {
	        current = Lib.getMessage(group);
	        show = current.getShow();
	        interval = current.getInterval();
            isset = true;
            setCurrentMessage();
            showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            	public void run() {
            		if (!isBroadcasting && !isScheduling) {
	            		for (Player p : GroupManager.getPlayersInGroup(group)) {
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
//	        Runnable run = new Runnable() {
//	    		@Override
//		        public void run() {
//	    			if (!isBroadcasting) {
//	    				Lib.setMsg(current, group);
//	    			}
//		            isset = true;
//		            showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
//		            	public void run() {
//		            		if (!isBroadcasting) {
//			            		for (Player p:GroupManager.getPlayersInGroup(group)) {
//			            			BarAPI.removeBar(p);
//			            		}
//		            		}
//		        			isset = false;
//		        			delayTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
//		            			public void run() {
//		    	        			startProcess();
//		            			}
//		            		}, interval);
//		            	}
//		            }, show);
//		        }
//	        };
//	        Main.scr.runTask(Main.getInstance(), run);
		}
	}
	
	public void setCurrentMessage() { // ALways resets broadcast percentage!
		if (isBroadcasting) {
			Lib.setMsg(broadcasting, group);
		} else if (isScheduling) {
			Lib.setMsg(scheduling, group);
		} else {
			Lib.setMsg(current, group);
		}
	}
	
	public void schedule(final Message message) {
        scheduling = message;
        isScheduling = true;
        if (!isBroadcasting) {
			Lib.setMsg(Lib.preGenMsg(scheduling), group);
        }
        setCurrentMessage();
        if (scheduleTaskId != -1) {
        	Main.scr.cancelTask(scheduleTaskId);
        }
        scheduleTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
        	public void run() {
    			isScheduling = false;
    			setCurrentMessage();
        	}
        }, message.getShow());
	}
	
	public void broadcast(final Message message) {
        broadcasting = message;
        isBroadcasting = true;
		setCurrentMessage();
        if (broadcastTaskId != -1) {
        	Main.scr.cancelTask(broadcastTaskId);
        }
        broadcastTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
        	public void run() {
    			isBroadcasting = false;
    			setCurrentMessage();
        	}
        }, message.getShow());
//        Runnable run = new Runnable() {
//    		@Override
//	        public void run() {
//    			Lib.setMsg(Lib.preGenMsg(message), group);
//	            broadcasting = message;
//	            isBroadcasting = true;
//	            if (broadcastTaskId != -1) {
//	            	Main.scr.cancelTask(broadcastTaskId);
//	            }
//	            broadcastTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
//	            	public void run() {
//	        			isBroadcasting = false;
//	        			Lib.setMsg(current, group);
//	            	}
//	            }, message.Show);
//	        }
//        };
//        Main.scr.runTask(Main.getInstance(), run);
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
