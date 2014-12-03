package com.rfsync.core.tasks;

import com.rfsync.core.thread.MasterFileWatchThread;

public class FileSystemWatcher implements FileTask {

	@Override
	public void execute(String path) throws Exception {
		MasterFileWatchThread.startThread(path);
		System.out.println("Master thread initialized.");
	}

}
