package fr.alienationgaming.jailworker.listner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

import fr.alienationgaming.jailworker.JailWorker;
import fr.alienationgaming.jailworker.Utils;

public class JWBlockBreakListener implements Listener {
	
	private JailWorker plugin;
	private Utils utils = new Utils(plugin);
	
	public JWBlockBreakListener(JailWorker jailworker) {
		plugin = jailworker;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		String jailName = null;
		
		Set<String> s = plugin.getJailConfig().getConfigurationSection("Jails").getKeys(false);
		Iterator<String> it = s.iterator();
		while (it.hasNext()){
			String elem = (String) it.next();
			World world1 = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + elem + ".World"));
			Vector vec1 = plugin.getJailConfig().getVector("Jails." + elem + ".Location.Block1");
			Vector vec2 = plugin.getJailConfig().getVector("Jails." + elem + ".Location.Block2");
			if (utils.isInRegion(event.getBlock().getLocation(), new Location(world1, vec1.getX(), vec1.getY(), vec1.getZ()), new Location(world1, vec2.getX(), vec2.getY(), vec2.getZ()))){
				jailName = elem;
				break;
			}
		}
		/* If block isnt in a jail */
		if (jailName == null)
			return;
		/* else */
		if (plugin.prisoners.contains( player.getName()) || player.hasPermission("jail.admin.build")){
			if (plugin.prisoners.contains(player.getName())){
				Material type = Material.getMaterial(plugin.getJailConfig().getString("Jails." + jailName + ".Type"));
				if (event.getBlock().getType().getId() == 2)
					event.getBlock().setType(Material.DIRT);
				if (event.getBlock().getType() == type)
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							event.getBlock().setType(type);
						}
					}, 20L*10); //20 Tick (1 Second) delay before run() is called
					event.getBlock().setType(Material.AIR);

					int remain = getRemainingBlocks(player)-1;
					if (remain > 0){
						setRemainingBlocks(player,remain);
						plugin.getJailConfig().set("Prisoners." + player.getName() + ".RemainingBlocks", remain);
						plugin.saveJailConfig();
						plugin.reloadJailConfig();
						player.sendMessage(plugin.toLanguage("info-listener-blockremaning", remain));
					}else {
						plugin.interactWithPlayer.freePlayer(player);
					}
				}else if (event.getBlock().getType() != type) {
					//player.sendMessage(plugin.toLanguage("info-listener-donttryescape"));
					event.setCancelled(true);
				}
				return ;
			}
		}
		else{
			player.sendMessage(plugin.toLanguage("info-listener-notowner"));
			event.setCancelled(true);
		}
	}

	private int getRemainingBlocks(Player p) {
		int i =0;
		try (ResultSet rs = plugin.mysql.query("SELECT * FROM JailWorker WHERE UUID= '" + p.getUniqueId().toString() + "'")) {
			if ((!rs.next()) || (String.valueOf(rs.getString("UUID")) == null)) return i;

			i = rs.getInt("Amount");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return i;
	}

	private void setRemainingBlocks(Player p ,int i) {
		plugin.mysql.update(
				"UPDATE JailWorker" +
				" SET Amount= "+ i +
				" WHERE UUID= '"+p.getUniqueId().toString()+"';");

	}
	ArrayList<String> msgCooldown = new ArrayList<>();
	@EventHandler(priority = EventPriority.HIGH)
	public void itemPickup(PlayerPickupItemEvent e){
		if(plugin.prisoners.contains(e.getPlayer().getName())){
			if(!e.getItem().getItemStack().getType().equals(Material.GOLDEN_PICKAXE) || e.getItem().getItemStack().getItemMeta().hasEnchants()){
				e.setCancelled(true);
				if(!msgCooldown.contains(e.getPlayer().getName())){
					e.getPlayer().sendMessage("Du Darfst hier nur Goldpicken verwenden");
					msgCooldown.add(e.getPlayer().getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							msgCooldown.remove(e.getPlayer().getName());
						}
					}, 200L); //20 Tick (1 Second) delay before run() is called
				}
			}
		}

	}
}
