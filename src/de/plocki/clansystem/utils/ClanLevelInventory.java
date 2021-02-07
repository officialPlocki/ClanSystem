package de.plocki.clansystem.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.plocki.main.Settings;
import de.plocki.utils.EconomyUtils;
import de.plocki.utils.MySQL;

public class ClanLevelInventory implements Listener{
	
	@EventHandler
	public static void onJoin(PlayerJoinEvent e) {
		if(e.getPlayer().hasPlayedBefore()==false) {
			try {
				PreparedStatement insert= MySQL.con.prepareStatement("INSERT INTO isInClan (UUID,INT) VALUES (?,?)");
				insert.setString(1, e.getPlayer().getUniqueId().toString());
				insert.setInt(2, 0);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public static void onClick(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();

		int level = ClanUtils.getclanLevel(ClanUtils.getClanTag(p))*100000;
		int levelItemLevel = ClanUtils.getclanLevel(ClanUtils.getClanTag(p))+1;
		if(e.getView().getTitle().equalsIgnoreCase("§eNächstes Clan Level")) {
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eLevel "+levelItemLevel+" §7- §b"+level+Settings.value)) {
				if(EconomyUtils.getCoins(p)==level||EconomyUtils.getCoins(p)>=level) {
					EconomyUtils.removeCoins(p, level);
					p.sendMessage(Settings.prefix+"Du hast erfolgreich für §b"+ level+Settings.value+ "§7 das Clan Level auf §e"+levelItemLevel+"§7 erhöht!");
					return;
				} else {
					p.sendMessage(Settings.prefix+"Du hast nicht genügend Geld.");
					return;
				}
			}
		}
	}
	
	public static void openClanLevelInventory(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§eNächstes Clan Level");
		
		int level = ClanUtils.getclanLevel(ClanUtils.getClanTag(p))*100000;
		int levelItemLevel = ClanUtils.getclanLevel(ClanUtils.getClanTag(p))+1;
		ItemStack levelItem = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta levelItemMeta = levelItem.getItemMeta();
		levelItemMeta.setDisplayName("§eLevel "+levelItemLevel+" §7- §b"+level+Settings.value);
		levelItem.setItemMeta(levelItemMeta);
		
		ItemStack maxLevelItem = new ItemStack(Material.RED_SHULKER_BOX);
		ItemMeta maxLevelItemMeta = maxLevelItem.getItemMeta();
		maxLevelItemMeta.setDisplayName("§cMaximales Level (§7Kein Upgrade verfügbar§c)");
		maxLevelItem.setItemMeta(maxLevelItemMeta);
		
		if(!(ClanUtils.getclanLevel(ClanUtils.getClanTag(p))>=5)) {
			inv.setItem(4, levelItem);
			return;
		} else {
			inv.setItem(4, maxLevelItem);
			return;
		}
		
	}
	
}
