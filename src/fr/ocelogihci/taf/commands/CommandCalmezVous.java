package fr.ocelogihci.taf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandCalmezVous implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			Player l_Player = (Player)sender;
			Server l_Server = l_Player.getServer();
			if(args.length != 0) {
				String l_strParam = args[0];
				
				if (cmd.getName().equalsIgnoreCase("calmezvous")){
					if (l_Player.getPlayer().hasPermission("opcalmezvous"));

					if (l_Player.getServer().getPlayer(l_strParam) != null) {//Vérifie l'existance de la cible
						//On execute la commande
						l_Server.broadcastMessage(ChatColor.BLUE + l_strParam + ChatColor.WHITE +" : J'ai MAAAAAL!");
						l_Server.broadcastMessage(ChatColor.GREEN + l_Player.getPlayer().getName() + ChatColor.WHITE + " applique des Calmez-Vous! sur " + ChatColor.BLUE + l_strParam);

						l_Server.getPlayer(l_strParam).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 3));
					}
					else {
						//Message d'erreur
						l_Player.getPlayer().sendMessage(ChatColor.RED + "Oui... d'accord.");
					}
					return true;
				}
			}
		}
		return false;
	}

}
