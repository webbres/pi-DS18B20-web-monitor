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

@RequestMapping("/sensors")
@RestController
public class SensorDataController {

	@RequestMapping(value = "/{id}/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public double getSensorReading(@PathVariable("id") String id) throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		if (!SensorHelper.getSensorNames().contains(id)) {
			throw new IOException("Sensor with id " + id + " is not known. Possible values: " + names);
		}

		return SensorHelper.readTemperatureForSensor(id);
	}

	@RequestMapping(value = "/ids/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public List<String> getSensorNames() throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		return names;
	}

	@RequestMapping(value = "/all/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Map<String, Double> getAllSensorValues() throws IOException {

		List<String> names = SensorHelper.getSensorNames();

		Map<String, Double> values = new TreeMap<String, Double>();

		for (String name : names) {
			double temp = SensorHelper.readTemperatureForSensor(name);

			values.put(name, temp);
		}

		return values;
	}

}
