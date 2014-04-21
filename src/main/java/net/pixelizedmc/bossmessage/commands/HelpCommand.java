package net.pixelizedmc.bossmessage.commands;

public class HelpCommand {
	public String command;
	public String description;
	public String example;
	
	public HelpCommand(String command, String description, String example) {
		this.command = command;
		this.description = description;
		this.example = example;
	}
}
