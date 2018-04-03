package com.sjwebb.pi.temp.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Simple interaction and management of a H2 database to store the sensor data.
 * 
 * @author Sam Webb
 *
 */
public class SensorDatabaseSupport implements AutoCloseable {
	private PreparedStatement addData;
	private PreparedStatement getRecordCountForSensor;
	private Connection connection;

	public SensorDatabaseSupport(Connection connection) throws SQLException {

		this.connection = connection;
		addData = connection.prepareStatement(
				"INSERT INTO " + SensorDatabaseManager.SCHEMA_NAME + ".RESULTS(sensor, temp) VALUES(?, ?);");

		getRecordCountForSensor = connection.prepareStatement("SELECT COUNT(*) FROM "
				+ SensorDatabaseManager.FULLY_QUALIFIED_RESULT_TABLE_NAME + " WHERE SENSOR = ?;");
	}

	/**
	 * Log the sensor data into the database
	 * 
	 * @param sensor
	 *            The unique sensor name
	 * @param temp
	 *            The temperature reading of the sensor
	 * @throws SQLException
	 * @throws IOException
	 */
	public void logResult(String sensor, double temp) throws SQLException, IOException {
		addData.setString(1, sensor);
		addData.setDouble(2, temp);

		int rowCount = addData.executeUpdate();

		if (rowCount != 1)
			throw new IOException("Expected to add 1 row but added " + rowCount);
	}

	@Override
	public void close() throws Exception {

		if (!connection.isClosed())
			connection.close();
	}

	/**
	 * Get the number of records for the specified sensor
	 * 
	 * @param id  The ID of the sensor
	 * @return
	 * @throws SQLException
	 */
	public Optional<Integer> getRecordCountForSensor(String id) throws SQLException {
		
		getRecordCountForSensor.setString(1, id);
		
		ResultSet result = getRecordCountForSensor.executeQuery();
		
		Optional<Integer> count;
		
		if(result.next())
		{
			count = Optional.ofNullable(result.getInt(1));
		} else
		{
			count = Optional.empty();
		}
		
		return count;
	}

}
