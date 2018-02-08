package fr.ocelogihci.taf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTaf implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player l_Player = (Player)sender;
			Server l_Server = l_Player.getServer();
			
			if(args.length != 0) {
				String l_strParam = args[0];

				if (cmd.getName().equalsIgnoreCase("taf")){ //Vérifie la commande entrée 
					if (l_Player.getPlayer().hasPermission("op.taf"));


					if (l_Player.getServer().getPlayer(l_strParam) != null) {  //Vérifie l'existance de la cible
						//On execute la commande
						l_Server.broadcastMessage(ChatColor.RED + l_Player.getPlayer().getName() + ChatColor.WHITE + " a tapé " + ChatColor.BLUE + l_strParam);
						l_Server.broadcastMessage(ChatColor.BLUE + l_strParam + ChatColor.WHITE +" devient bleu, cyanosé.");

						l_Server.getPlayer(l_strParam).damage(15);
					}
					else {
						//Message d'erreur
						l_Player.getPlayer().sendMessage(ChatColor.RED + "Tu as tapé dans le vide!" + ChatColor.DARK_RED + " Entity '"+ args[0] + "' cannot be find");
					}

					return true;
				}
			}
		}
		return false;

	}

}
