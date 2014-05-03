package net.pixelizedmc.bossmessage.lang;

import java.util.ArrayList;
import java.util.List;

import net.pixelizedmc.bossmessage.commands.HelpCommand;

public class Lang {
	public static List<HelpCommand> commands = new ArrayList<HelpCommand>();
	public static HelpCommand COMMAND_ADD = new HelpCommand(
			"/bm add <group> <percent> <show> <interval> <message>",
			"Adds a new message to the specified group. (Show\n§9and Interval values must be specified in ticks,\n§920 ticks = 1 second) The percent vault may contain\n§9equations and scripts!",
			"/bm add default health/max_health*100 260 100 &1This is a &lTEST!",
			"add");
	public static HelpCommand COMMAND_REMOVE = new HelpCommand(
			"/bm remove <group> <message #>",
			"Removes a message from the specified group. To view\n§9the list of message #'s, see /bm list.",
			"/bm remove default 1",
			"remove");
	public static HelpCommand COMMAND_ADDGROUP = new HelpCommand(
			"/bm addgroup <group>",
			"Creates a new message group with the specified name.\n§9By default, a new group has no messages, you would\n§9need to add them manually (see /bm add.)",
			"/bm addgroup test",
			"addgroup");
	public static HelpCommand COMMAND_DELGROUP = new HelpCommand(
			"/bm delgroup <group>",
			"Permanently removes a group with the specified name,\n§9and all it's messages. You should always keep atleast\n§9one group, even if it has no messages. Everytime\n§9BossMessage config loads, it will reset it's groups\n§9(and messages) to defaults, if finds no groups were\n§9found.",
			"/bm delgroup test",
			"delgroup");
	public static HelpCommand COMMAND_LIST = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified\n§9group in their order. If no group is specified, then\n§9lists all existing groups.",
			"/bm list default",
			"list");
	public static HelpCommand COMMAND_WHITELIST = new HelpCommand(
			"/bm whitelist <true/false>",
			"Toggles the world whitelist. If whitelist is on,\n§9messages will only be shown in specified worlds\n§9(see /bm addworld), otherwise messages will be shown\n§9in all worlds.",
			"/bm whitelist true",
			"whitelist");
	public static HelpCommand COMMAND_ADDWORLD = new HelpCommand(
			"/bm addworld <world>",
			"Adds the specified world to the world whitelist (see /bm whitelist.)",
			"/bm addworld world_nether",
			"addworld");
	public static HelpCommand COMMAND_DELWORLD = new HelpCommand(
			"/bm delworld <world>",
			"Removes the specified world from the world whitelist (see /bm whitelist.)",
			"/bm delworld world_nether",
			"delworld");
	public static HelpCommand COMMAND_TOGGLE = new HelpCommand(
			"/bm toggle",
			"Toggles your message view. By default, everyone has\n§9access to this command. Generally, implemented to\n§9allow players to disable flashing BossBar messages\n§9if they find it annoying.",
			"/bm toggle",
			"toggle");
	public static HelpCommand COMMAND_SETCOLORS = new HelpCommand(
			"/bm setcolors <colors>",
			"Sets the possible colors for %rdm_color%, the arguments\n§9for thes command are the colorcodes (from 0 to f,\n§9including formating codes) types all in one string.\n§9Command shown below as an example, means that every\n§9%rdm_color% will be replaced with either &1, &4, &a\n§9or &b, with equal probability\n§9(1/[amount of colorcodes entered]). HINT: If you\n§9want specific colorcode to appear more often than\n§9others, you can enter it more than once! Ex:\n§9'/bm setcolors aaabbc' will make color &a appear\n§9with probability 3/6, &b - 2/6 and &c - 1/6.",
			"/bm setcolors 14be",
			"setcolors");
	public static HelpCommand COMMAND_NOPERM = new HelpCommand(
			"/bm noperm <msg>",
			"I honestly dunno why in the world I added this command,\n§9but it simply changes the message that will appear\n§9in chat for a player, that tries to use a bossmessage\n§9command that he has no access to.",
			"/bm noperm &cNo permission!",
			"noperm");
	public static HelpCommand COMMAND_RRC = new HelpCommand(
			"/bm rrc <true/false>",
			"Toggles the random color repeating when using multiple\n§9%rdm_color% tags in the same message.",
			"/bm rrc false",
			"rrc");
	public static HelpCommand COMMAND_RRP = new HelpCommand(
			"/bm rrp <true/false>",
			"Toggles the random player repeating when using multiple\n§9%rdm_player% tags in the same message.",
			"/bm rrp false",
			"rrp");
	public static HelpCommand COMMAND_RANDOM = new HelpCommand(
			"/bm random <true/false>",
			"Toggles the random message picking. If true, messages\n§9will auto-broadcasted randomly, otherwise they are\n§9broadcasted in order.",
			"/bm random true",
			"random");
	public static HelpCommand COMMAND_RELOAD = new HelpCommand(
			"/bm reload",
			"Reloads the config.",
			"/bm reload",
			"reload");
	public static HelpCommand COMMAND_UPDATE = new HelpCommand(
			"/bm update",
			"Downloads and installs the latest version of BossMessage,\n§9if the update is available. Updates are being checked\n§9automatically every time BossMessage loads, unless\n§9disabled in the config. To check for updates manually\n§9see /bm check.",
			"/bm update",
			"update.perform");
	public static HelpCommand COMMAND_CHECK = new HelpCommand(
			"/bm check",
			"Checks for updates. If an update is available, it\n§9will notify you the newest version with the direct\n§9download link to it, and allow you to do /bm update\n§9to auto-download and install the update.",
			"/bm check",
			"update.check");
	public static HelpCommand COMMAND_BROADCAST = new HelpCommand(
			"/bm broadcast <sec> <message>",
			"Broadcasts the message for the specified time (in\n§9seconds), the number of seconds must be a Java integer\n§9(can't be more than 2147483647).",
			"/bm broadcast 20 &6This is a broadcast message!",
			"broadcast");
	public static HelpCommand COMMAND_QB = new HelpCommand(
			"/bm qb <message>",
			"Broadcasts a message for the default time (30 seconds,\n§9changeable on config.yml)",
			"/bm qb &6This is a broadcast message!",
			"qb");
	public static HelpCommand COMMAND_GB = new HelpCommand(
			"/bm gb <group> <sec> <message>",
			"Same as regular broadcast (see /bm broadcast), except\n§9that it only broadcasts the message to players in\n§9the specific message group.",
			"/bm gb default 20 &6This is a broadcast message!",
			"gb");
	public static HelpCommand COMMAND_SETREGION = new HelpCommand(
			"/bm setregion <region> <message>",
			"Same as regular broadcast (see /bm broadcast), except\n§9that it only broadcasts the message to players in\n§9the specific message group.",
			"/bm setregion spawn default",
			"gb");
	public static HelpCommand COMMAND_INFO = new HelpCommand(
			"/bm info",
			"Displays information about the plugin, everyone has\n§9access to this command by default.",
			"/bm info",
			"info");
	public static HelpCommand COMMAND_HELP = new HelpCommand(
			"/bm help",
			"Displays this help message.",
			"/bm help",
			"user");
	public static void loadLang() {
		commands.add(COMMAND_ADD);
		commands.add(COMMAND_REMOVE);
		commands.add(COMMAND_ADDGROUP);
		commands.add(COMMAND_DELGROUP);
		commands.add(COMMAND_LIST);
		commands.add(COMMAND_WHITELIST);
		commands.add(COMMAND_ADDWORLD);
		commands.add(COMMAND_DELWORLD);
		commands.add(COMMAND_TOGGLE);
		commands.add(COMMAND_SETCOLORS);
		commands.add(COMMAND_NOPERM);
		commands.add(COMMAND_RRC);
		commands.add(COMMAND_RRP);
		commands.add(COMMAND_RANDOM);
		commands.add(COMMAND_RELOAD);
		commands.add(COMMAND_UPDATE);
		commands.add(COMMAND_CHECK);
		commands.add(COMMAND_BROADCAST);
		commands.add(COMMAND_QB);
		commands.add(COMMAND_GB);
		commands.add(COMMAND_SETREGION);
		commands.add(COMMAND_INFO);
		commands.add(COMMAND_HELP);
	}
}
