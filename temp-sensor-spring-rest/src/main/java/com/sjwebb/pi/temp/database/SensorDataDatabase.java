package com.sjwebb.pi.temp.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.Server;

/**
 * Simple interaction and management of a H2 database to store the sensor data.
 * 
 * @author Sam Webb
 *
 */
public class SensorDataDatabase {
	
	// TODO: allow to be specified in application properties?
	/** Location for the database */
	public static final String DATABASE_PATH = "~/.temperature/data";

	public static final String SCHEMA_NAME = "PI_TEMP_DATA";
	
	private PreparedStatement addData;
	
	private Server server;
	
	public SensorDataDatabase() throws SQLException {
		createConnection();
	}

	private Connection createConnection() throws SQLException {
		
		// TODO: change to a logger instead of sysout
		System.out.println("Starting database server");
		startServer();
		System.out.println("Server status: " + server.getStatus());
		System.out.println("Server url: " + server.getURL());
		System.out.println("Server port: " + server.getPort());

		// TODO: 	allow property to specify embedded vs server mode?
		// 			server mode allows connections to the database outside
		//			of this application
//		Connection conn = DriverManager.getConnection("jdbc:h2:" + DATABASE_PATH);
		
		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/" + DATABASE_PATH);

		initDB(conn);
		
		addData = conn.prepareStatement("INSERT INTO " + SCHEMA_NAME + ".RESULTS(sensor, temp) VALUES(?, ?);");

		return conn;
	}

	/**
	 * Running as a server allows other connections to the database
	 * @throws SQLException
	 */
	private void startServer() throws SQLException {
		server = Server.createTcpServer("-tcpAllowOthers");
		server.start();
	}
	
	/**
	 * Stop the database server
	 */
	public void stopServer() {
		server.stop();
	}

	/**
	 * Initialise the database if it does not already exist
	 * @param conn
	 * @throws SQLException
	 */
	private void initDB(Connection conn) throws SQLException {
		ResultSet schemas = conn.createStatement().executeQuery("SHOW SCHEMAS");

		boolean exists = false;
		
		while (schemas.next()) {
			if (schemas.getString("SCHEMA_NAME").equals(SCHEMA_NAME)) {
				exists = true;
			}
		}

		if(!exists) {
			createSchema(conn);
		} else
		{
			System.out.println("Database already exists");
		}
	}

	/**
	 * Create the database schema allowing for storing the sensor data with
	 * time stamp.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void createSchema(Connection conn) throws SQLException {
		String createSchema = "CREATE SCHEMA PI_TEMP_DATA;\r\n" + 
				"\r\n" + 
				"CREATE TABLE PI_TEMP_DATA.RESULTS(\r\n" + 
				"    index INT NOT NULL auto_increment,\r\n" + 
				"    sensor VARCHAR(64) NOT NULL,\r\n" + 
				"    temp double NOT NULL,\r\n" + 
				"   timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP\r\n" + 
				");\r\n" + 
				"ALTER TABLE  PI_TEMP_DATA.RESULTS ADD PRIMARY KEY (index);";
		
		conn.createStatement().executeUpdate(createSchema);
	}
	
	/**
	 * Log the sensor data into the database
	 * 
	 * @param sensor	The unique sensor name
	 * @param temp		The temperature reading of the sensor
	 * @throws SQLException
	 * @throws IOException
	 */
	public void logResult(String sensor, double temp) throws SQLException, IOException {
		addData.setString(1, sensor);
		addData.setDouble(2, temp);
		
		int rowCount = addData.executeUpdate();
		
		if(rowCount != 1)
			throw new IOException("Expected to add 1 row but added " + rowCount);
	}

}
