package fr.alienationgaming.jailworker.commands;

import fr.alienationgaming.jailworker.JailWorker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NeinCMD implements CommandExecutor {

	private JailWorker plugin;
	
	public NeinCMD(JailWorker plugin) {
		this.plugin = plugin;
//		plugin.getCommand("nein").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			String PrefixStartKick = plugin.getConfig().getString("StartKickPrefix").replace("&", "§");
			
			if(StartJailCMD.voting.contains("true")) {
				if(!StartJailCMD.Ja.contains(p.getName()) && !StartJailCMD.Nein.contains(p.getName())) {

					StartJailCMD.Nein.add(p.getName());
					p.sendMessage(PrefixStartKick + "§eDu hast für §cNein §eabgestimmt!");
					
				} else {
					p.sendMessage(PrefixStartKick + "§cDu hast bereits abgestimmt!");
				}
			} else {
				p.sendMessage(PrefixStartKick + "§eEs läuft keine Abstimmung!");
			}
		} else {
			
			String PrefixStartKick = plugin.getConfig().getString("StartKickPrefix").replace("&", "§");
			
			Bukkit.getConsoleSender().sendMessage(PrefixStartKick + "§cDie §4§lConsole §cdarf nicht mit Abstimmen.");
		}
		
		return true;
	}
}