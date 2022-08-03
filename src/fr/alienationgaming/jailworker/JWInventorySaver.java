package fr.alienationgaming.jailworker;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import de.smartbotstudios.jailworker.utils.ItemsToBase64;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JWInventorySaver {
	JailWorker plugin;
	public JWInventorySaver(JailWorker jailworker){
		plugin = jailworker;
	}

	public void clear(Player player){
		player.getInventory().clear();
		player.getInventory().setBoots(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setHelmet(null);
	}
	public void save(Player player){
		//TODO SQL Verbindung etc.

		ItemsToBase64.itemStackArrayToBase64(player.getInventory().getContents());
		ItemsToBase64.itemStackArrayToBase64(player.getInventory().getArmorContents());

		for(int i = 0; i < player.getInventory().getContents().length; i++){
            final ItemStack itemStack = player.getInventory().getContents()[i];
            if(itemStack!= null)
            	plugin.getJailConfig().set("Prisoners." + player.getName() + ".Inventory.Item-" + i, itemStack);
        }
		plugin.getJailConfig().set("Prisoners." + player.getName() + ".Inventory.Helmet", player.getInventory().getHelmet());
		plugin.getJailConfig().set("Prisoners." + player.getName() + ".Inventory.Chestplate", player.getInventory().getChestplate());
		plugin.getJailConfig().set("Prisoners." + player.getName() + ".Inventory.Leggings", player.getInventory().getLeggings());
		plugin.getJailConfig().set("Prisoners." + player.getName() + ".Inventory.Boots", player.getInventory().getBoots());
	}
	public void restore(Player player){
//TODO SQL Verbindung etc.
		ItemStack[] i = new ItemStack[0];
		try {
			i = ItemsToBase64.itemStackArrayFromBase64("Path");
			player.getInventory().setContents(i);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}




		try {
			i = ItemsToBase64.itemStackArrayFromBase64("Path");
			player.getInventory().setArmorContents(i);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}



		Set<String> s = plugin.getJailConfig().getConfigurationSection("Prisoners." + player.getName() + ".Inventory").getKeys(false);
		Iterator<String> it = s.iterator();
		ItemStack boots = plugin.getJailConfig().getItemStack("Prisoners." + player.getName() + ".Inventory.Boots");
		ItemStack leggings = plugin.getJailConfig().getItemStack("Prisoners." + player.getName() + ".Inventory.Leggings");
		ItemStack chestplate = plugin.getJailConfig().getItemStack("Prisoners." + player.getName() + ".Inventory.Chestplate");
		ItemStack helmet = plugin.getJailConfig().getItemStack("Prisoners." + player.getName() + ".Inventory.Helmet");

		while (it.hasNext()){
			String elem = (String) it.next();
			if (elem.split("-")[0].contentEquals("Item")){
				ItemStack item = plugin.getJailConfig().getItemStack("Prisoners." + player.getName() + ".Inventory." + elem);
				player.getInventory().setItem(Integer.parseInt(elem.split("-")[1]), item);
			}
		}
		player.getInventory().setBoots(boots);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setHelmet(helmet);
	}
}