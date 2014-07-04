package net.pixelizedmc.bossmessage;

import java.util.List;

public class Task {
	
	private String message;
	private List<String> commands;
	
	public Task(String message, List<String> commands) {
		this.message = message;
		this.commands = commands;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	
	public void addCommand(String cmd) {
		commands.add(cmd);
	}
}
