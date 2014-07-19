package net.pixelizedmc.bossmessage.utils;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class PlayerMessager extends Messager {
	
	public static Map<Player, PlayerMessager> players = new HashMap<Player, PlayerMessager>();
	private Player player;
	
	public PlayerMessager(Player player) {
		super(":" + player.getName(), false);
		this.setPlayer(player);
	}
	
	@Override
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
			Lib.setPlayerMsg(player, Lib.preGenMsg(Lib.getPlayerMsg(player)));
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public static Messager getPlayerMessager(Player p) {
		if (players.containsKey(p)) {
			return players.get(p);
		} else {
			PlayerMessager msgr = new PlayerMessager(p);
			players.put(p, msgr);
			return msgr;
		}
	}
}
