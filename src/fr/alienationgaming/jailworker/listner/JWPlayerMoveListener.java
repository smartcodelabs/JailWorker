package fr.alienationgaming.jailworker.listner;

import fr.alienationgaming.jailworker.JailWorker;
import fr.alienationgaming.jailworker.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Set;

public class JWPlayerMoveListener implements Listener {
    private Utils utils;// = new Utils(plugin);
    JailWorker plugin;

    public JWPlayerMoveListener(JailWorker plugin) {
        this.plugin = plugin;
        utils = new Utils(plugin);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (plugin.getJailConfig().contains("Prisoners." + player.getName())) {

            String jailName = "jail";

                World world1 = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
                Vector vec1 = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.Block1");
                Vector vec2 = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.Block2");
                if (!utils.isInRegion(e.getPlayer().getLocation(), new Location(world1, vec1.getX(), vec1.getY(), vec1.getZ()), new Location(world1, vec2.getX(), vec2.getY(), vec2.getZ()))){
                    Vector spawn = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.PrisonerSpawn");
                    World world = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
                    player.teleport(new Location(world, spawn.getX(), spawn.getY(), spawn.getZ()));
                    player.sendMessage("Ausbrechen ist nicht!");
                }




        } else return;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (plugin.getJailConfig().contains("Prisoners." + player.getName())) {

            String jailName = "jail";

            World world1 = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
            Vector vec1 = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.Block1");
            Vector vec2 = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.Block2");
            if (!utils.isInRegion(e.getPlayer().getLocation(), new Location(world1, vec1.getX(), vec1.getY(), vec1.getZ()), new Location(world1, vec2.getX(), vec2.getY(), vec2.getZ()))){
                Vector spawn = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.PrisonerSpawn");
                World world = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
                player.teleport(new Location(world, spawn.getX(), spawn.getY(), spawn.getZ()));
                player.sendMessage("Ausbrechen ist nicht!");
            }




        } else return;

    }


}