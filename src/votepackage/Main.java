package votepackage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.VotifierEvent;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{
	
	public static Main plugin;
	SettingsManager settings = SettingsManager.getInstance();
	
	public void onEnable(){
		plugin = this;
		settings.setup(this);
		saveDefaultConfig();
		new Reward_functions();
		new Plugin_functions();
		Bukkit.getPluginManager().registerEvents(this, this);
		if(settings.getData().get("server_votes") == null) {
			settings.getData().set("server_votes", 0);
			settings.saveData();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		Plugin_functions.checkPlayerNull(uuid);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String now = format.format(new Date());
		String playerDate = (String) settings.getData().get("players." + uuid + ".last_joined");
		if(playerDate == null) {
			
		}else {
		
			if(!(playerDate.equals(now.toString()))) {
				settings.getData().set("players." + uuid + ".last_joined", now);
				settings.saveData();
				player.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")) + Plugin_functions.displayColoredText(getConfig().getString("motd")));
			}
		}
	}
	
	@EventHandler
    public void onPlayerVote(VotifierEvent e) {
		UUID uuid = Bukkit.getPlayer(e.getVote().getUsername()).getUniqueId();
		Reward_functions.giveRewards(uuid);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rewardvoting")) {
			UUID uuid = Bukkit.getServer().getPlayer(sender.getName()).getUniqueId();
			if (args.length == 0) {
				sender.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")));
				sender.sendMessage(ChatColor.BOLD + "" + ChatColor.WHITE + "/rewardvoting [votes/player]");
			} else {
				if (args[0].equalsIgnoreCase("reload")) {
					// reload plugin files.
					sender.sendMessage("Plugin reloaded");
				} else if (args[0].equalsIgnoreCase("votes")) {
					sender.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")) + Plugin_functions.displayColoredText(getConfig().getString("server_vote").replace("{votes}", settings.getData().getString("server_votes"))));
				} else if (args[0].equalsIgnoreCase("player")) {
					sender.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")) + sender.getName());
					sender.sendMessage("Last_joined: " + settings.getData().getString("players." + uuid + ".last_joined"));
					sender.sendMessage("Votes: " + settings.getData().getString("players." + uuid + ".votes"));
					sender.sendMessage("Queued-Votes: " + settings.getData().getString("players." + uuid + ".queued_votes"));
				} else {
					sender.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")) + " unknown argument");
					sender.sendMessage(ChatColor.BOLD + "" + ChatColor.WHITE + "/rewardvoting [votes/player]");
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("vote")) {
			sender.sendMessage(Plugin_functions.displayColoredText(getConfig().getString("header")));
			for (String link : getConfig().getStringList("links")){
				sender.sendMessage(Plugin_functions.displayColoredText(link));
			}
		} else if (cmd.getName().equalsIgnoreCase("test")) {
			UUID uuid = Bukkit.getPlayer(sender.getName()).getUniqueId();
			Reward_functions.giveRewards(uuid);
		}
		return true;
	}
}
