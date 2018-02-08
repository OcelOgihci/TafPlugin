package fr.ocelogihci.taf;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import fr.ocelogihci.taf.commands.CommandCalmezVous;
import fr.ocelogihci.taf.commands.CommandGod;
import fr.ocelogihci.taf.commands.CommandTaf;

public final class TafPlugin extends JavaPlugin {

	private HashSet<String> m_hsPlayers = new HashSet<String>();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		//Initialisation des commandes
		getCommand("taf").setExecutor(new CommandTaf());
		getCommand("calmezvous").setExecutor(new CommandCalmezVous());
		getCommand("gateofdeath").setExecutor(new CommandGod(this));
		getCommand("godlist").setExecutor(new CommandGod(this));
		getCommand("setgodlimit").setExecutor(new CommandGod(this));
		getServer().getPluginManager().registerEvents(new TafListener(this), this);

		//		config.addDefault("godLimit", true);
		

		System.out.println("[Taf] godLimit : " + this.getConfig().getString("godLimit"));
	}
	
	
	/*********************************************************************  [Tools]  *****************************************************************************************/

	public HashSet<String> getGodPlayers(){
		return m_hsPlayers;
	}
	
	
	public Location getBlockBehindPlayer(Player p) {
		Vector inverseDirectionVec = p.getLocation().toVector().normalize().multiply(-1);
		return p.getLocation().add(inverseDirectionVec);
	}


	public List<Player> getPlayersWithin(EntityDamageEvent e, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == e.getEntity().getWorld() && p.getLocation().distanceSquared(e.getEntity().getLocation()) <= d2) {
				res.add(p);
			}
		}
		return res;
	}
	
	public void removeAllPotionEffects(Player p) {
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}

}
