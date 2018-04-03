package com.sjwebb.pi.temp.threads;

import com.sjwebb.pi.temp.database.SensorDatabaseManager;

/**
 * Handle graceful shutdown of the application
 * @author Sam
 *
 */
public 	class ShutdownThread extends Thread
{
	private SensorDatabaseManager database;
	
	public ShutdownThread(SensorDatabaseManager database)
	{
		this.database = database;
	}
	@Override
	public void run() {
		database.stopServer();
	}
}
