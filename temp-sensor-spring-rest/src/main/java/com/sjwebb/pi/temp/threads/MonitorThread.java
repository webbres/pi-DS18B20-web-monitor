package com.sjwebb.pi.temp.threads;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.sjwebb.pi.temp.database.SensorDataDatabase;
import com.sjwebb.pi.temp.sensor.SensorHelper;

/**
 * Regularly poll the sensor files to get the temperature data
 * 
 * @author Sam Webb
 *
 */
public class MonitorThread extends Thread {

	private List<String> sensorNames;
	private SensorDataDatabase database;
	
	public MonitorThread(List<String> sensorNames, SensorDataDatabase database) {
		this.sensorNames = sensorNames;
		this.database = database;
	}

	@Override
	public void run() {
		
		System.out.println("Starting sensor monitor thread");
		while (true) {

			for (String sensor : sensorNames) {
				try {

					double temp = SensorHelper.getTemperatureForSensor(sensor);
					database.logResult(sensor, temp);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

//			try {
//				printTemperatures(sensorNames);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}

			// Now wait for the interval period to then record the next temperatures
			try {
				Thread.sleep(1000 * SensorHelper.LOG_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Print the temperature readings of the named sensors to the console
	 * @param sensorNames
	 * @throws IOException
	 */
	private static void printTemperatures(List<String> sensorNames) throws IOException {
		List<Double> temps = SensorHelper.getTemperaturesForSensors(sensorNames);
		
		Date date = new java.util.Date();
	
	    StringBuilder string = new StringBuilder(140);   
	    string
	        .append('\r')
	        .append(date.toString())
	        .append(" : ")
	        .append(temps);


	    System.out.print(string);
	}
}
