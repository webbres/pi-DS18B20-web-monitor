package com.sjwebb.pi.temp.sensor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Support for interacting with DS18B20 sensors connected to a Raspberry Pi.
 * 
 * @author Sam
 *
 */
public class SensorHelper {
	
	/** The base directory for the device data */
	public static final String BASE_DIR = "/sys/bus/w1/devices/";
	
	/**	The file the sensor data is stored in */
	public static final String SENSOR_FILE = "/w1_slave";
	
	/** DS18B20 sensor folders start with 28 followed by the unique ID */
	public static final String DEVICE_INITIAL_IDENTIFIER = "28";
	
	/** How frequently to log the temperature */
	public static final int LOG_INTERVAL = 5;
	
	/**
	 * Get the available sensor names
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<String> getSensorNames() throws IOException {
		
		File deviceFolder = new File(BASE_DIR);
		
		if(!deviceFolder.exists())
		{
			throw new IOException("The base directory does not exist");
		}
		
		List<String> names = new ArrayList<String>();
		
		for(File file : deviceFolder.listFiles())
		{
			if(file.isDirectory() && file.getName().startsWith(DEVICE_INITIAL_IDENTIFIER))
			{
				names.add(file.getName());
			}
		}
		
		return names;
	}
	
	/**
	 * Read the temperature value for the named sensor
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static double readTemperatureForSensor(String name) throws IOException
	{	
		File deviceFile = new File(BASE_DIR + name + SENSOR_FILE);
		
		return readTemp(deviceFile);
	}

	/**
	 * Read the temperature value from the w1_slave sensor file
	 * @param deviceFile
	 * @return
	 * @throws IOException
	 */
	private static double readTemp(File deviceFile) throws IOException 
	{
		List<String> list = Files.readAllLines(deviceFile.toPath(), StandardCharsets.UTF_8);
		
		return parseTempFromLine(list.get(1));
	}

	/**
	 * Extract the temperature value
	 * 
	 * @param string  the 2nd line from the w1_slave sensor file
	 * @return
	 */
	private static double parseTempFromLine(String string) {

		String temp = string.split("t=")[1];
		
		return Double.valueOf(temp) / 1000d;
	}

	/**
	 * Get the current temperature reading for all the named sensors
	 * @param sensorNames
	 * @return
	 * @throws IOException
	 */
	public static List<Double> getTemperaturesForSensors(List<String> sensorNames) throws IOException {
		List<Double> temps = new ArrayList<Double>();
		
		for(String name : sensorNames)
		{
			temps.add(readTemperatureForSensor(name));
		}
		
		return temps;
	}
	

}
