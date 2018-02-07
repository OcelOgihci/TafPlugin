package fr.ocelogihci.taf;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;


public class TafCommandListener implements CommandExecutor, Listener {
	public static HashSet<String> g_hsPlayers = new HashSet<String>();


	/*********************************************************************  [Commands]  *****************************************************************************************/

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player)sender;
			Plugin plugin = player.getServer().getPluginManager().getPlugin("Taf");

			if(args.length != 0) {
				String param1 = args[0];

				if (cmd.getName().equalsIgnoreCase("taf")){ //Vérifie la commande entrée 
					if (player.getPlayer().hasPermission("op.taf"));


					if (player.getServer().getPlayer(param1) != null) {  //Vérifie l'existance de la cible
						//On execute la commande
						plugin.getServer().broadcastMessage(ChatColor.RED + player.getPlayer().getName() + ChatColor.WHITE + " a tapé " + ChatColor.BLUE + param1);
						plugin.getServer().broadcastMessage(ChatColor.BLUE + param1 + ChatColor.WHITE +" devient bleu, cyanosé.");

						plugin.getServer().getPlayer(param1).damage(15);
					}
					else {
						//Message d'erreur
						player.getPlayer().sendMessage(ChatColor.RED + "Tu as tapé dans le vide!" + ChatColor.DARK_RED + " Entity '"+ args[0] + "' cannot be find");
					}

					return true;
				}

				if (cmd.getName().equalsIgnoreCase("calmezvous")){
					if (player.getPlayer().hasPermission("opcalmezvous"));

					if (player.getServer().getPlayer(param1) != null) {//Vérifie l'existance de la cible
						//On execute la commande
						plugin.getServer().broadcastMessage(ChatColor.BLUE + param1 + ChatColor.WHITE +" : J'ai MAAAAAL!");
						plugin.getServer().broadcastMessage(ChatColor.GREEN + player.getPlayer().getName() + ChatColor.WHITE + " applique des Calmez-Vous! sur " + ChatColor.BLUE + param1);

						plugin.getServer().getPlayer(param1).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 3));
					}
					else {
						//Message d'erreur
						player.getPlayer().sendMessage(ChatColor.RED + "Oui... d'accord.");
					}
					return true;
				}

				if (cmd.getName().equalsIgnoreCase("setgodlimit")){
					plugin.getConfig().set("godLimit", Boolean.parseBoolean(args[0]));
					plugin.saveConfig();
					plugin.getServer().broadcastMessage("[GodLimit] : " + plugin.getConfig().getBoolean("godLimit"));

					plugin.getServer().broadcastMessage(ChatColor.GREEN +"[GodLimit] set to " + plugin.getConfig().getBoolean("godLimit"));
					return true;
				}
			}


			if (cmd.getName().equalsIgnoreCase("gateofdeath")){
				if(!g_hsPlayers.contains(player.getName())) {
					if(plugin.getConfig().getBoolean("godLimit") && g_hsPlayers.size() == 0 || !plugin.getConfig().getBoolean("godLimit")) {
						player.setAllowFlight(true);
						g_hsPlayers.add(player.getName());
						//						player.sendMessage(player.getName() + " a été ajouté à test.");

						plugin.getServer().broadcastMessage(ChatColor.DARK_RED + player.getPlayer().getName() +" ouvre la huitième porte!");

						removeAllPotionEffects(player);
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 3));
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 3));
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 3));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 3));
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 2));
						player.setFireTicks(1180);


						//						player.sendMessage(g_hsPlayers.toString());

						BukkitScheduler scheduler = player.getServer().getScheduler();
						BukkitScheduler scheduler2 = player.getServer().getScheduler();
						scheduler2.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								Location loc = player.getLocation().clone().subtract(0,1,0);
								loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
								player.setFireTicks(0);

							}
						}, 20L);

						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								if(g_hsPlayers.contains(player.getName()) && player.getHealth() > 0) {
									Location loc = player.getLocation().clone().subtract(0,1,0);
									loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
									removeAllPotionEffects(player);

									player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 3));
									player.setHealth(1);
									player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 2));
									player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 2));
									player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 2));
									player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 2));
									player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 0));
									player.getServer().broadcastMessage(ChatColor.GREEN + player.getPlayer().getName() +" est presque mort.");

									player.setAllowFlight(false);
									g_hsPlayers.remove(player.getName());
								}
							}
						}, 1200L);

						return true;
					}else {
						player.sendMessage(ChatColor.RED + "Un joueur a déjà ouvert la huitième porte et la limite est fixée (voir /setgodlimit) ");
						return true;

					}
				}else {
					player.sendMessage(ChatColor.RED + "Tu as déjà ouvert la huitième porte !");
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("godlist")){
				player.sendMessage(g_hsPlayers.toString());
				return true;
			}

		}
		return false;

	}

	/*********************************************************************  [Events]  *******************************************************************************/

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();

			if (g_hsPlayers.contains(p.getName()) && e.getPlayer().getName() == p.getName()) {

				if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR) ) {
					Vector deuxBlksDnt = p.getEyeLocation().getDirection().normalize().multiply(5);
					Location loc = p.getEyeLocation().add(deuxBlksDnt);
					loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3.0F,false, false);

				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			if (g_hsPlayers.contains(p.getName()) && p.getName() == e.getPlayer().getName()) {
				Location loc = getBlockBehindPlayer(p);
				loc.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);

			}
		}
	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player dead = (Player) e.getEntity();

		if(e.getEntity().getKiller() instanceof Player) {

			Player killer = dead.getKiller();

			if (g_hsPlayers.contains(dead.getName())) {
				dead.sendMessage(dead.getName() + " a été retiré de la list");
				e.setDeathMessage(ChatColor.RED + dead.getName() + ChatColor.WHITE + " a claqué la porte, contraint par " + ChatColor.GREEN + killer.getName());
				dead.setAllowFlight(false);
				//			p.setFlying(false);
			}else if(g_hsPlayers.contains(killer.getName())) {
				e.setDeathMessage(ChatColor.RED + dead.getName() + ChatColor.WHITE + " a reçu une tatane de " + ChatColor.GREEN + killer.getName());

			}



		}else
		if(g_hsPlayers.contains(dead.getName()) && !(e.getEntity().getKiller() instanceof Player)) {
			g_hsPlayers.remove(dead.getName());
			dead.sendMessage("coucou");
		}


	}


	@EventHandler
	public void handleDamage(EntityDamageEvent e) {
		if(e.isCancelled()) { return;}

		for (Player p: getPlayersWithin(e, 6)) {
			if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
				//				e.getEntity().sendMessage(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " a été affecté par l'explosion"); 
				if(e.getEntity() instanceof Player) {
					if(g_hsPlayers.contains(p.getName())) {
						//						e.getEntity().sendMessage(ChatColor.RED + "Explosion cancelled");
						e.setCancelled(true);

					}else {
						//						e.getEntity().sendMessage(ChatColor.GREEN + "Explosion uncancelled");
						Player target = p;
						target.damage(5);
					}
				}
			}

		}
		//		e.getEntity().sendMessage("************************");
	}



	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		Player p = (Player) e.getPlayer();
		if (p.getGameMode() != GameMode.CREATIVE) {
			if(g_hsPlayers.contains(p.getName())) {
				Vector dir = p.getEyeLocation().getDirection();
				p.setVelocity(e.getPlayer().getVelocity().add(dir.multiply(4)));
				p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 150);
				p.playSound(e.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 10, -10);
			} 
			//					e.getPlayer().sendMessage(ChatColor.RED + p.getName() + " : " + p.isFlying());
			e.setCancelled(true);


		}


	}


	/*********************************************************************  [Tools]  *****************************************************************************************/


	public Location getBlockBehindPlayer(Player p) {
		Vector inverseDirectionVec = p.getLocation().toVector().normalize().multiply(-1);
		return p.getLocation().add(inverseDirectionVec);
	}

	public void removeAllPotionEffects(Player p) {
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
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


}


/*********************************************************************    *****************************************************************************************/



