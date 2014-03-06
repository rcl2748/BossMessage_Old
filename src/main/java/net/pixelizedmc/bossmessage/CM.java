package net.pixelizedmc.bossmessage;

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
    static FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    
    public static boolean enabled;
    public static boolean random;
    public static boolean repeatrdmcolors;
    public static boolean repeatrdmplayers;
    public static String colorcodes;
    public static List<List<String>> messages;
    public static List<List<String>> rawmessages;
    public static String noperm;
    public static boolean whitelist;
    public static List<String> worlds;
    public static List<String> ignoreplayers;
    
    public static void createConfig() {
        config.addDefault("BossMessage.Random", false);
        config.addDefault("BossMessage.ColorCodes", "1234567890abcdef");
        config.addDefault("BossMessage.NoPermission", "&cNo Permission!");
        config.addDefault("BossMessage.Whitelist", false);
        config.addDefault("BossMessage.RepeatRandomColors", true);
        config.addDefault("BossMessage.RepeatRandomPlayers", true);
        
        List<List<String>> exampleList = new ArrayList<List<String>>();
        
        List<String> msg1 = new ArrayList<>();
        msg1.add("&bYo &5%player%&b, wazzup?");
        msg1.add("100");
        msg1.add("100");
        msg1.add("0");
        exampleList.add(msg1);
        
        List<String> msg2 = new ArrayList<>();
        msg2.add("&aBossMessage - best BossBar plugin by &bplay.pixelizedmc.net");
        msg2.add("60");
        msg2.add("100");
        msg2.add("0");
        exampleList.add(msg2);
        
        List<String> msg3 = new ArrayList<>();
        msg3.add("%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors");
        msg3.add("30");
        msg3.add("100");
        msg3.add("0");
        exampleList.add(msg3);
        
        List<String> msg4 = new ArrayList<>();
        msg4.add("&aRight now, there are &b%online_players%&c/&b%max_players% &aPlayers online");
        msg4.add("100");
        msg4.add("100");
        msg4.add("0");
        exampleList.add(msg4);
        
        config.addDefault("BossMessage.Messages", exampleList);
        
        List<String> worldList = new ArrayList<>();
        worldList.add("world");
        worldList.add("world_nether");
        worldList.add("ExampleWorld");
        config.addDefault("BossMessage.WhitelistedWorlds", worldList);
        
        config.addDefault("BossMessage.IgnoredPlayers", new ArrayList<>());
        
        config.options().copyDefaults(true);
        
        save();
    }
    
	@SuppressWarnings("unchecked")
	public static void readConfig() {
        random = config.getBoolean("BossMessage.Random");
        repeatrdmcolors = config.getBoolean("BossMessage.RepeatRandomColors");
        repeatrdmplayers = config.getBoolean("BossMessage.RepeatRandomPlayers");
        noperm = ChatColor.translateAlternateColorCodes('&', config.getString("BossMessage.NoPermission"));
        colorcodes = config.getString("BossMessage.ColorCodes");
        messages = (List<List<String>>) config.getList("BossMessage.Messages");
        whitelist = config.getBoolean("BossMessage.Whitelist");
        worlds = config.getStringList("BossMessage.WhitelistedWorlds");
        ignoreplayers = config.getStringList("BossMessage.IgnoredPlayers");
    }
	
    public static void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("[BossMessage] Error 'createConfig' on " + path);
        }
    }
    
	public static List<String> colorMsg(List<String> msg) {
    	List<String> output = new ArrayList<>();
    	output.add(ChatColor.translateAlternateColorCodes('&', msg.get(0)));
    	output.add(msg.get(1));
    	output.add(msg.get(2));
    	output.add(msg.get(3));
		return output;
    }
}
