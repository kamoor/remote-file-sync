package com.rfsync.core.thread;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * This thread will keep an eye on file system changes and initiate a sync with
 * all slaves
 * 
 * @author MIBRLK0
 * 
 */
public class MasterFileWatchThread implements Runnable {

	String path = null;

	private MasterFileWatchThread(String p) {
		this.path = p;
	}

	/**
	 * Start a new thread
	 */
	public static void startThread(String path) {

		new Thread(new MasterFileWatchThread(path)).start();
	}

	@Override
	public void run() {

		try {

			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = new File(path).toPath();

			dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			System.out.println("Started polling " + path);
			for (;;) {

				// wait for key to be signaled
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					// The filename is the
					// context of the event.
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();
					MasterFileTransmitterThread.start(kind.name(), filename
							.getFileName().toString(), path);
				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}
}
