package net.pixelizedmc.bossmessage;

import net.pixelizedmc.bossmessage.utils.Message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CM {
	
    final static String path = "plugins/BossMessage/config.yml";
    static File file = new File(path);
    public static FileConfiguration config = Main.getInstance().getConfig();
    
    public static boolean enabled;
    public static boolean random;
    public static boolean repeatrdmcolors;
    public static boolean repeatrdmplayers;
    public static String colorcodes;
    public static List<Message> messages;
    public static List<Message> rawmessages;
    public static boolean useVNP;
    public static String noperm;
    public static boolean whitelist;
    public static List<String> worlds;
    public static List<String> ignoreplayers;
    public static String mode;
    public static boolean checkUpdates;
    
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
		Main.numberOfMessages = config.getConfigurationSection("BossMessage.Messages").getKeys(false).size();
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
	
    public static void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("[BossMessage] Error 'createConfig' on " + path);
        }
    }
    
	public static Message colorMsg(Message m) {
		return new Message(ChatColor.translateAlternateColorCodes('&', m.msg), m.percent, m.show, m.interval, m.calcpct);
    }
	
	public static void reloadConfig() {
	    config = YamlConfiguration.loadConfiguration(file);
	    readConfig();
	}
	
	public static List<Message> readMessages() {
		List<Message> output = new ArrayList<Message>();
		for (int msgid = 0;msgid < Main.numberOfMessages;msgid++) {
			String message = config.getString("BossMessage.Messages." + msgid + ".Message");
			String percent = config.getString("BossMessage.Messages." + msgid + ".Percentage");
			int show = config.getInt("BossMessage.Messages." + msgid + ".Show");
			int interval = config.getInt("BossMessage.Messages." + msgid + ".Interval");
			boolean calcpct = config.getBoolean("BossMessage.Messages." + msgid + ".CalculatePercentage");
			output.add(new Message(message, percent, show, interval, calcpct));
		}
		return output;
	}
	
	public static List<Message> colorMsgs(List<Message> msgs) {
		List<Message> output = new ArrayList<Message>();
		for (Message msg:msgs) {
			output.add(colorMsg(msg));
		}
		return output;
	}
	
	public static void writeMessages(List<Message> msgs) {
		config.set("BossMessage.Messages", null);
		for (int msgid = 0;msgid < msgs.size();msgid++) {
			config.set("BossMessage.Messages." + msgid + ".Message", msgs.get(msgid).msg);
			config.set("BossMessage.Messages." + msgid + ".Percentage", msgs.get(msgid).percent);
			config.set("BossMessage.Messages." + msgid + ".Show", msgs.get(msgid).show);
			config.set("BossMessage.Messages." + msgid + ".Interval", msgs.get(msgid).interval);
			config.set("BossMessage.Messages." + msgid + ".CalculatePercentage", msgs.get(msgid).calcpct);
		}
	}
}
