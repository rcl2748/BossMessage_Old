package net.pixelizedmc.bossmessage.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class DataManager {
	
    final static String path = "plugins/BossMessage/data.yml";
    static File file = new File(path);
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    
    public static void createConfig() {
    	config.options().copyDefaults(true);
        save();
    }
    
	public static void save() {
        try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void reloadConfig() {
	    config = YamlConfiguration.loadConfiguration(file);
	}
}