package net.PixelizedMC.BossMessage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ml
 * Date: 01.09.13
 * Time: 00:42
 * To change this template use File | Settings | File Templates.
 */
public class ConfigManager {

    final static String path = "plugins/BossMessage/config.yml";
    File file = new File(path);
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public boolean enabled = false;
    public boolean random = true;
    public int interval = 60;
    public int show = 20;
    public String colorcodes;
    public List<List<String>> messages;
    public List<String> players;
    public boolean whitelist = false;
    public List<String> worlds;
    //public String configVersion = Main.getInstance().getDescription().getVersion();

    public void createConfig () {
        //if (newConfig() == false)
        //    return;

        config.addDefault("BossMessage.Enabled", true);
        config.addDefault("BossMessage.Random", false);
        config.addDefault("BossMessage.Interval", 250);
        config.addDefault("BossMessage.ColorCodes", "0123456789abcdef");
        config.addDefault("BossMessage.Show", 100);
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
	public void readConfig() {
        enabled = config.getBoolean("BossMessage.Enabled");
        random = config.getBoolean("BossMessage.Random");
        interval = config.getInt("BossMessage.Interval");
        show = config.getInt("BossMessage.Show");
        colorcodes = config.getString("BossMessage.ColorCodes");
        messages = (List<List<String>>) config.getList("BossMessage.Messages");
        players = config.getStringList("BossMessage.IgnorePlayers");
        whitelist = config.getBoolean("BossMessage.Whitelist");
        worlds = config.getStringList("BossMessage.WhitelistedWorlds");
    }

    private void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("[BossMessage] Error 'createConfig' on " + path);
        }
    }
}
