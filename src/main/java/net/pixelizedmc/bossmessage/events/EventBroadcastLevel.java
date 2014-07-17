package net.pixelizedmc.bossmessage.events;

public enum EventBroadcastLevel {
	EVERYONE,
	TARGET;
	
	public static EventBroadcastLevel getLevelFromString(String input) {
		String level = input.toLowerCase();
		switch (level) {
			case "everyone":
				return EVERYONE;
			case "target":
				return TARGET;
			default:
				return null;
		}
	}
}
