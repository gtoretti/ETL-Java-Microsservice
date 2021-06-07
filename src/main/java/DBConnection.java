package main.java;

import java.sql.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class DBConnection {
  private static String driverName = "com.mysql.jdbc.Driver";
  private static Connection con;

  private static final Logger logger = LogManager.getRootLogger();
  
  public static Connection getConnection(String urlstring, String username, String password) {
    try {
      Class.forName(driverName);
      try {
        con = DriverManager.getConnection(urlstring, username, password);
      } catch (SQLException e) {
    	  e.printStackTrace();
    	  logger.error(e);
      }
    } catch (ClassNotFoundException e) {
    	logger.error(e);
    }
    return con;
  }
}  
