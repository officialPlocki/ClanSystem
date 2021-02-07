package de.plocki.clansystem.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.plocki.clansystem.commands.CMD_clan;
import de.plocki.clansystem.utils.ClanInventoryUtils;
import de.plocki.clansystem.utils.ClanLevelInventory;
import de.plocki.utils.MySQL;

public class Main extends JavaPlugin{
	
	//Clan Invite
	//Clan Level Inventory
	
	public void onEnable() {
		MySQL.connect();
		MySQL.createTable();
		
		getCommand("clan").setExecutor(new CMD_clan());
		Bukkit.getPluginManager().registerEvents(new ClanLevelInventory(), this);
		Bukkit.getPluginManager().registerEvents(new ClanInventoryUtils(), this);
	}
	public void onDisable() {
		MySQL.disconnect();
	}
}
