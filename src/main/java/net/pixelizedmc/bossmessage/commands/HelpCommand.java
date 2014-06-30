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
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExample() {
		return example;
	}
	
	public void setExample(String example) {
		this.example = example;
	}
	
	public String getPerm() {
		return perm;
	}
	
	public void setPerm(String perm) {
		this.perm = perm;
	}
}
