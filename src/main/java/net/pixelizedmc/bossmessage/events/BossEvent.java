package net.pixelizedmc.bossmessage.events;

import net.pixelizedmc.bossmessage.utils.Message;

public class BossEvent {
	private boolean enabled;
	private Message message;
	private EventBroadcastLevel level;
	
	public BossEvent(boolean enabled, Message message, EventBroadcastLevel level) {
		this.enabled = enabled;
		this.message = message;
		this.level = level;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public EventBroadcastLevel getLevel() {
		return level;
	}
	
	public void setLevel(EventBroadcastLevel level) {
		this.level = level;
	}
}
