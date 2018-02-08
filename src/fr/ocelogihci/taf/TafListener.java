package fr.ocelogihci.taf;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
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
import org.bukkit.util.Vector;

public class TafListener implements Listener {
	private TafPlugin m_tfMain;

	public TafListener(TafPlugin plugin) {
		this.m_tfMain = plugin;
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(!(e.getPlayer() instanceof Player)) return;
		
		Player p = (Player) e.getPlayer();

		if (m_tfMain.getGodPlayers().contains(p.getName()) && e.getPlayer().getName() == p.getName()) {

			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR) ) {
				Vector deuxBlksDnt = p.getEyeLocation().getDirection().normalize().multiply(5);
				Location loc = p.getEyeLocation().add(deuxBlksDnt);
				loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3.0F,false, false);
			}
		}

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(!(e.getPlayer() instanceof Player)) return;
		
		Player p = (Player) e.getPlayer();
		if (m_tfMain.getGodPlayers().contains(p.getName()) && p.getName() == e.getPlayer().getName()) {
			Location loc = m_tfMain.getBlockBehindPlayer(p);
			loc.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);

		}

	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player dead = (Player) e.getEntity();

		if(e.getEntity().getKiller() instanceof Player) {

			Player killer = dead.getKiller();

			if (m_tfMain.getGodPlayers().contains(dead.getName())) {
				dead.sendMessage(dead.getName() + " a été retiré de la list");
				e.setDeathMessage(ChatColor.RED + dead.getName() + ChatColor.WHITE + " a claqué la porte, contraint par " + ChatColor.GREEN + killer.getName());
				dead.setAllowFlight(false);
				//			p.setFlying(false);
			}else if(m_tfMain.getGodPlayers().contains(killer.getName())) {
				e.setDeathMessage(ChatColor.RED + dead.getName() + ChatColor.WHITE + " a reçu une tatane de " + ChatColor.GREEN + killer.getName());

			}



		}else
			if(m_tfMain.getGodPlayers().contains(dead.getName()) && !(e.getEntity().getKiller() instanceof Player)) {
				m_tfMain.getGodPlayers().remove(dead.getName());
			}


	}


	@EventHandler
	public void handleDamage(EntityDamageEvent e) {
		if(e.isCancelled()) { return;}

		for (Player p: m_tfMain.getPlayersWithin(e, 6)) {
			if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
				//				e.getEntity().sendMessage(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " a été affecté par l'explosion"); 
				if(e.getEntity() instanceof Player) {
					if(m_tfMain.getGodPlayers().contains(p.getName())) {
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
			if(m_tfMain.getGodPlayers().contains(p.getName())) {
				Vector dir = p.getEyeLocation().getDirection();
				p.setVelocity(e.getPlayer().getVelocity().add(dir.multiply(4)));
				p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 150);
				p.playSound(e.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 10, -10);
			} 
			//					e.getPlayer().sendMessage(ChatColor.RED + p.getName() + " : " + p.isFlying());
			e.setCancelled(true);


		}


	}







}



