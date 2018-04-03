package com.sjwebb.pi.temp.controllers.database;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sjwebb.pi.temp.database.SensorDatabaseManager;
import com.sjwebb.pi.temp.database.SensorDatabaseSupport;

/**
 * Provides REST resources for interaction with stored sensor data
 * 
 * @author Sam Webb
 *
 */
@RequestMapping("/data/")
@RestController
public class DatabaseController {

	/**
	 * Get the current reading of the named sensor
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{sensor}/record/count", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Integer getSensorReading(@PathVariable("sensor") String sensor) throws IOException {

		Optional<Integer> count;
		try (Connection connection = SensorDatabaseManager.getInstance().createConnection();
				SensorDatabaseSupport support = new SensorDatabaseSupport(connection);) {

			count = support.getRecordCountForSensor(sensor);

			if (!count.isPresent())
				throw new IOException("Count not returned for sensor " + sensor);

			support.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Error with database query", e);
		}

		return count.get();
	}

}
