package net.pixelizedmc.bossmessage.lang;

import net.pixelizedmc.bossmessage.commands.HelpCommand;

public class Lang {
	public static HelpCommand COMMAND_ADD = new HelpCommand(
			"/bm add <group> <percent> <show> <interval> <message>",
			"Adds a new message to the specified group. (Show and\n§9Interval values must be specified in ticks,\n§920 ticks = 1 second) The percent vault may contain\n§9equations and scripts!",
			"/bm add default health/max_health*100 260 100 &1This is a &lTEST!");
	public static HelpCommand COMMAND_REMOVE = new HelpCommand(
			"/bm remove <group> <message #>",
			"Removes a message from the specified group. To view the list of message #'s, see /bm list.",
			"/bm remove default 1");
	public static HelpCommand COMMAND_ADDGROUP = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_DELGROUP = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_LIST = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_WHITELIST = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_ADDWORLD = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_DELWORLD = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_TOGGLE = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified group in their order. If no group is specified, then lists all existing groups.",
			"/bm list default");
}
