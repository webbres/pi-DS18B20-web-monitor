package com.sjwebb.pi.temp.threads;

import com.sjwebb.pi.temp.database.Database;

/**
 * Handle graceful shutdown of the application
 * @author Sam
 *
 */
public 	class ShutdownThread extends Thread
{
	Database database;
	public ShutdownThread(Database database)
	{
		this.database = database;
	}
	@Override
	public void run() {
		System.out.println();
		System.out.println("Shutting down");
		database.stopServer();
		System.out.println();
	}
}
