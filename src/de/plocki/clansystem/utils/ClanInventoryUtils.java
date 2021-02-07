package de.plocki.clansystem.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClanInventoryUtils implements Listener{
	@EventHandler
	public static void onClick(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		String item = e.getCurrentItem().getItemMeta().getDisplayName();
		
		if(e.getView().getTitle().equalsIgnoreCase("§eClan")) {
			e.setCancelled(true);
			if(item.equalsIgnoreCase("§cClan Löschen (§4Achtung: §7Keine Bestätigung!§c)")) {
				ClanUtils.deleteClan(ClanUtils.getClanTag(p));
				p.closeInventory();
			} else if (item.equalsIgnoreCase("§aZeige die Clan Mitglieder an")) {
				ClanUtils.getClanMembers(ClanUtils.getClanTag(p));
				p.closeInventory();
			} else if(item.equalsIgnoreCase("§eClan Leveln")) {
				p.closeInventory();
				try {
					Thread.sleep(20*2);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ClanLevelInventory.openClanLevelInventory(p);
			}
		}
	}
}
