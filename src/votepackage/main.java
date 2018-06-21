package votepackage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.net.VoteReceiver;

public class main extends JavaPlugin implements Listener{
	
	SettingsManager settings = SettingsManager.getInstance();
	
	public void onEnable(){
		settings.setup(this);
	}
	
	@EventHandler
    public void onPlayerVote(Votifier e) {
        VoteReceiver vote = e.getVoteReceiver();
        Bukkit.broadcastMessage("There has been voted");
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rewardvoting")){
			sender.sendMessage("Test");
		}
		return true;
	}
}
