package net.pixelizedmc.bossmessage.configuration;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.Task;
import net.pixelizedmc.bossmessage.events.BossEvent;
import net.pixelizedmc.bossmessage.events.EventBroadcastLevel;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.Messager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CM {
	
	final static String path = "plugins/BossMessage/config.yml";
	static File file = new File(path);
	public static FileConfiguration config = Main.getInstance().getConfig();
	
	public static boolean random;
	public static boolean repeatrdmcolors;
	public static boolean repeatrdmplayers;
	public static String colorcodes;
	public static Map<String, List<Message>> messages = new HashMap<String, List<Message>>();
	public static Map<String, List<Message>> rawmessages = new HashMap<String, List<Message>>();
	public static boolean useVNP;
	public static String noperm;
	public static boolean whitelist;
	public static List<String> worlds;
	public static List<String> ignoreplayers;
	public static boolean checkUpdates;
	public static List<String> groups;
	public static int scheduleDefaultTime;
	public static String schedulePercent;
	public static int broadcastDefaultTime;
	public static String broadcastPercent;
	public static Map<String, Map<String, Messager>> regions;
	public static Map<String, Task> tasks = new HashMap<String, Task>();
	public static BossEvent onPVPDeath;
	public static BossEvent onPlayerJoin;
	public static BossEvent onPlayerQuit;
	
	public static void createConfig() {
		config.options().copyDefaults(true);
		
		boolean outdatedConfig = false;
		if (config.getInt("BossMessage.ConfigVersion") != 0) {
			for (String key : config.getKeys(false)) {
				config.set(key, null);
			}
			save();
			config.options().copyDefaults(true);
			outdatedConfig = true;
		}
		if (config.getConfigurationSection("BossMessage.Messages") == null || config.getConfigurationSection("BossMessage.Messages").getKeys(false).size() == 0 || outdatedConfig) {
			List<Message> defaultMessages = new ArrayList<Message>();
			defaultMessages.add(new Message("&bYo &5%player%&b, wazzup?", "100", 100, 0));
			defaultMessages.add(new Message("&aBossMessage - best BossBar plugin by &bVictor2748", "50", 100, 0));
			defaultMessages.add(new Message("%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors", "30", 100, 0));
			defaultMessages.add(new Message("&aRight now, there are &b%online_players%&c/&b%max_players% &aPlayers online", "online_players/max_players*100", 100, 0));
			defaultMessages.add(new Message("&6Th1s 1s a m3ssag3 w1th &brand0m&6 p3rs3ntag3", "Math.floor(Math.random()*100)", 100, 0));
			defaultMessages.add(new Message("&eThis is a message with an auto-reducing percentage", "auto", 100, 0));
			defaultMessages.add(new Message("&bYour balance: &c%econ_dollars%.%econ_cents%", "100", 100, 0));
			defaultMessages.add(new Message("&bYour health is shown on the bossbar!", "health/max_health*100", 100, 0));
			defaultMessages.add(new Message("&dDon't forget to check out the new cool BossMessage Animator!", "100", 100, 0));
			config.set("BossMessage.Messages.default", defaultMessages);
		}
		if (config.getConfigurationSection("BossMessage.Tasks") == null || config.getConfigurationSection("BossMessage.Tasks").getKeys(false).size() == 0 || outdatedConfig) {
			ConfigurationSection tasks = config.getConfigurationSection("BossMessage.Tasks");
			List<String> cmds1 = new ArrayList<String>();
			cmds1.add("say Oops, sorry! We are rong again. No rain will be!");
			List<String> cmds2 = new ArrayList<String>();
			cmds2.add("stop");
			List<String> cmds3 = new ArrayList<String>();
			cmds3.add("say Now, Skynet will initiate a massive nuclear attack in your a$$");
			tasks.addDefault("makeitrain.Message", "&4[&6Weather&aForecast&4] &bRa1n 1t w1ll b3 in &e%sec%");
			tasks.addDefault("makeitrain.Commands", cmds1);
			tasks.addDefault("stoptheserver.Message", "&4Server is going down in &e%sec%");
			tasks.addDefault("stoptheserver.Commands", cmds2);
			tasks.addDefault("starttheskynet.Message", "&aSkynet activated! Its gonna f*** you up in %sec% seconds.");
			tasks.addDefault("starttheskynet.Commands", cmds3);
			
		}
		save();
	}
	
	public static void readConfig() {
		groups = new ArrayList<String>(config.getConfigurationSection("BossMessage.Messages").getKeys(false));
		random = config.getBoolean("BossMessage.Random");
		repeatrdmcolors = config.getBoolean("BossMessage.RepeatRandomColors");
		repeatrdmplayers = config.getBoolean("BossMessage.RepeatRandomPlayers");
		noperm = ChatColor.translateAlternateColorCodes('&', config.getString("BossMessage.NoPermission"));
		colorcodes = config.getString("BossMessage.ColorCodes");
		rawmessages = readMessages();
		messages = colorMsgs(rawmessages);
		whitelist = config.getBoolean("BossMessage.Whitelist");
		worlds = config.getStringList("BossMessage.WhitelistedWorlds");
		ignoreplayers = config.getStringList("BossMessage.IgnoredPlayers");
		useVNP = config.getBoolean("BossMessage.VanishNoPacketSupport");
		checkUpdates = config.getBoolean("BossMessage.CheckUpdates");
		scheduleDefaultTime = config.getInt("BossMessage.Schedule.DefaultTime");
		schedulePercent = config.getString("BossMessage.Schedule.Percent");
		broadcastDefaultTime = config.getInt("BossMessage.Broadcast.DefaultTime");
		broadcastPercent = config.getString("BossMessage.Broadcast.Percent");
		tasks = getTasks();
		
		boolean enabled;
		Message msg;
		EventBroadcastLevel level;
		ConfigurationSection events = config.getConfigurationSection("BossMessage.Events");
		
		enabled = events.getBoolean("PVPDeath.Enabled");
		String message = events.getString("PVPDeath.Message");
		String percent = events.getString("PVPDeath.Percent");
		int show = events.getInt("PVPDeath.Show");
		msg = new Message(message, percent, show, 0);
		level = EventBroadcastLevel.getLevelFromString(events.getString("PVPDeath.Broadcast"));
		onPVPDeath = new BossEvent(enabled, msg, level);

		enabled = events.getBoolean("PlayerJoin.Enabled");
		message = events.getString("PlayerJoin.Message");
		percent = events.getString("PlayerJoin.Percent");
		show = events.getInt("PlayerJoin.Show");
		msg = new Message(message, percent, show, 0);
		level = EventBroadcastLevel.getLevelFromString(events.getString("PlayerJoin.Broadcast"));
		onPlayerJoin = new BossEvent(enabled, msg, level);
		
		enabled = events.getBoolean("PlayerQuit.Enabled");
		message = events.getString("PlayerQuit.Message");
		percent = events.getString("PlayerQuit.Percent");
		show = events.getInt("PlayerQuit.Show");
		msg = new Message(message, percent, show, 0);
		level = EventBroadcastLevel.getLevelFromString(events.getString("PlayerQuit.Broadcast"));
		onPlayerQuit = new BossEvent(enabled, msg, level);
		
		if (broadcastDefaultTime < 1) {
			Main.logger.severe(Main.PREFIX_CONSOLE + "QuickBroadcastShowTime must be more than 0!");
			broadcastDefaultTime = 30;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, List<Message>> readMessages() {
		Map<String, List<Message>> output = new HashMap<String, List<Message>>();
		for (String group : groups) {
			List<Message> msgs = (List<Message>) config.getList("BossMessage.Messages." + group);
			output.put(group, msgs);
		}
		return output;
	}
	
	public static Map<String, Task> getTasks() {
		Map<String, Task> output = new HashMap<String, Task>();
		ConfigurationSection sec = config.getConfigurationSection("BossMessage.Tasks");
		for (String key : sec.getKeys(false)) {
			String message = sec.getString(key + ".Message");
			List<String> cmds = sec.getStringList(key + ".Commands");
			output.put(key, new Task(message, cmds));
		}
		return output;
	}
	
	public static Map<String, Map<String, Messager>> readRegionGroups() {
		Map<String, Map<String, Messager>> output = new HashMap<String, Map<String, Messager>>();
		ConfigurationSection sec = config.getConfigurationSection("BossMessage.Regions");
		if (sec != null && sec.getKeys(false).size() > 0) {
			for (String world : sec.getKeys(false)) {
				Map<String, Messager> worldmap = new HashMap<String, Messager>();
				ConfigurationSection worldsec = sec.getConfigurationSection(world);
				for (String region : worldsec.getKeys(false)) {
					worldmap.put(region, Main.messagers.get(worldsec.getString(region)));
				}
				output.put(world, worldmap);
			}
		}
		return output;
	}
	
	public static void save() {
		Main.getInstance().saveConfig();
	}
	
	public static void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		readConfig();
	}
	
	public static Map<String, List<Message>> colorMsgs(Map<String, List<Message>> rawmessages2) {
		Map<String, List<Message>> output = new HashMap<String, List<Message>>();
		for (String group : groups) {
			List<Message> output2 = new ArrayList<Message>();
			for (Message msg : rawmessages2.get(group)) {
				output2.add(msg.color());
			}
			output.put(group, output2);
		}
		return output;
	}
}