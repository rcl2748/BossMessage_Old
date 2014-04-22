package net.pixelizedmc.bossmessage.configuration;

import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.utils.Message;

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
    public static boolean checkUpdates;
    public static List<String> groups;
    public static int broadcastDefaultTime;
    public static String broadcastPercent;
    
    public static void createConfig() {
    	config.options().copyDefaults(true);
    	boolean outdatedConfig = false;
    	if (config.getString("BossMessage.ConfigVersion") != "0") {
    		for (String key:config.getKeys(false)) {
    			config.set(key, null);
    		}
    		save();
        	config.options().copyDefaults(true);
        	outdatedConfig = true;
    	}
        if (config.getConfigurationSection("BossMessage.Messages") == null||config.getConfigurationSection("BossMessage.Messages").getKeys(false).size() == 0||outdatedConfig) {
        	List<Message> defaultMessages = new ArrayList<Message>();
        	defaultMessages.add(new Message("&bYo &5%player%&b, wazzup?", "100", 100, 0));
        	defaultMessages.add(new Message("&aBossMessage - best BossBar plugin by &bplay.pixelizedmc.net", "30", 100, 0));
        	defaultMessages.add(new Message("%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors", "30", 100, 0));
        	defaultMessages.add(new Message("&aRight now, there are &b%online_players%&c/&b%max_players% &aPlayers online", "online_players/max_players*100", 100, 0));
        	defaultMessages.add(new Message("&6Th1s 1s a m3ssag3 w1th &brand0m&6 p3rs3ntag3", "Math.floor(Math.random()*100)", 100, 0));
        	config.set("BossMessage.Messages.default", defaultMessages);
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
        broadcastDefaultTime = config.getInt("BossMessage.Broadcast.DefaultTime");
        broadcastPercent = config.getString("BossMessage.Broadcast.Percent");
        
        if (broadcastDefaultTime < 1) {
        	Main.logger.severe(Main.PREFIX_CONSOLE + "QuickBroadcastShowTime must be more than 0!");
        	broadcastDefaultTime = 30;
        }
    }
	
    @SuppressWarnings("unchecked")
	public static Map<String, List<Message>> readMessages() {
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