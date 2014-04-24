package net.pixelizedmc.bossmessage.commands;

public class HelpCommand {
	public String command;
	public String description;
	public String example;
	public String perm;
	
	public HelpCommand(String command, String description, String example, String perm) {
		this.command = command;
		this.description = description;
		this.example = example;
		this.perm = perm;
	}
}
