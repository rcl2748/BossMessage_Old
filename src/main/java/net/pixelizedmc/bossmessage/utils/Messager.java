package net.pixelizedmc.bossmessage.utils;

import java.util.List;

import me.confuser.barapi.BarAPI;
import net.pixelizedmc.bossmessage.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messager {
	
	private Message current;
	private int show;
	private int interval;
	private boolean set;
	private int showingTaskId;
	private int delayTaskId;
	private String group;
	private boolean isClosed = false;
	private boolean isBroadcasting = false;
	private boolean isScheduling = false;
	private Message broadcasting;
	private Message scheduling;
	private int broadcastTaskId = -1;
	private int scheduleTaskId = -1;
	private Thread thread;
	private boolean messageAutoLoop = false;
	private double messageAutoReduceBy;
	private double messageAutoLastPercent;
	private double scheduleAutoLastPercent;
	private double broadcastAutoLastPercent;
	private int scheduleAutoTaskId = -1;
	private int broadcastAutoTaskId = -1;
	
	public Message getCurrent() {
		return current;
	}
	
	public boolean isSet() {
		return set;
	}
	
	public Messager(String group) {
		this.group = group;
		if (group != null) {
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					startProcess();
				}
			});
			thread.start();
		}
	}
	
	public void startProcess() {
		if (!isClosed) {
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
			if (!isBroadcasting && !isScheduling) {
				Lib.setMsg(Lib.preGenMsg(toShow), group);
			}
			showingTaskId = Main.scr.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					if (!isBroadcasting && !isScheduling) {
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
	
	public void schedule(Message message, final List<String> commands) {
		if (message.getPercent().equalsIgnoreCase("auto")) {
			scheduleAutoReducingMessage(message, commands);
		} else {
			if (isScheduling) {
				Main.scr.cancelTask(scheduleTaskId);
			}
			scheduling = message;
			isScheduling = true;
			if (!isBroadcasting) {
				Lib.setMsg(Lib.preGenMsg(scheduling), group);
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
			Lib.setMsg(Lib.preGenMsg(broadcasting), group);
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
	
	public Message getCurrentMessage() {
		if (isBroadcasting) {
			return broadcasting;
		} else if (isScheduling) {
			return scheduling;
		} else if (set) {
			return current;
		}
		return null;
	}
	
	public void stop() {
		this.isClosed = true;
		if (!set) {
			Main.scr.cancelTask(delayTaskId);
		}
		Main.scr.cancelTask(showingTaskId);
	}
}
