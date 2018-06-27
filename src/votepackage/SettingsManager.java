package votepackage;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager {

	static SettingsManager instance = new SettingsManager();
	
	FileConfiguration data;
	File dataFile;
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	public void setup(Plugin p) {		
		if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
		}
		dataFile = new File(p.getDataFolder(), "data.yml");
		if (!dataFile.exists()) {
            try {
                    dataFile.createNewFile();
            } catch (IOException e) {
                    Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
            }
		}
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	public FileConfiguration getData() {
		return data;
	}
	
	public void saveData() {
		try {
			data.save(dataFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save the data.yml");
		}
	}
	
	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dataFile);				
	}
}
