package votepackage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.VotifierEvent;

public class Main extends JavaPlugin implements Listener{
	
	SettingsManager settings = SettingsManager.getInstance();
	
	public void onEnable(){
		settings.setup(this);
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		if(settings.getData().get("server_votes") == null) {
			settings.getData().set("server_votes", 0);
			settings.saveData();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		checkPlayerNull(player);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String now = format.format(new Date());
		String playerDate = (String) settings.getData().get("players." + player.getName() + ".last_joined");
		if(playerDate == null) {
			
		}else {
		
			if(!(playerDate.equals(now.toString()))) {
				settings.getData().set("players." + player.getName() + ".last_joined", now);
				settings.saveData();
				player.sendMessage(displayColoredText(getConfig().getString("header")) + displayColoredText(getConfig().getString("motd")));
			}
		}
	}
	
	@EventHandler
    public void onPlayerVote(VotifierEvent e) {
//		String player = e.getVote().getUsername();
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rewardvoting")) {
			if (args.length == 0) {
				settings.getData().set("players." + sender.getName() + ".votes",sender.getName().length());
				settings.saveData();
				sender.sendMessage("Player setted");
			} else {
				if (args[0].equalsIgnoreCase("reload")) {
					// reload plugin files.
					sender.sendMessage("Plugin reloaded");
				} else if (args[0].equalsIgnoreCase("votes")) {
					sender.sendMessage(displayColoredText(getConfig().getString("header")) + displayColoredText(getConfig().getString("server_vote").replace("{votes}", settings.getData().getString("server_votes"))));
				} else if (args[0].equalsIgnoreCase("player")) {
					sender.sendMessage(displayColoredText(getConfig().getString("header")) + sender.getName());
					sender.sendMessage(ChatColor.DARK_AQUA + " Last joined: " + ChatColor.RESET + settings.getData().getString("players." + sender.getName() + ".last_joined"));
					sender.sendMessage(ChatColor.DARK_AQUA + " Votes: " + ChatColor.RESET + settings.getData().getString("players." + sender.getName() + ".votes"));
					sender.sendMessage(ChatColor.DARK_AQUA + " Queued votes: " + ChatColor.RESET + settings.getData().getString("players." + sender.getName() + ".queued_votes"));

				} else {
					sender.sendMessage("Unknown command");
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("vote")) {
			sender.sendMessage(displayColoredText(getConfig().getString("header")));
			for (String link : getConfig().getStringList("links")){
				sender.sendMessage(displayColoredText(link));
			}
		} else if (cmd.getName().equalsIgnoreCase("test")) {
			giveRewards(sender.getName());
		}
		return true;
	}
	
	public void checkPlayerNull(Player p) {
		if(settings.getData().get("players." + p.getName()) == null){
			settings.getData().set("players." + p.getName() + ".last_joined", "01-01-1970");
			settings.getData().set("players." + p.getName() + ".votes", 0);
			settings.getData().set("players." + p.getName() + ".queued_votes", 0);
		}
	}
	public String displayColoredText(String text) {
		text = ChatColor.translateAlternateColorCodes('&', text);
		text = ChatColor.translateAlternateColorCodes('$', text);
		text = ChatColor.translateAlternateColorCodes('%', text);
		return text;
	}
	public void giveRewards(String p) {
        Bukkit.broadcastMessage(displayColoredText(getConfig().getString("header")) + displayColoredText(getConfig().getString("broadcast").replace("{username}", p)));
	}
}
