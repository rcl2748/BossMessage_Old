package net.PixelizedMC.BossMessage;

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

    public static boolean enabled = false;
    public static boolean random = true;
    public static int interval = 60;
    public static int show = 20;
    public static String colorcodes;
    public static List<List<String>> messages;
    public static List<List<String>> rawmessages;
    public static String noperm;
    public static List<String> players;
    public static boolean whitelist = false;
    public static List<String> worlds;
    //public String configVersion = Main.getInstance().getDescription().getVersion();

    public static void createConfig() {
        //if (newConfig() == false)
        //    return;

        config.addDefault("BossMessage.Enabled", true);
        config.addDefault("BossMessage.Random", false);
        config.addDefault("BossMessage.Interval", 250);
        config.addDefault("BossMessage.Show", 100);
        config.addDefault("BossMessage.ColorCodes", "'0123456789abcdef'");
        config.addDefault("BossMessage.NoPermission", "&cNo Permission!");
        config.addDefault("BossMessage.Whitelist", false);

        List<List<String>> exampleList = new ArrayList<List<String>>();
        
        List<String> msg1 = new ArrayList<>();
        msg1.add("&bYo &5%player%&b, wazzup?");
        msg1.add("100");
        exampleList.add(msg1);

        List<String> msg2 = new ArrayList<>();
        msg2.add("&aBossMessage - best BossBar plugin by &bplay.pixelizedmc.net");
        msg2.add("60");
        exampleList.add(msg2);
        
        List<String> msg3 = new ArrayList<>();
        msg3.add("%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors");
        msg3.add("30");
        exampleList.add(msg3);
        
        config.addDefault("BossMessage.Messages", exampleList);

        List<String> playersList = new ArrayList<>();
        playersList.add("testPlayer");
        playersList.add("examplePlayer");
        playersList.add("your name");
        config.addDefault("BossMessage.IgnorePlayers", playersList);

        List<String> worldList = new ArrayList<>();
        worldList.add("world");
        worldList.add("world_nether");
        worldList.add("ExampleWorld");
        config.addDefault("BossMessage.WhitelistedWorlds", worldList);

        //config.set("BossMessage.configVersion", Main.getInstance().getDescription().getVersion());

        config.options().copyDefaults(true);

        save();
    }
    
	@SuppressWarnings("unchecked")
	public static void readConfig() {
        enabled = config.getBoolean("BossMessage.Enabled");
        random = config.getBoolean("BossMessage.Random");
        interval = config.getInt("BossMessage.Interval");
        show = config.getInt("BossMessage.Show");
        noperm = ChatColor.translateAlternateColorCodes('&', config.getString("BossMessage.NoPermission"));
        colorcodes = config.getString("BossMessage.ColorCodes");
        rawmessages = (List<List<String>>) config.getList("BossMessage.Messages");
        messages = getMsgs();
        players = config.getStringList("BossMessage.IgnorePlayers");
        whitelist = config.getBoolean("BossMessage.Whitelist");
        worlds = config.getStringList("BossMessage.WhitelistedWorlds");
    }

    public static void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("[BossMessage] Error 'createConfig' on " + path);
        }
    }
    
    @SuppressWarnings("unchecked")
	public static List<List<String>> getMsgs() {
    	List<List<String>> output = new ArrayList<List<String>>();
    	List<List<String>> msgs = (List<List<String>>) config.getList("BossMessage.Messages");
    	for (List<String> msg:msgs) {
    		List<String> a = new ArrayList<>();
    		a.add(ChatColor.translateAlternateColorCodes('&', msg.get(0)));
    		a.add(msg.get(1));
    		output.add(a);
    	}
		return output;
    }
}
