package votepackage;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class SettingsManager {

	static SettingsManager instance = new SettingsManager();
	FileConfiguration config;
	File configFile;
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	public void setup(Plugin p) {
		config = p.getConfig();
		config.options().copyDefaults(true);
		configFile = new File(p.getDataFolder(), "config.yml");
		saveConfig();
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save the config.yml");
		}
	}
	
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);				
	}
	
}
