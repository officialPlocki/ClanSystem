package de.plocki.clansystem.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.plocki.utils.MySQL;

public class ClanUtils {
	static ArrayList<String> clanPlayers = new ArrayList<String>();
	public static boolean isInClan(Player p) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT isInClan FROM isInClan WHERE UUID = ?");
			st.setString(1, p.getUniqueId().toString());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				if(rs.getInt("isInClan")==1) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static void update(Player p) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT isInClan FROM isInClan WHERE UUID = ?");
			st.setString(1, p.getUniqueId().toString());
			ResultSet rs = st.executeQuery();
			if(rs==null) {
				PreparedStatement insert = MySQL.con.prepareStatement("INSERT INTO isInClan (UUID, isInClan) VALUES (?,?)");
				insert.setString(1, p.getUniqueId().toString());
				insert.setInt(2, 0);
				insert.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void setPlayerInClan(Player p, String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO ClanMembersTable (TAG, UUID) VALUES (?,?)");
			st.setString(1, clanTag);
			st.setString(2, p.getUniqueId().toString());
			st.executeUpdate();
			PreparedStatement st2 = MySQL.con.prepareStatement("UPDATE isInClan SET isInClan = ? WHERE UUID = ?");
			st2.setInt(1, 1);
			st2.setString(2, p.getUniqueId().toString());
			st2.executeUpdate();
			PreparedStatement members = MySQL.con.prepareStatement("SELECT MEMBERS FROM clanTable WHERE TAG = ?");
			members.setString(1, clanTag);
			ResultSet mrs = members.executeQuery();
			if(mrs.next()) {
				int membersnow = mrs.getInt("MEMBERS");
				PreparedStatement st3 = MySQL.con.prepareStatement("UPDATE clanTable SET MEMBERS = ? WHERE TAG = ?");
				st3.setInt(1, membersnow+1);
				st3.setString(2, clanTag);
				st3.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void removePlayerFromClan(Player p, String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("DELETE FROM ClanMembersTable WHERE UUID = ?");
			st.setString(1, p.getUniqueId().toString());
			st.executeUpdate();
			PreparedStatement members = MySQL.con.prepareStatement("SELECT MEMBERS FROM clanTable WHERE TAG = ?");
			members.setString(1, clanTag);
			ResultSet mrs = st.executeQuery();
			if(mrs.next()) {
				int membersnow = mrs.getInt("MEMBERS");
				PreparedStatement st2 = MySQL.con.prepareStatement("UPDATE clanTable SET MEMBERS = ? WHERE TAG = ?");
				st2.setInt(1, membersnow-1);
				st2.setString(2, clanTag);
				st2.executeUpdate();
				PreparedStatement usr = MySQL.con.prepareStatement("UPDATE isInClan SET isInClan = ? WHERE UUID = ?");
				usr.setInt(1, 0);
				usr.setString(2, p.getUniqueId().toString());
				usr.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removePlayersFromClan(Player p, String clanTag) {
			try {
				PreparedStatement st = MySQL.con.prepareStatement("DELETE FROM ClanMembersTable WHERE UUID = ?");
				st.setString(1, p.getUniqueId().toString());
				st.executeUpdate();
				PreparedStatement members = MySQL.con.prepareStatement("SELECT MEMBERS FROM clanTable WHERE TAG = ?");
				members.setString(1, clanTag);
				ResultSet mrs = st.executeQuery();
				if(mrs.next()) {
					int membersnow = mrs.getInt("MEMBERS");
					PreparedStatement st2 = MySQL.con.prepareStatement("UPDATE clanTable SET MEMBERS = ? WHERE TAG = ?");
					st2.setInt(1, membersnow-1);
					st2.setString(2, clanTag);
					st2.executeUpdate();
					PreparedStatement usr = MySQL.con.prepareStatement("UPDATE isInClan SET isInClan = ? WHERE UUID = ?");
					usr.setInt(1, 0);
					usr.setString(2, p.getUniqueId().toString());
					usr.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public static void setClanLeader(Player p, String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("UPDATE clanTable SET LEADER = ? WHERE TAG = ?");
			st.setString(1, p.getUniqueId().toString());
			st.setString(2, clanTag);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String getClanTag(Player p) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT TAG FROM ClanMembersTable WHERE UUID = ?");
			st.setString(1, p.getUniqueId().toString());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getString("TAG");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	public static void addclanLevel(String clanTag) {
		
		try {
			PreparedStatement st = MySQL.con.prepareStatement("UPDATE clanTable SET LEVEL = ? WHERE TAG = ?");
			st.setInt(1, getclanLevel(clanTag)+1);
			st.setString(2, clanTag);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Integer getclanLevel(String clanTag) {
		
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT LEVEL FROM clanTable WHERE TAG = ?");
			st.setString(1, clanTag);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getInt("LEVEL");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return -1;
	}
	
	public static void createClan(Player clanLeader, String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO clanTable (MEMBERS, TAG, LEADER, LEVEL) VALUES (?,?,?,?)");
			st.setInt(1, 1);
			st.setString(2, clanTag);
			st.setString(3, clanLeader.getUniqueId().toString());
			st.setInt(4, 0);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void deleteClan(String clanTag) {
		for(String all : getClanMembers(clanTag)) {
			Player p = (Player)Bukkit.getPlayer(all);
			removePlayersFromClan(p, clanTag);
		}
		try {
			PreparedStatement st = MySQL.con.prepareStatement("DELETE FROM clanTable WHERE TAG = ?");
			st.setString(1, clanTag);
			st.executeUpdate();
			PreparedStatement members = MySQL.con.prepareStatement("DELETE FROM ClanMembersTable WHERE TAG = ?");
			members.setString(1, clanTag);
			members.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static String getClanLeader(String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT LEADER FROM clanTable WHERE TAG = ?");
			st.setString(1, clanTag);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getString("LEADER");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	public static ArrayList<String> getClanMembers(String clanTag){
		ArrayList<String> clanPlayers = new ArrayList<String>();
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT UUID FROM ClanMembersTable WHERE TAG = ?");
			st.setString(1, clanTag);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				clanPlayers.add(Bukkit.getOfflinePlayer(rs.getString("UUID")).getName().toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clanPlayers;
	}
	
	public static Boolean clanTagExists(String clanTag) {
		try {
			PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM clanTable WHERE TAG = ?");
			st.setString(1, clanTag);
			ResultSet rs = st.executeQuery();
			if(rs.next()==false) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static Integer getClanMembersNumber(String clanTag) {
	      try {
		      PreparedStatement st = MySQL.con.prepareStatement("SELECT MEMBERS FROM clanTable WHERE TAG = ?");
			  st.setString(1, clanTag);
			  ResultSet rs = st.executeQuery();
			  if(rs.next()) {
				  return rs.getInt("MEMBERS");
			  }
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}
}
