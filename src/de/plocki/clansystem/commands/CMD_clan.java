package de.plocki.clansystem.commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.plocki.clansystem.utils.ClanUtils;
import de.plocki.main.Settings;
import de.plocki.utils.EconomyUtils;
import de.plocki.utils.MySQL;

public class CMD_clan implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player p = (Player)sender;
		if(p.hasPermission(Settings.permissionprefix+".clan")) {
			if(args.length==0) {
				p.sendMessage("§e§lClan Hilfe");
				p.sendMessage("");
				p.sendMessage("§7/clan leave <TAG");
				p.sendMessage("§7/clan info <TAG>");
				p.sendMessage("§7Clan menu");
				p.sendMessage("§7/clan create <WUNSCH TAG>");
				p.sendMessage("");
				p.sendMessage("§cBitte beachte, dass das erstellen von einem Clan 50000€ kostet!");
				p.sendMessage("§bDen TAG findest du in der Tablist.");
				p.sendMessage("");
				return false;
			} else if(args.length==2) {
				if(args[0].equalsIgnoreCase("leave")) {
					if(ClanUtils.isInClan(p)==false) {
						p.sendMessage(Settings.prefix+"Du bist in keinem Clan!");
					} else if(ClanUtils.isInClan(p)==true) {
						if(ClanUtils.clanTagExists(args[1])==true) {
							p.sendMessage(Settings.prefix+"Du hast den Clan erfolgreich verlassen.");
							ClanUtils.removePlayerFromClan(p, args[1]);
						} else if(ClanUtils.clanTagExists(args[1])==false) {
							p.sendMessage(Settings.prefix+"Der Eingegebene Clan TAG existiert nicht.");
						}
						return true;
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(ClanUtils.clanTagExists(args[1])==true) {
						p.sendMessage("§e§lClan Info");
						p.sendMessage("");
						p.sendMessage("§7Clan Inhaber: §c"+ClanUtils.getClanLeader(args[1]));
						p.sendMessage("§7Clan TAG: §c"+args[1]);
						p.sendMessage("§7Clan Mitglieder: §c"+ClanUtils.getClanMembersNumber(args[1]));
						p.sendMessage("§7Clan Level: §cKommt Bald!");
						p.sendMessage("");
					} else if(ClanUtils.clanTagExists(args[1])==false) {
						p.sendMessage(Settings.prefix+"Der Eingegebene Clan TAG existiert nicht.");
					}
					return true;
				} else if(args[0].equalsIgnoreCase("create")){
					
					if(ClanUtils.isInClan(p)==false) {
						if(EconomyUtils.getCoins(p)==50000||EconomyUtils.getCoins(p)>=50000) {
							if(args[1].length()==5||args[1].length()<=5) {
								ClanUtils.createClan(p, args[1]);
								ClanUtils.setPlayerInClan(p, args[1]);
								EconomyUtils.removeCoins(p, 50000);
								p.sendMessage(Settings.prefix+"Du hast erfolgreich den Clan §b"+args[1]+ " §7erstellt!");
								return true;
							} else {
								p.sendMessage(Settings.prefix+"Der TAG darf nicht länger als 5 Zeichen sein!");
								return false;
							}
						} else {
							p.sendMessage(Settings.prefix+"Du hast nicht genügend Geld!");
							return false;
						}
					} else if(ClanUtils.isInClan(p)==true) {
						p.sendMessage(Settings.prefix+"§cDu bist bereits in einem Clan!");
					}
					
					return true;
				} else {
					p.sendMessage("§e§lClan Hilfe");
					p.sendMessage("");
					p.sendMessage("§7/clan leave <TAG");
					p.sendMessage("§7/clan info <TAG>");
					p.sendMessage("§7/clan menu");
					p.sendMessage("§7/clan create <WUNSCH TAG>");
					p.sendMessage("");
					p.sendMessage("§cBitte beachte, dass das erstellen von einem Clan §e50000"+Settings.value+" §ckostet!");
					p.sendMessage("§bDen TAG findest du in der Tablist.");
					p.sendMessage("");
				}
			} else if(args.length==1) {
				if(args[0].equalsIgnoreCase("menu")) {
					if(ClanUtils.isInClan(p)==false) {
						p.sendMessage(Settings.prefix+"Du bist in keinem Clan!");
					} else if(ClanUtils.isInClan(p)==true) {
						if(ClanUtils.getClanLeader(ClanUtils.getClanTag(p)).contains(p.getUniqueId().toString())) {
							Inventory inv = Bukkit.createInventory(null, 3*9, "§eClan");
							
							ItemStack deleteClan = new ItemStack(Material.RED_SHULKER_BOX);
							ItemMeta deleteClanMeta = deleteClan.getItemMeta();
							deleteClanMeta.setDisplayName("§cClan Löschen (§4Achtung: §7Keine Bestätigung!§c)");
							deleteClan.setItemMeta(deleteClanMeta);
							
							ItemStack showClanMembers = new ItemStack(Material.PAPER);
							ItemMeta showClanMembersMeta = showClanMembers.getItemMeta();
							showClanMembersMeta.setDisplayName("§aZeige die Clan Mitglieder an");
							showClanMembers.setItemMeta(showClanMembersMeta);
							
							ItemStack levelClan = new ItemStack(Material.EXP_BOTTLE);
							ItemMeta levelClanMeta = levelClan.getItemMeta();
							levelClanMeta.setDisplayName("§eClan Leveln");
							levelClan.setItemMeta(levelClanMeta);
							
							inv.setItem(10, levelClan);
							
							inv.setItem(13, deleteClan);
							
							inv.setItem(16, showClanMembers);
							
							p.openInventory(inv);
						} else if(!(ClanUtils.getClanLeader(ClanUtils.getClanTag(p))==p.getName())) {
							p.sendMessage(Settings.prefix+"Du musst der Clan Inhaber sein um dieses Menü zu Öffnen!");
						}
					}
					
					return true;
				} else if(args[0].equalsIgnoreCase("bugfix")) {
					try {
						PreparedStatement insert= MySQL.con.prepareStatement("INSERT INTO isInClan (UUID, isInClan) VALUES (?,?)");
						insert.setString(1, p.getUniqueId().toString());
						insert.setInt(2, 0);
						insert.executeUpdate();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					p.sendMessage("§e§lClan Hilfe");
					p.sendMessage("");
					p.sendMessage("§7/clan leave <TAG");
					p.sendMessage("§7/clan info <TAG>");
					p.sendMessage("§7/clan menu");
					p.sendMessage("§7/clan create <WUNSCH TAG>");
					p.sendMessage("");
					p.sendMessage("§cBitte beachte, dass das erstellen von einem Clan 50000€ kostet!");
					p.sendMessage("§bDen TAG findest du in der Tablist.");
					p.sendMessage("");
				}
			}
	}
		
		return false;
	}

}
