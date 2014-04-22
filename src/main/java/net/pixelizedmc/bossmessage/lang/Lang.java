package net.pixelizedmc.bossmessage.lang;

import java.util.ArrayList;
import java.util.List;

import net.pixelizedmc.bossmessage.commands.HelpCommand;

public class Lang {
	public static List<HelpCommand> commands = new ArrayList<HelpCommand>();
	public static HelpCommand COMMAND_ADD = new HelpCommand(
			"/bm add <group> <percent> <show> <interval> <message>",
			"Adds a new message to the specified group. (Show\n§9and Interval values must be specified in ticks,\n§920 ticks = 1 second) The percent vault may contain\n§9equations and scripts!",
			"/bm add default health/max_health*100 260 100 &1This is a &lTEST!");
	public static HelpCommand COMMAND_REMOVE = new HelpCommand(
			"/bm remove <group> <message #>",
			"Removes a message from the specified group. To view\n§9the list of message #'s, see /bm list.",
			"/bm remove default 1");
	public static HelpCommand COMMAND_ADDGROUP = new HelpCommand(
			"/bm addgroup <group>",
			"Creates a new message group with the specified name.\n§9By default, a new group has no messages, you would\n§9need to add them manually (see /bm add.)",
			"/bm addgroup test");
	public static HelpCommand COMMAND_DELGROUP = new HelpCommand(
			"/bm delgroup <group>",
			"Permanently removes a group with the specified name,\n§9and all it's messages. You should always keep atleast\n§9one group, even if it has no messages. Everytime\n§9BossMessage config loads, it will reset it's groups\n§9(and messages) to defaults, if finds no groups were\n§9found.",
			"/bm delgroup test");
	public static HelpCommand COMMAND_LIST = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified\n§9group in their order. If no group is specified, then\n§9lists all existing groups.",
			"/bm list default");
	public static HelpCommand COMMAND_WHITELIST = new HelpCommand(
			"/bm whitelist <true/false>",
			"Toggles the world whitelist. If whitelist is on,\n§9messages will only be shown in specified worlds\n§9(see /bm addworld), otherwise messages will be shown\n§9in all worlds.",
			"/bm whitelist true");
	public static HelpCommand COMMAND_ADDWORLD = new HelpCommand(
			"/bm addworld <world>",
			"Adds the specified world to the world whitelist (see /bm whitelist.)",
			"/bm addworld world_nether");
	public static HelpCommand COMMAND_DELWORLD = new HelpCommand(
			"/bm delworld <world>",
			"Removes the specified world from the world whitelist (see /bm whitelist.)",
			"/bm delworld world_nether");
	public static HelpCommand COMMAND_TOGGLE = new HelpCommand(
			"/bm toggle",
			"Toggles your message view. By default, everyone has access to this command. Generally, implemented to allow players to disable flashing BossBar messages if they find it annoying.",
			"/bm toggle");
	public static HelpCommand COMMAND_RRC = new HelpCommand(
			"/bm rrc <true/false>",
			"Toggles the random color repeating when using multiple\n§9%rdm_color% tags in the same message.",
			"/bm rrc false");
	public static HelpCommand COMMAND_RRP = new HelpCommand(
			"/bm rrp <true/false>",
			"Toggles the random player repeating when using multiple\n§9%rdm_player% tags in the same message.",
			"/bm rrp false");
	public static HelpCommand COMMAND_RANDOM = new HelpCommand(
			"/bm random <true/false>",
			"Toggles the random message picking. If true, messages\n§9will auto-broadcasted randomly, otherwise they are\n§9broadcasted in order.",
			"/bm random true");
	public static HelpCommand COMMAND_RELOAD = new HelpCommand(
			"/bm reload",
			"Reloads the config.",
			"/bm reload");
	public static HelpCommand COMMAND_UPDATE = new HelpCommand(
			"/bm update",
			"Downloads and installs the latest version of BossMessage,\n§9if the update is available. Updates are being checked\n§9automatically every time BossMessage loads, unless\n§9disabled in the config. To check for updates manually\n§9see /bm check.",
			"/bm update");
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
		commands.add(COMMAND_RRC);
		commands.add(COMMAND_RRP);
		commands.add(COMMAND_RANDOM);
		commands.add(COMMAND_RELOAD);
		commands.add(COMMAND_UPDATE);
	}
}
