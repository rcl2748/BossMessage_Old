package net.pixelizedmc.bossmessage.utils;

import java.util.List;
import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messager {
	
	protected Message current;
	protected int show;
	protected int interval;
	protected boolean set;
	protected int showingTaskId;
	protected int delayTaskId;
	protected String group;
	protected double messageAutoReduceBy;
	protected double messageAutoLastPercent;
	protected Message scheduling;
	protected boolean isScheduling = false;
	protected int scheduleAutoTaskId = -1;
	protected double scheduleAutoLastPercent;
	protected int scheduleTaskId = -1;
	protected Message broadcasting;
	protected int broadcastTaskId = -1;
	protected Thread thread;
	protected boolean messageAutoLoop = false;
	protected double broadcastAutoLastPercent;
	protected boolean isBroadcasting = false;
	protected int broadcastAutoTaskId = -1;
	protected boolean isBroadcastingEvent = false;
	protected int eventAutoTaskId = -1;
	protected int eventTaskId = -1;
	protected Message broadcastingEvent;
	protected double eventAutoLastPercent;
	
	public Message getCurrent() {
		return current;
	}
	
	public boolean isSet() {
		return set;
	}

	public Messager(String group, boolean start) {
		this.group = group;
		if (start) {
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					startProcess();
				}
			});
			thread.start();
		}
	}
	
	public Messager(String group) {
		this(group, true);
	}
	
	public void startProcess() {
		if (!messageAutoLoop) {
			current = Lib.getMessage(group);
			if (current.getPercent().equalsIgnoreCase("auto")) {
				messageAutoLoop = true;
				messageAutoReduceBy = 100D / (current.getShow() / 20D);
				show = 20;
				interval = 0;
			} else {
				show = current.getShow();
				interval = current.getInterval();
			}
		}
		Message toShow = current.clone();
		if (messageAutoLoop) {
			int percent;
			if (Utils.isInteger(current.getPercent())) {
				messageAutoLastPercent -= messageAutoReduceBy;
				percent = (int) Math.round(messageAutoLastPercent);
			} else {
				percent = 100;
				messageAutoLastPercent = 100;
			}
			if (messageAutoLastPercent <= messageAutoReduceBy) {
				messageAutoLoop = false;
			}
			String spct = Integer.toString(percent);
			current.setPercent(spct);
			toShow.setPercent(spct);
			toShow.setMessage(current.getMessage().replaceAll("(?i)%sec%", Long.toString(Math.round(messageAutoLastPercent / messageAutoReduceBy))));
		}
		set = true;
		if (!isBroadcasting && !isBroadcastingEvent && !isScheduling) {
			Lib.setMsg(toShow, this);
		}
		showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			public void run() {
				if (!isBroadcasting && !isBroadcastingEvent && !isScheduling) {
					for (Player p : GroupManager.getPlayersInGroup(group)) {
						BarAPI.removeBar(p);
					}
				}
				set = false;
				delayTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
					public void run() {
						startProcess();
					}
				}, interval);
			}
		}, show);
	}
	
	public void setCurrentMessage() {
		if (isBroadcasting) {
			Lib.setMsg(broadcasting, this);
		} else if (isBroadcastingEvent) {
			Lib.setMsg(broadcastingEvent, this);
		} else if (isScheduling) {
			Lib.setMsg(scheduling, this);
		} else if (set) {
			Lib.setMsg(current, this);
		} else {
			for (Player p : GroupManager.getPlayersInGroup(group)) {
				BarAPI.removeBar(p);
			}
		}
	}
	
	public void schedule(Message message, final List<String> commands) {
		if (message.getPercent().equalsIgnoreCase("auto")) {
			scheduleAutoReducingMessage(message, commands);
		} else {
			if (isScheduling) {
				Main.scr.cancelTask(scheduleTaskId);
			}
			scheduling = message;
			isScheduling = true;
			if (!isBroadcasting && !isBroadcastingEvent) {
				Lib.setMsg(scheduling, this);
			}
			scheduleTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					isScheduling = false;
					if (commands != null) {
						setCurrentMessage();
						for (String cmd : commands) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
						}
					}
				}
			}, message.getShow());
		}
	}
	
	public void scheduleAutoReducingMessage(final Message message, final List<String> commands) {
		if (scheduleAutoTaskId != -1) {
			Main.scr.cancelTask(scheduleAutoTaskId);
		}
		final Message schedulingAuto = message.clone();
		final double scheduleAutoReduceBy = 100D / (schedulingAuto.getShow() / 20D);
		scheduleAutoTaskId = Main.scr.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Message toShow = schedulingAuto.clone();
				int percent;
				if (Utils.isInteger(schedulingAuto.getPercent())) {
					scheduleAutoLastPercent -= scheduleAutoReduceBy;
					percent = (int) Math.round(scheduleAutoLastPercent);
				} else {
					percent = 100;
					scheduleAutoLastPercent = 100;
				}
				String spct = Integer.toString(percent);
				schedulingAuto.setPercent(spct);
				toShow.setPercent(spct);
				toShow.setShow(20);
				toShow.setMessage(schedulingAuto.getMessage().replaceAll("(?i)%sec%", Long.toString(Math.round(scheduleAutoLastPercent / scheduleAutoReduceBy))));
				if (scheduleAutoLastPercent < scheduleAutoReduceBy * 1.5) {
					schedule(toShow, commands);
					int t = scheduleAutoTaskId;
					scheduleAutoTaskId = -1;
					Main.scr.cancelTask(t);
				} else {
					schedule(toShow, null);
				}
			}
		}, 0, 20);
	}
	
	public void broadcast(Message message) {
		if (message.getPercent().equalsIgnoreCase("auto")) {
			broadcastAutoReducingMessage(message);
		} else {
			if (isBroadcasting) {
				Main.scr.cancelTask(broadcastTaskId);
			}
			broadcasting = message;
			isBroadcasting = true;
			Lib.setMsg(broadcasting, this);
			broadcastTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					isBroadcasting = false;
					setCurrentMessage();
				}
			}, message.getShow());
		}
	}
	
	public void broadcastAutoReducingMessage(final Message message) {
		if (broadcastAutoTaskId != -1) {
			Main.scr.cancelTask(broadcastAutoTaskId);
		}
		final Message broadcastingAuto = message.clone();
		final double broadcastAutoReduceBy = 100D / (broadcastingAuto.getShow() / 20D);
		broadcastAutoTaskId = Main.scr.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Message toShow = broadcastingAuto.clone();
				int percent;
				if (Utils.isInteger(broadcastingAuto.getPercent())) {
					broadcastAutoLastPercent -= broadcastAutoReduceBy;
					percent = (int) Math.round(broadcastAutoLastPercent);
				} else {
					percent = 100;
					broadcastAutoLastPercent = 100;
				}
				String spct = Integer.toString(percent);
				broadcastingAuto.setPercent(spct);
				toShow.setPercent(spct);
				toShow.setShow(20);
				toShow.setMessage(broadcastingAuto.getMessage().replaceAll("(?i)%sec%", Long.toString(Math.round(broadcastAutoLastPercent / broadcastAutoReduceBy))));
				broadcast(toShow);
				if (broadcastAutoLastPercent < broadcastAutoReduceBy * 1.5) {
					int t = broadcastAutoTaskId;
					broadcastAutoTaskId = -1;
					Main.scr.cancelTask(t);
				}
			}
		}, 0, 20);
	}
	
	public void broadcastEvent(Message message) {
		if (message.getPercent().equalsIgnoreCase("auto")) {
			eventAutoReducingMessage(message);
		} else {
			if (isBroadcastingEvent) {
				Main.scr.cancelTask(eventTaskId);
			}
			broadcastingEvent = message;
			isBroadcastingEvent = true;
			if (!isBroadcasting) {
				Lib.setMsg(broadcastingEvent, this);
			}
			eventTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					isBroadcastingEvent = false;
					setCurrentMessage();
				}
			}, message.getShow());
		}
	}
	
	public void eventAutoReducingMessage(final Message message) {
		if (eventAutoTaskId != -1) {
			Main.scr.cancelTask(eventAutoTaskId);
		}
		final Message eventAuto = message.clone();
		final double eventAutoReduceBy = 100D / (eventAuto.getShow() / 20D);
		eventAutoTaskId = Main.scr.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Message toShow = eventAuto.clone();
				int percent;
				if (Utils.isInteger(eventAuto.getPercent())) {
					eventAutoLastPercent -= eventAutoReduceBy;
					percent = (int) Math.round(eventAutoLastPercent);
				} else {
					percent = 100;
					eventAutoLastPercent = 100;
				}
				String spct = Integer.toString(percent);
				eventAuto.setPercent(spct);
				toShow.setPercent(spct);
				toShow.setShow(20);
				toShow.setMessage(eventAuto.getMessage().replaceAll("(?i)%sec%", Long.toString(Math.round(eventAutoLastPercent / eventAutoReduceBy))));
				broadcastEvent(toShow);
				if (eventAutoLastPercent < eventAutoReduceBy * 1.5) {
					int t = eventAutoTaskId;
					eventAutoTaskId = -1;
					Main.scr.cancelTask(t);
				}
			}
		}, 0, 20);
	}
	
	public Message getCurrentMessage() {
		if (isBroadcasting) {
			return broadcasting;
		} else if (isBroadcastingEvent) {
			return broadcastingEvent;
		} else if (isScheduling) {
			return scheduling;
		} else if (set) {
			return current;
		}
		return null;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public boolean isActive() {
		return set | isScheduling | isBroadcastingEvent | isBroadcasting;
	}
	
	public void stop() {
		if (!set) {
			Main.scr.cancelTask(delayTaskId);
		}
		Main.scr.cancelTask(showingTaskId);
	}
}
