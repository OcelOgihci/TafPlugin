package fr.ocelogihci.taf;


import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TafPlugin extends JavaPlugin {

	private File configf;
	 private FileConfiguration config;

	@Override
	public void onEnable() {
		//Initialisation des commandes
		CommandExecutor tafExecutor = new TafCommandListener(); 
		getCommand("taf").setExecutor(tafExecutor);
		getCommand("calmezvous").setExecutor(tafExecutor);
		getCommand("gateofdeath").setExecutor(tafExecutor);
		getCommand("godlist").setExecutor(tafExecutor);
		getCommand("setgodlimit").setExecutor(tafExecutor);
		getServer().getPluginManager().registerEvents(new TafCommandListener(), this);
		
//		config.addDefault("godLimit", true);
		createConfig();
		
		System.out.println("[Taf] godLimit : " + config.getString("godLimit"));
	}
//	@Override
//	public FileConfiguration getConfig() {
//        return this.config;
//    }
//	
	private void createConfig() {
	    try {
	        if (!getDataFolder().exists()) {
	            getDataFolder().mkdirs();
	        }
	        configf = new File(getDataFolder(), "config.yml");
	        if (!configf.exists()) {
	            getLogger().info("Config.yml not found, creating!");
	            saveDefaultConfig();
	        } else {
	            getLogger().info("Config.yml found, loading!");
	        }
	        config = new YamlConfiguration();
	        try {
	            config.load(configf);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();

	    }

	}


}
