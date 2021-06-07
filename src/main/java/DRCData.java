package main.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class DRCData {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	public static List<List<Object>> getRelatedAlertKeys() {
		Connection Localconnection = null;
		PreparedStatement Localstmt;
		ResultSet Localrs;
		List<List<Object>> ret = new ArrayList<>();


		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_CDI_DB"), settings.getProperty("USER_CDI_DB"), settings.getProperty("PASSWORD_CDI_DB"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_GET_CDI_ALERTS"),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Localrs = Localstmt.executeQuery();

				while (Localrs.next()) {
					List<Object> row = new ArrayList<Object>();
					String NODE = Localrs.getString(1);
					row.add(NODE);
					String ALERTGROUP = Localrs.getString(2);
					row.add(ALERTGROUP);
					String INSTANCEID = Localrs.getString(3);
					row.add(INSTANCEID);
					String ALERTKEY = Localrs.getString(4);
					row.add(ALERTKEY);
					logger.info("CIOAPP parameters from CDI: NODE: " + NODE + " | ALERTGROUP: " + ALERTGROUP  + " | INSTANCEID: " + INSTANCEID + " | ALERTKEY: " + ALERTKEY);
					ret.add(row);
				}

			Localrs.close();
			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					//ok as designed
				}
			}
		}
		return ret;
	}

	public static boolean insertRootCause(long alertId, String Code, String Name, float frequency) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_INSERT_ROOTCAUSE"));
			Localstmt.setLong(1, alertId);
			Localstmt.setString(2, Code);
			Localstmt.setString(3, Name);
			Localstmt.setFloat(4, frequency);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return true;
	}

	public static boolean insertResolution(long alertId, String Code, float Confidence) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_INSERT_RESOLUTION_CODE"));
			Localstmt.setLong(1, alertId);
			Localstmt.setString(3, Code);
			Localstmt.setFloat(4, Confidence);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return true;
	}

	public static boolean insertNode(long alertId, String name, float frequency) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_INSERT_NODE"));
			Localstmt.setLong(1, alertId);
			Localstmt.setString(2, name);
			Localstmt.setFloat(3, frequency);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return true;
	}

	public static boolean insertAutomaton(long alertId, String Name, float Confidence) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_INSERT_AUTOMATON"));
			Localstmt.setLong(1, alertId);
			Localstmt.setString(2, Name);
			Localstmt.setFloat(3, Confidence);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return true;
	}

	public static int insertAlert(String NODE, String ALERTGROUP,String INSTANCEID,String ALERTKEY) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;
		int WatsonResultID = 0;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_INSERT_ALERT"));
			Localstmt.setString(1, NODE);
			Localstmt.setString(2, ALERTGROUP);
			Localstmt.setString(3, INSTANCEID);
			Localstmt.setString(4, ALERTKEY);
			Localstmt.execute();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return WatsonResultID;
	}

	public static int getAlertID(String NODE,String ALERTGROUP,String INSTANCEID,String ALERTKEY) {
		int alertID = 0;
		Connection Localconnection = null;
		PreparedStatement Localstmt;
		ResultSet Localrs;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_GET_ALERT_ID"), ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Localstmt.setString(1, NODE);
			Localstmt.setString(2, ALERTGROUP);
			Localstmt.setString(3, INSTANCEID);
			Localstmt.setString(4, ALERTKEY);
			Localrs = Localstmt.executeQuery();

			if (Localrs.next()) {
				alertID = Localrs.getInt(1);
			}else {
				//insert
			}

			Localrs.close();
			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return alertID;
	}

	public static void deleteResolutionCodes(long alertId) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_DELETE_RESOLUTION"));
			Localstmt.setLong(1, alertId);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	public static void deleteNodes(long alertId) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_DELETE_NODE"));
			Localstmt.setLong(1, alertId);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	public static void deleteRootCauseCodes(long alertId) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_DELETE_ROOT"));
			Localstmt.setLong(1, alertId);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	public static void deleteAutomatons(long alertId) {
		Connection Localconnection = null;
		PreparedStatement Localstmt;

		try {
			Settings settings = new Settings();
			Properties queries = new Properties();
			try {
				queries.load(new FileInputStream("queries.properties"));
			}catch(FileNotFoundException e) {
				queries.load(DRCData.class.getClassLoader().getResourceAsStream("queries.properties"));
			}
			
			Localconnection = DBConnection.getConnection(settings.getProperty("URL_DRC"), settings.getProperty("USER_DRC"), settings.getProperty("PASSWORD_DRC"));
			Localconnection.setAutoCommit(false);

			Localstmt = Localconnection.prepareStatement(queries.getProperty("DRC_DELETE_AUTOMATON"));
			Localstmt.setLong(1, alertId);
			Localstmt.executeUpdate();

			Localstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (Localconnection != null) {
				try {
					Localconnection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}
}
