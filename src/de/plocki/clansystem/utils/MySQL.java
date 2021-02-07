package de.plocki.clansystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.plocki.utils.ConfigManager;





public class MySQL {
  public static Connection con;
  static String host = ConfigManager.cfg.getString("Host");
  static String db = ConfigManager.cfg.getString("Database");
  static String user = ConfigManager.cfg.getString("User");
  static String pw = ConfigManager.cfg.getString("Password");
  
  public static void connect() {
    if (!isConnected()) {
      try {
        con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?autoReconnect=true", user, pw);
        System.out.println("MySQL verbunden!");
      } catch (SQLException e) {
        
        e.printStackTrace();
      } 
    }
  }
  
  public static void disconnect() {
    if (!isConnected()) {
      try {
        con.close();
        System.out.println("MySQL gentrennt!");
      } catch (SQLException e) {
        
        e.printStackTrace();
      } 
    }
  }
  
  public static void createTable() {
    try {
      con.prepareStatement("CREATE TABLE IF NOT EXISTS clanTable (MEMBERS INT(16), TAG VARCHAR(5), LEADER VARCHAR(50), LEVEL INT(1))").executeUpdate();
    } catch (SQLException e) {
      
      e.printStackTrace();
    } 
    try {
        con.prepareStatement("CREATE TABLE IF NOT EXISTS isInClan (UUID VARCHAR(50), isInClan INT(1))").executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
    try {
      con.prepareStatement("CREATE TABLE IF NOT EXISTS ClanMembersTable (TAG VARCHAR(5), UUID VARCHAR(50))").executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  private static boolean isConnected() {
    return (con != null);
  }
}