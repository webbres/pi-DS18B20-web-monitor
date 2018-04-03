package com.sjwebb.pi.temp.threads;

import com.sjwebb.pi.temp.database.SensorDataDatabase;

/**
 * Handle graceful shutdown of the application
 * @author Sam
 *
 */
public 	class ShutdownThread extends Thread
{
	private SensorDataDatabase database;
	
	public ShutdownThread(SensorDataDatabase database)
	{
		this.database = database;
	}
	@Override
	public void run() {
		database.stopServer();
	}
}
