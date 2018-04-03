package com.sjwebb.pi.temp.controllers.sensor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sjwebb.pi.temp.sensor.SensorHelper;

/**
 * Provides REST resources for interaction with live sensor data
 * 
 * @author Sam Webb
 *
 */
@RequestMapping("/sensors")
@RestController
public class SensorDataController {

	/**
	 * Get the current reading of the named sensor
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{id}/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public double getSensorReading(@PathVariable("id") String id) throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		if (!SensorHelper.getSensorNames().contains(id)) {
			throw new IOException("Sensor with id " + id + " is not known. Possible values: " + names);
		}

		return SensorHelper.getTemperatureForSensor(id);
	}

	/**
	 * Get the available sensor IDs
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/ids/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public List<String> getSensorNames() throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		return names;
	}

	/**
	 * List all of the sensors data in Map. Expected to output as a series of key value pairs once converted with
	 * Jackson.
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/all/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Map<String, Double> getAllSensorValues() throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		Map<String, Double> values = new TreeMap<String, Double>();

		for (String name : names) {
			double temp = SensorHelper.getTemperatureForSensor(name);

			values.put(name, temp);
		}

		return values;
	}

}
