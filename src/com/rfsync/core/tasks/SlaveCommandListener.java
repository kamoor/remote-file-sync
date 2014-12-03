package com.rfsync.core.tasks;

//import com.rfsync.core.thread.MasterSocketThread;

public class SlaveCommandListener implements FileTask {

	@Override
	public void execute(String port) throws Exception {
		// MasterSocketThread.startThread(Integer.parseInt(port));
		System.out.println("Slave thread initialized.");
		System.out.println("Listening for command from master.");
	}

}
