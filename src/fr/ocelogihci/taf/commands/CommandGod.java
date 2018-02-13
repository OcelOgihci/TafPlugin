package fr.ocelogihci.taf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.ocelogihci.taf.TafPlugin;

public class CommandGod implements CommandExecutor {
	private TafPlugin m_tfMain;

	public CommandGod(TafPlugin plugin) {
		this.m_tfMain = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			Player l_Player = (Player)sender;
			Server l_Server = l_Player.getServer();
			
			// /gateofdeath
			if (cmd.getName().equalsIgnoreCase("gateofdeath")){
				if(!m_tfMain.getGodPlayers().contains(l_Player.getName())) {
					if(m_tfMain.getConfig().getBoolean("godLimit") && m_tfMain.getGodPlayers().size() == 0 || !m_tfMain.getConfig().getBoolean("godLimit")) {
						l_Player.setAllowFlight(true);
						m_tfMain.getGodPlayers().add(l_Player.getName());
						//						player.sendMessage(player.getName() + " a été ajouté à test.");

						l_Server.broadcastMessage(ChatColor.DARK_RED + l_Player.getPlayer().getName() +" ouvre la huitième porte!");

						m_tfMain.removeAllPotionEffects(l_Player);
						l_Player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 3));
						l_Player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 3));
						l_Player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 3));
						l_Player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 3));
						l_Player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 2));
						l_Player.setFireTicks(1180);


						//						player.sendMessage(g_hsPlayers.toString());

						BukkitScheduler l_schGodRemover = l_Player.getServer().getScheduler();
						BukkitScheduler l_schAirRemover = l_Player.getServer().getScheduler();
						l_schAirRemover.scheduleSyncDelayedTask(m_tfMain, new Runnable() {
							@Override
							public void run() {
								Location loc = l_Player.getLocation().clone().subtract(0,1,0);
								loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
								l_Player.setFireTicks(0);

							}
						}, 20L);

						l_schGodRemover.scheduleSyncDelayedTask(m_tfMain, new Runnable() {
							@Override
							public void run() {
								if(m_tfMain.getGodPlayers().contains(l_Player.getName()) && l_Player.getHealth() > 0) {
									Location loc = l_Player.getLocation().clone().subtract(0,1,0);
									loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
									m_tfMain.removeAllPotionEffects(l_Player);

									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 3));
									l_Player.setHealth(1);
									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 2));
									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 2));
									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 2));
									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 2));
									l_Player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 0));
									l_Player.getServer().broadcastMessage(ChatColor.GREEN + l_Player.getPlayer().getName() +" est presque mort.");

									l_Player.setAllowFlight(false);
									m_tfMain.getGodPlayers().remove(l_Player.getName());
								}
							}
						}, 1200L);

						return true;
					}else {
						l_Player.sendMessage(ChatColor.RED + "Un joueur a déjà ouvert la huitième porte et la limite est fixée (voir /setgodlimit) ");
						return true;

					}
				}else {
					l_Player.sendMessage(ChatColor.RED + "Tu as déjà ouvert la huitième porte !");
					return true;
				}
			}
			
			// /godlist
			if (cmd.getName().equalsIgnoreCase("godlist")){
				l_Player.sendMessage(m_tfMain.getGodPlayers().toString());
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("heal")) {
				l_Player.sendMessage(ChatColor.GREEN + "Vous avez été soigné(e)");
				l_Player.setHealth(20);
				return true;
			}
			
			
			// /setgodlimit
			if(args.length != 0) {
				String l_strParam = args[0];

				if (cmd.getName().equalsIgnoreCase("setgodlimit")){
					m_tfMain.getConfig().set("godLimit", Boolean.parseBoolean(l_strParam));
					m_tfMain.saveConfig();
					l_Server.broadcastMessage("[GodLimit] : " + m_tfMain.getConfig().getBoolean("godLimit"));

					l_Server.broadcastMessage(ChatColor.GREEN +"[GodLimit] set to " + m_tfMain.getConfig().getBoolean("godLimit"));
					return true;
				}
			}

		}

		return false;
	}

}
