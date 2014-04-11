package net.pixelizedmc.bossmessage.configuration;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.configuration.Message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
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
    public static String mode;
    public static boolean checkUpdates;
    public static List<String> groups;
    
    public static void createConfig() {
    	config.options().copyDefaults(true);
    	config.addDefault("BossMessage.Messages", new ArrayList<>());
        if (!config.contains("BossMessage.Messages.0")) {
        	config.set("BossMessage.Messages.0.Message", "&bYo &5%player%&b, wazzup?");
        	config.set("BossMessage.Messages.0.Percentage", "100");
        	config.set("BossMessage.Messages.0.Show", 100);
        	config.set("BossMessage.Messages.0.Interval", 0);
        	config.set("BossMessage.Messages.0.CalculatePercentage", false);
        	config.set("BossMessage.Messages.1.Message", "&aBossMessage - best BossBar plugin by &bplay.pixelizedmc.net");
        	config.set("BossMessage.Messages.1.Percentage", "60");
        	config.set("BossMessage.Messages.1.Show", 100);
        	config.set("BossMessage.Messages.1.Interval", 0);
        	config.set("BossMessage.Messages.1.CalculatePercentage", false);
        	config.set("BossMessage.Messages.2.Message", "%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors");
        	config.set("BossMessage.Messages.2.Percentage", "30");
        	config.set("BossMessage.Messages.2.Show", 100);
        	config.set("BossMessage.Messages.2.Interval", 0);
        	config.set("BossMessage.Messages.2.CalculatePercentage", false);
        	config.set("BossMessage.Messages.3.Message", "&aRight now, there are &b%online_players%&c/&b%max_players% &aPlayers online");
        	config.set("BossMessage.Messages.3.Percentage", "online_players/max_players*100");
        	config.set("BossMessage.Messages.3.Show", 100);
        	config.set("BossMessage.Messages.3.Interval", 0);
        	config.set("BossMessage.Messages.3.CalculatePercentage", true);
        	config.set("BossMessage.Messages.4.Message", "&6Th1s 1s a m3ssag3 w1th &brand0m&6 p3rs3ntag3");
        	config.set("BossMessage.Messages.4.Percentage", "Math.floor(Math.random()*100)");
        	config.set("BossMessage.Messages.4.Show", 100);
        	config.set("BossMessage.Messages.4.Interval", 0);
        	config.set("BossMessage.Messages.4.CalculatePercentage", true);
        }
        save();
    }
    
	public static void readConfig() {
		groups = new ArrayList<String>(config.getConfigurationSection("BossMessage.Messages").getKeys(false));
		mode = config.getString("BossMessage.Mode");
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
    }
	
    @SuppressWarnings("unchecked")
	public static Map<String, List<Message>> readMessages() {
    	List<String> groups = new ArrayList<>(config.getConfigurationSection("BossMessage.Messages").getKeys(false));
    	Map<String, List<Message>> output = new HashMap<String, List<Message>>();
    	for (String group:groups) {
    		List<Message> msgs = (List<Message>) config.getList("BossMessage.Messages." + group);
    		output.put(group, msgs);
    	}
		return output;
	}

	public static void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("[BossMessage] Error 'createConfig' on " + path);
        }
    }
    
	public static Message colorMsg(Message m) {
		return new Message(ChatColor.translateAlternateColorCodes('&', m.Message), m.Percent, m.Show, m.Interval);
    }
	
	public static void reloadConfig() {
	    config = YamlConfiguration.loadConfiguration(file);
	    readConfig();
	}
	
	public static Map<String, List<Message>> colorMsgs(Map<String, List<Message>> rawmessages2) {
		Map<String, List<Message>> output = new HashMap<String, List<Message>>();
		List<String> groups = new ArrayList<>(rawmessages2.keySet());
		for (String group:groups) {
			List<Message> output2 = new ArrayList<Message>();
			for (Message msg:rawmessages2.get(group)) {
				output2.add(colorMsg(msg));
			}
			output.put(group, output2);
		}
		return output;
	}
}
