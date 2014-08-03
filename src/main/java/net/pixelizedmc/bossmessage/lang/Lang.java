package net.pixelizedmc.bossmessage.lang;

import java.util.ArrayList;
import java.util.List;

import net.pixelizedmc.bossmessage.commands.HelpCommand;

public class Lang {
	public static List<HelpCommand> commands = new ArrayList<HelpCommand>();
	public static HelpCommand COMMAND_ADD = new HelpCommand(
			"/bm add <group> <percent> <show> <interval> <message>",
			"Adds a new message to the specified group. (Show\nand Interval values must be specified in ticks,\n20 ticks = 1 second) The percent vault may contain\nequations and scripts!",
			"/bm add default health/max_health*100 260 100 &1This is a &lTEST!",
			"add");
	public static HelpCommand COMMAND_REMOVE = new HelpCommand(
			"/bm remove <group> <message #>",
			"Removes a message from the specified group. To view\nthe list of message #'s, see /bm list.",
			"/bm remove default 1",
			"remove");
	public static HelpCommand COMMAND_ADDGROUP = new HelpCommand(
			"/bm addgroup <group>",
			"Creates a new message group with the specified name.\nBy default, a new group has no messages, you would\nneed to add them manually (see /bm add.)",
			"/bm addgroup test",
			"addgroup");
	public static HelpCommand COMMAND_DELGROUP = new HelpCommand(
			"/bm delgroup <group>",
			"Permanently removes a group with the specified name,\nand all it's messages. You should always keep atleast\none group, even if it has no messages. Everytime\nBossMessage config loads, it will reset it's groups\n(and messages) to defaults, if finds no groups were\nfound.",
			"/bm delgroup test",
			"delgroup");
	public static HelpCommand COMMAND_LIST = new HelpCommand(
			"/bm list [group]",
			"Lists the messages and their #'s for the specified\ngroup in their order. If no group is specified, then\nlists all existing groups.",
			"/bm list default",
			"list");
	public static HelpCommand COMMAND_WHITELIST = new HelpCommand(
			"/bm whitelist <true/false>",
			"Toggles the world whitelist. If whitelist is on,\nmessages will only be shown in specified worlds\n(see /bm addworld), otherwise messages will be shown\nin all worlds.",
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
			"Toggles your message view. By default, everyone has\naccess to this command. Generally, implemented to\nallow players to disable flashing BossBar messages\nif they find it annoying.",
			"/bm toggle",
			"toggle");
	public static HelpCommand COMMAND_SETCOLORS = new HelpCommand(
			"/bm setcolors <colors>",
			"Sets the possible colors for %rdm_color%, the arguments\nfor thes command are the colorcodes (from 0 to f,\nincluding formating codes) types all in one string.\nCommand shown below as an example, means that every\n%rdm_color% will be replaced with either &1, &4, &a\nor &b, with equal probability\n(1/[amount of colorcodes entered]). HINT: If you\nwant specific colorcode to appear more often than\nothers, you can enter it more than once! Ex:\n'/bm setcolors aaabbc' will make color &a appear\nwith probability 3/6, &b - 2/6 and &c - 1/6.",
			"/bm setcolors 14be",
			"setcolors");
	public static HelpCommand COMMAND_NOPERM = new HelpCommand(
			"/bm noperm <msg>",
			"I honestly dunno why in the world I added this command,\nbut it simply changes the message that will appear\nin chat for a player, that tries to use a bossmessage\ncommand that he has no access to.",
			"/bm noperm &cNo permission!",
			"noperm");
	public static HelpCommand COMMAND_RRC = new HelpCommand(
			"/bm rrc <true/false>",
			"Toggles the random color repeating when using multiple\n%rdm_color% tags in the same message.",
			"/bm rrc false",
			"rrc");
	public static HelpCommand COMMAND_RRP = new HelpCommand(
			"/bm rrp <true/false>",
			"Toggles the random player repeating when using multiple\n%rdm_player% tags in the same message.",
			"/bm rrp false",
			"rrp");
	public static HelpCommand COMMAND_RANDOM = new HelpCommand(
			"/bm random <true/false>",
			"Toggles the random message picking. If true, messages\nwill auto-broadcasted randomly, otherwise they are\nbroadcasted in order.",
			"/bm random true",
			"random");
	public static HelpCommand COMMAND_RELOAD = new HelpCommand(
			"/bm reload",
			"Reloads the config.",
			"/bm reload",
			"reload");
	public static HelpCommand COMMAND_UPDATE = new HelpCommand(
			"/bm update",
			"Downloads and installs the latest version of BossMessage,\nif the update is available. Updates are being checked\nautomatically every time BossMessage loads, unless\ndisabled in the config. To check for updates manually\nsee /bm check.",
			"/bm update",
			"update.perform");
	public static HelpCommand COMMAND_CHECK = new HelpCommand(
			"/bm check",
			"Checks for updates. If an update is available, it\nwill notify you the newest version with the direct\ndownload link to it, and allow you to do /bm update\nto auto-download and install the update.",
			"/bm check",
			"update.check");
	public static HelpCommand COMMAND_BROADCAST = new HelpCommand(
			"/bm broadcast <sec> <message>",
			"Broadcasts the message for the specified time (in\nseconds), the number of seconds must be a Java integer\n(can't be more than 2147483647).",
			"/bm broadcast 20 &6This is a broadcast message!",
			"broadcast");
	public static HelpCommand COMMAND_QB = new HelpCommand(
			"/bm qb <message>",
			"Broadcasts a message for the default time (30 seconds,\nchangeable on config.yml)",
			"/bm qb &6This is a broadcast message!",
			"qb");
	public static HelpCommand COMMAND_GB = new HelpCommand(
			"/bm gb <group> <sec> <message>",
			"Same as regular broadcast (see /bm broadcast), except\nthat it only broadcasts the message to players in\nthe specific message group.",
			"/bm gb default 20 &6This is a broadcast message!",
			"gb");
	public static HelpCommand COMMAND_SETREGION = new HelpCommand(
			"/bm setregion <world> <region> <group>",
			"Assigns a message group to the specified WorldGuard\nregion. This feature requires WorldGuard and\nWorldGuardRegionEvents to be installed! When a player\nis inside a region with a message group assigned, the\nregion's messages will override his group.",
			"/bm setregion spawn default",
			"setregion");
	public static HelpCommand COMMAND_UNSETREGION = new HelpCommand(
			"/bm unsetregion <world> <region>",
			"Clears the message group from the specified region (see /bm setregion).",
			"/bm unsetregion world spawn",
			"unsetregion");
	public static HelpCommand COMMAND_SCHEDULE = new HelpCommand(
			"/bm schedule <task> <sec>",
			"Schedules the task for the specified time.",
			"/bm schedule makeitrain 20",
			"task.schedule");
	public static HelpCommand COMMAND_QS = new HelpCommand(
			"/bm qs <task>",
			"Schedules the task for the default time (60 seconds,\nchangeable on config.yml)",
			"/bm unsetregion world spawn",
			"task.quick");
	public static HelpCommand COMMAND_ADDTASK = new HelpCommand(
			"/bm addtask <task> <message>",
			"Creates a new task with a message for scheduling\n(see /bm schedule)",
			"/bm addtask makeitrain &bRa1n 1t w1ll b3 in: &e%sec%",
			"task.add");
	public static HelpCommand COMMAND_DELTASK = new HelpCommand(
			"/bm deltask <task>",
			"Removes the specified task",
			"/bm deltask makeitrain",
			"task.remove");
	public static HelpCommand COMMAND_ADDTASKCMD = new HelpCommand(
			"/bm addtaskcmd <task> <cmd>",
			"Adds the command to the specified task. Task commands\nare dispatched from the console when the task is\nexecuted (see /bm schedule)",
			"/bm addtaskcmd weather thunder",
			"task.addcmd");
	public static HelpCommand COMMAND_TASKLIST = new HelpCommand(
			"/bm tasklist",
			"Lists all the tasks you have.",
			"/bm tasklist",
			"task.list");
	public static HelpCommand COMMAND_TASKINFO = new HelpCommand(
			"/bm taskinfo <task>",
			"Displays the info for the specified task.",
			"/bm taskinfo makeitrain",
			"task.info");
	public static HelpCommand COMMAND_CONTACT = new HelpCommand(
			"/bm contact <question>",
			"Contacts us! Only contact us if you found a bug, or\nneed help that you couldn't find anywhere else! Specify\nthe question/problem that you have.",
			"/bm contact Need help with per-region messages",
			"live.contact");
	public static HelpCommand COMMAND_SEND = new HelpCommand(
			"/bm send <message>",
			"Sends us the message while chatting with us.",
			"/bm send Hey, how are you doing? :)",
			"live.send");
	public static HelpCommand COMMAND_INFO = new HelpCommand(
			"/bm info",
			"Displays information about the plugin, everyone has\naccess to this command by default.",
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
		commands.add(COMMAND_UNSETREGION);
		commands.add(COMMAND_SCHEDULE);
		commands.add(COMMAND_QS);
		commands.add(COMMAND_ADDTASK);
		commands.add(COMMAND_DELTASK);
		commands.add(COMMAND_ADDTASKCMD);
		commands.add(COMMAND_TASKLIST);
		commands.add(COMMAND_TASKINFO);
		commands.add(COMMAND_INFO);
		commands.add(COMMAND_HELP);
	}
}
