package fr.alienationgaming.jailworker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Utils {
	JailWorker plugin;
	JWPlayerInteract iv = new JWPlayerInteract(plugin);
	public Utils(JailWorker jailWorker)
	{
		plugin = jailWorker;
	}


	public String getDate()
	{
		/* Get Date*/
		@SuppressWarnings("unused")
		Locale locale = Locale.getDefault();
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(actuelle);

		return date;
	}
	public boolean isInRegion(Location myLoc, Location loc1, Location loc2)
	{
		if (((Math.min(loc1.getBlockX(), loc2.getBlockX()) <= myLoc.getBlockX()) && (myLoc.getBlockX() <= Math.max(loc1.getBlockX(), loc2.getBlockX()))) && 
				((Math.min(loc1.getBlockY(), loc2.getBlockY()) <= myLoc.getBlockY()) && (myLoc.getBlockY() <= Math.max(loc1.getBlockY(), loc2.getBlockY()))) &&
				((Math.min(loc1.getBlockZ(), loc2.getBlockZ()) <= myLoc.getBlockZ()) && (myLoc.getBlockZ() <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()))))
		{
			return true;
		}
		return false;
	}
	public void printBlockPos(Player player, Block block)
	{
		player.sendMessage(ChatColor.BLUE + "x :" + ChatColor.RESET + block.getX());
		player.sendMessage(ChatColor.BLUE + "y :" + ChatColor.RESET + block.getY());
		player.sendMessage(ChatColor.BLUE + "z :" + ChatColor.RESET + block.getZ());
	}

	@SuppressWarnings("deprecation")
	public int getNbrBlockInRegion(int BlockId, Location loc1, Location loc2)
	{
		int res = 0;
		int x = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int y = Math.min(loc1.getBlockY(), loc2.getBlockY()) - 1;
		int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		Location tmpLoc = new Location(loc1.getWorld(), x, y, z);

		while (z < Math.max(loc1.getBlockZ(), loc2.getBlockZ()))
		{
			y = Math.min(loc1.getBlockY(), loc2.getBlockY()) - 1;
			while (y < Math.max(loc1.getBlockY(), loc2.getBlockY()))
			{
				x = Math.min(loc1.getBlockX(), loc2.getBlockX());
				while (x < Math.max(loc1.getX(), loc2.getX()))
				{	
					tmpLoc.setX(x);
					if (tmpLoc.getBlock().getType().getId() == BlockId || (BlockId == -1 && tmpLoc.getBlock().getType().getId() != 0))
					{
						res++;
					}
					x++;
				}
				y++;
				tmpLoc.setY(y);
			}
			z++;
			tmpLoc.setZ(z);
		}
		return (res);
	}

	@SuppressWarnings("deprecation")
	public int DelBlocksInRegion(int BlockId, Location loc1, Location loc2)
	{
		int res = 0;
		int x = (int) Math.min(loc1.getX(), loc2.getX());
		int y = (int) (Math.min(loc1.getY(), loc2.getY()) - 1);
		int z = (int) Math.min(loc1.getZ(), loc2.getZ());
		Location tmpLoc = new Location(loc1.getWorld(), x, y, z);

		while (z <= Math.max(loc1.getZ(), loc2.getZ())){
			y = (int) (Math.min(loc1.getY(), loc2.getY()) - 1);
			while (y <= Math.max(loc1.getY(), loc2.getY())){
				x = (int) Math.min(loc1.getX(), loc2.getX());
				while (x <= Math.max(loc1.getX(), loc2.getX())){	
					tmpLoc.setX(x);
					if (tmpLoc.getBlock().getType().getId() == BlockId){
						tmpLoc.getBlock().setType(Material.AIR);
						res++;
					}
					x++;
				}
				y++;
				tmpLoc.setY(y);
			}
			z++;
			tmpLoc.setZ(z);
		}
		return (res);
	}
}
