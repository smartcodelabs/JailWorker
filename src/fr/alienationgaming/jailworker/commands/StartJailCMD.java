package fr.alienationgaming.jailworker.commands;

import fr.alienationgaming.jailworker.JailWorker;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StartJailCMD implements CommandExecutor {

	private JailWorker plugin;

	public StartJailCMD(JailWorker plugin) {
		this.plugin = plugin;
//		plugin.getCommand("startjail").setExecutor(this);
	}
	
	public static int cd;
	public static int cdz;
	
	public static File SKTime = new File("SmartServer/Data/StartJail.yml");
	public static YamlConfiguration SK_cfg = YamlConfiguration.loadConfiguration(SKTime);
	

	
	public static ArrayList<String> voting = new ArrayList<>();
	public static ArrayList<String> Ja = new ArrayList<>();
	public static ArrayList<String> Nein = new ArrayList<>();
	public static HashMap<String, String> Name = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			String name = p.getName();
			Long cooldown = plugin.mysql.getCooldown(p);
			String PrefixStartJail = plugin.getConfig().getString("StartJailPrefix").replace("&", "§");
			

			
			if(p.hasPermission("smartserver.startkick.time.bypass") || cooldown<=System.currentTimeMillis()) {
				if(!voting.contains("true")) {
					if(p.hasPermission("smartserver.startkick.use")) {
							if(args.length >= 2) {
								if(!args[0].equalsIgnoreCase(p.getName())) {
									String target = args[0];
									Player tar = Bukkit.getPlayer(target);
									if(tar != null) {
										if(!tar.hasPermission("smartserver.startkick.bypass")) {
											voting.add("true");
											
											Name.put("N", tar.getName());

											cdz = 31;
											
											String Message = "";
											
											for(int i = 1; i < args.length; i++) {
												Message = Message + args[i] + " ";
											}
											
											for(Player all : Bukkit.getOnlinePlayers()) {
												all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
											}
											Bukkit.broadcastMessage(PrefixStartJail + "§e§lAchtung §4§lStartJail §e§lAbstimmung!");
											Bukkit.broadcastMessage("");
											Bukkit.broadcastMessage(PrefixStartJail + "§eSoll der Spieler §a§l" + tar.getName() + " §ein das Jail?" + "\n" + " §e? §a/ja §c/nein");
											Bukkit.broadcastMessage(PrefixStartJail + "§fErsteller: §a" + p.getName());
											Bukkit.broadcastMessage(PrefixStartJail + "§fDauer: §430 Sekunden");
											Bukkit.broadcastMessage(PrefixStartJail + "§fBegründung: §3§l" + Message);
											
											TextComponent JaTxt = new TextComponent();
											JaTxt.setText("§fStimme §afür §fdie Bestrafung von §a" + tar.getName() + " §fab. §a[Klick hier!]");
											JaTxt.setBold(true);
											JaTxt.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,("/Ja")));
											JaTxt.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fStimme §afür §fdie Bestrafung von §a" + tar.getName() + " §fab.").create()));
											
											TextComponent NeinTxt = new TextComponent();
											NeinTxt.setText("§fStimme §cgegen §fdie Bestrafung von §a" + tar.getName() + " §fab. " + "\n" + "§c[Klick hier!]");
											NeinTxt.setBold(true);
											NeinTxt.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,("/Nein")));
											NeinTxt.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fStimme §cgegen §fdie Bestrafung von §a" + tar.getName() + " §fab.").create()));
											
											Bukkit.spigot().broadcast(JaTxt);
											Bukkit.spigot().broadcast(NeinTxt);
											
											cd = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

												@Override
												public void run() {
													
													cdz--;
													
													if(cdz == 3) {
														for(Player all : Bukkit.getOnlinePlayers()) {
															all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
														}
														Bukkit.broadcastMessage(PrefixStartJail + "§aDie Abstimmung den Spieler §b" + tar.getName() + " §azu" + "\n" + " §abestrafen endet in 3 Sekunden!");
													}
													if(cdz == 2) {
														for(Player all : Bukkit.getOnlinePlayers()) {
															all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
														}
														Bukkit.broadcastMessage(PrefixStartJail + "§aDie Abstimmung den Spieler §b" + tar.getName() + " §azu" + "\n" + " §abestrafen endet in 2 Sekunden!");
													}
													if(cdz == 1) {
														for(Player all : Bukkit.getOnlinePlayers()) {
															all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
														}
														Bukkit.broadcastMessage(PrefixStartJail + "§aDie Abstimmung den Spieler §b" + tar.getName() + " §azu" + "\n" + " §abestrafen endet in einer Sekunde!");
													}
													if(cdz == 0) {
														
														voting.remove("true");
														
														if(Ja.size() > Nein.size()) {
															
															for(Player all : Bukkit.getOnlinePlayers()) {
																all.playSound(all.getLocation(), Sound.ENTITY_WITHER_HURT, 10, 1);
															}
															
															Bukkit.broadcastMessage(" ");
															Bukkit.broadcastMessage(" ");
															
															String players = "";
															
															for(String all : Ja) {
																
																players = players + all + "§7, §a";
																
																}
															
															String players2 = "";
															
															for(String all : Nein) {
																
																players2 = players2 + all + "§7, §c";
																
																}
															
															Bukkit.broadcastMessage("§a" + players + "\n" + "§c" + players2);
															
															Bukkit.broadcastMessage(" ");
															
															Bukkit.broadcastMessage(PrefixStartJail + "§cEs wird die einfache Mehrheit benötigt ...");
															Bukkit.broadcastMessage(PrefixStartJail + "§eDie Abstimmung " + tar.getName() + " §eging §a" + Ja.size() + " §ezu §c" + Nein.size() + " §eaus!");
															
															Bukkit.broadcastMessage(PrefixStartJail + "§c§l" + tar.getName() + " §c§lwurde bestraft und für §b§l15" + "\n" + " §b§lBlöcke §c§lin das Jail gesperrt!");
																

													} else {
														
														for(Player all : Bukkit.getOnlinePlayers()) {
															all.playSound(all.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
														}
														
														Bukkit.broadcastMessage(" ");
														Bukkit.broadcastMessage(" ");
														
														String players = "";
														
														for(String all : Ja) {
															
															players = players + all + "§7, §a";
															
															}
														
														String players2 = "";
														
														for(String all : Nein) {
															
															players2 = players2 + all + "§7, §c";
															
															}
														
														Bukkit.broadcastMessage("§a" + players + "\n" + "§c" + players2);
														
														Bukkit.broadcastMessage(" ");
														
														Bukkit.broadcastMessage(PrefixStartJail + "§cEs wird die einfache  Mehrheit benötigt ...");
														Bukkit.broadcastMessage(PrefixStartJail + "§eDie Abstimmung " + tar.getName() + " §eging §a" + Ja.size() + " §ezu §c" + Nein.size() + " §eaus!");
														Bukkit.broadcastMessage(PrefixStartJail + "§a" + tar.getName() + " §awurde nicht bestraft!");
														
													}

														Bukkit.broadcastMessage(PrefixStartJail + "§aDer Chat ist wieder freigegeben!");
														
														Name.clear();
														
														Ja.clear();
														Nein.clear();
														
														
														Bukkit.getScheduler().cancelTask(cd);
														cdz = 31;
															
													}
													
												}
												
											}, 20, 20);
										} else {
											
											p.kickPlayer("§4§lStart§e§lJail" + "\n" + "\n" + "             §cDu kannst diesen Spieler nicht §4§lStart§e§lJailen§c!");
											
										}
										
									} else {
										p.sendMessage(PrefixStartJail + "§cDer Spieler §a" + args[0] + " §cist nicht online!");
									}
								} else {
									p.sendMessage(PrefixStartJail + "§cDu kannst dich nicht selber §e§ljailen§c!");
								}
							} else {
								p.sendMessage(PrefixStartJail + "§cBenutze: §a/§4§lStart§e§lJail §a<Spieler> <Grund>");
							}
						} else {
							p.sendMessage(PrefixStartJail + "§aKaufe dir diesen Befehl bei §d/perk§a.");
						}
				} else {
					p.sendMessage(PrefixStartJail + "§cEs läuft bereits eine Abstimmung!");
				}
			} else {
				
				Date date = new Date(cooldown);
				String mm_dd_yyyy = new SimpleDateFormat("dd.MM.yyyy").format(date);
				String hour_min = new SimpleDateFormat("HH:mm").format(date);
				
				p.sendMessage(PrefixStartJail + "§c§lDu kannst diesen Befehl erst wieder am" + "\n" + " §a§l" + mm_dd_yyyy + " §c§lum §a§l" + hour_min + " §c§lUhr benutzen.");
			}
				
			}
		
		return true;
	}
	
	public void SetSkTime(Player p, int time) {
		
		SK_cfg.set(p.getName(), System.currentTimeMillis() + time*1000);
		try {
			SK_cfg.save(SKTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


}
