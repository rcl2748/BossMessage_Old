package net.pixelizedmc.bossmessage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.pixelizedmc.bossmessage.commands.Commands;
import net.pixelizedmc.bossmessage.configuration.CM;
import net.pixelizedmc.bossmessage.lang.Lang;
import net.pixelizedmc.bossmessage.listeners.OnPlayerDeath;
import net.pixelizedmc.bossmessage.listeners.OnPlayerJoin;
import net.pixelizedmc.bossmessage.listeners.OnPlayerPortal;
import net.pixelizedmc.bossmessage.listeners.OnPlayerQuit;
import net.pixelizedmc.bossmessage.listeners.OnPlayerTeleport;
import net.pixelizedmc.bossmessage.listeners.OnRegionEntered;
import net.pixelizedmc.bossmessage.listeners.OnRegionLeft;
import net.pixelizedmc.bossmessage.utils.BungeeCordChannelListener;
import net.pixelizedmc.bossmessage.utils.Message;
import net.pixelizedmc.bossmessage.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance = null;
	public static final String PREFIX_ERROR = "�4[�cBossMessage�4] �6";
	public static final String PREFIX_NORMAL = "�2[�aBossMessage�2] �e";
	public static final String PREFIX_ERROR_MULTILINE = "�3============== �4[�cBossMessage�4] �3==============";
	public static final String PREFIX_NORMAL_MULTILINE = "�3============== �2[�aBossMessage�2] �3==============";
	public static final String PREFIX_LIVE_MULTILINE = "�3============ �2[�aBossMessage�4�olive�2] �3============";
	public static final String PREFIX_LIVE = "�2[�aBossMessage�6�olive�2] �e";
	public static final String PREFIX_LIVE_ERROR = "�4[�cBossMessage�6�olive�4] �6";
	public static final String PREFIX_CONSOLE = "[BossMessage] ";
	public static final String BUKKIT_LINK = "http://dev.bukkit.org/bukkit-plugins/bossmessage/";
	public static PluginManager plm = Bukkit.getPluginManager();
	public static BukkitScheduler scr = Bukkit.getScheduler();
	public static Map<String, Message> current;
	public static Economy econ;
	public static Permission permManager;
	public static boolean useVault = false;
	public static boolean useWorldGuard = false;
	public static WorldGuardPlugin worldGuard;
	public static File file;
	public static boolean updater_available;
	public static String version;
	public static String updater_name;
	public static String updater_version;
	public static String updater_link;
	public static Logger logger = Bukkit.getLogger();
	public static ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
	public static Map<String, Messager> messagers = new HashMap<String, Messager>();
	public static Messager nullGroupMsgr = new Messager(null, false);
	
	public void onEnable() {
		instance = this;
		version = getDescription().getVersion();
		file = getFile();
		
		// Hook in BarAPI
		if (plm.getPlugin("BarAPI") == null) {
			logger.severe(PREFIX_CONSOLE + "THIS PLUGIN REQUIRES BARAPI TO BE INSTALLED!!!");
			logger.severe(PREFIX_CONSOLE + "BOSSMESSAGE IS NOW BEING DISABLED!!!");
			plm.disablePlugin(this);
			return;
		}
		
		// config
		ConfigurationSerialization.registerClass(Message.class, "ConfigurationMessage");
		CM.createConfig();
		CM.readConfig();
		
		// Load lang
		Lang.loadLang();
		
		// Register commands
		getCommand("bm").setExecutor(new Commands());
		
		// Hook in Metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			logger.warning(PREFIX_CONSOLE + "BossMessage couldn't connect to Metrics :(");
		}
		
		// Hook in Vault
		if (setupVault()) {
			useVault = true;
			logger.info(PREFIX_CONSOLE + "Successfully hooked in to Vault");
		} else {
			logger.warning(PREFIX_CONSOLE + "Vault is either not installed, or not enabled! Using economy and tag %rank% will not be possible!");
		}
		
		// Hook in VNP
		if (CM.useVNP) {
			if (plm.getPlugin("VanishNoPacket") != null) {
				logger.info(PREFIX_CONSOLE + "Successfully hooked in to VanishNoPacket!");
			} else {
				CM.useVNP = false;
				logger.warning(PREFIX_CONSOLE + "ATTENTION!!! VanishNoPacket is NOT installed!");
			}
		}
		
		// Hook in WorldGuard
		Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
		Plugin wgEvents = Bukkit.getPluginManager().getPlugin("WGRegionEvents");
		if (wg != null && wgEvents != null) {
			logger.info(PREFIX_CONSOLE + "Successfully hooked in to WorldGuard");
			worldGuard = (WorldGuardPlugin) wg;
			Bukkit.getPluginManager().registerEvents(new OnRegionEntered(), this);
			Bukkit.getPluginManager().registerEvents(new OnRegionLeft(), this);
			useWorldGuard = true;
		} else {
			logger.warning(PREFIX_CONSOLE + "WorldGuard and/or WorldGuardRegionEvents are either not installed, or not enabled! Having per-region messages will not be possible!");
		}
		
		// BungeeCord
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordChannelListener());
		
		// Updater
		if (CM.checkUpdates) {
			checkUpdate();
		}
		
		// Register events
		Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerTeleport(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerPortal(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerDeath(), this);
		
		// enabled
		startProcess();
	}
	
	public static void stopProcess() {
		for (String group : CM.groups) {
			messagers.get(group).stop();
		}
		messagers.clear();
	}
	
	public static void startProcess() {
		for (String group : CM.groups) {
			messagers.put(group, new Messager(group));
		}
		CM.regions = CM.readRegionGroups();
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static boolean setupVault() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		RegisteredServiceProvider<Permission> pp = Bukkit.getServicesManager().getRegistration(Permission.class);
		if (rsp == null || pp == null) {
			return false;
		}
		permManager = pp.getProvider();
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public static void checkUpdate() {
		Updater updater = new Updater(getInstance(), 64888, file, Updater.UpdateType.NO_DOWNLOAD, false, null);
		updater_available = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
		updater_name = updater.getLatestName();
		updater_version = updater.getLatestGameVersion();
		updater_link = updater.getLatestFileLink();
	}
}
