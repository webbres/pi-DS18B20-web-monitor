package com.sjwebb.pi.temp;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.sjwebb.pi.temp.database.SensorDataDatabase;
import com.sjwebb.pi.temp.sensor.SensorHelper;
import com.sjwebb.pi.temp.threads.MonitorThread;
import com.sjwebb.pi.temp.threads.ShutdownThread;

/**
 * Spring boot application entry point. Starts up the underlying application (database) and sets of the 
 * sensor monitoring. 
 * @author samwe
 *
 */
@EnableWebMvc
@SpringBootApplication
public class Application {
	

	
	final static Logger logger = Logger.getLogger("Pi-Sensor");

	public static void main(String[] args) {

		start();
		SpringApplication.run(Application.class, args);
	}

	
	/**
	 * Start the H2 database and trigger the monitoring
	 */
	public static void start()
	{
		// Get the initial sensor list
		List<String> sensorNames = null;

		try {
			sensorNames = SensorHelper.getSensorNames();

			if (sensorNames.size() > 0) {
				System.out.println("Sensors found: " + sensorNames);
			} else {
				System.out.println("No sensors found");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Start the database
		SensorDataDatabase database = null;
		try 
		{
			database = new SensorDataDatabase();
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("Error with database");
			System.exit(1);
		}
		
		// TODO: does this still work after wrapping in Spring Boot?
		createShutdownHook(database);
		
		System.out.println("Shutdown with Ctrl + C");
		
		MonitorThread thread = new MonitorThread(sensorNames, database);
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(thread);
	}
	
	/**
	 * Create a shutdown hook to enable graceful termination of the application
	 * @param database
	 */
	private static void createShutdownHook(SensorDataDatabase database) {
		
		ShutdownThread thread = new ShutdownThread(database);
		Runtime.getRuntime().addShutdownHook(thread);
	}
	


}
