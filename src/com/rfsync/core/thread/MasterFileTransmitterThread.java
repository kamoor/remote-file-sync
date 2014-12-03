package com.rfsync.core.thread;

import java.io.File;
import com.rfsync.core.constants.CoreConstants;
import com.rfsync.core.constants.FileEvent;
import com.rfsync.core.network.FileSenderForMaster;

/**
 * This is a thread to send a file to slave
 * 
 * @author MIBRLK0
 * 
 */
public class MasterFileTransmitterThread implements Runnable {

	String action = null;
	String file;
	String rootPath;

	public static void start(String action, String file, String rootPath) {
		new Thread(new MasterFileTransmitterThread(action, file, rootPath))
				.start();
	}

	public MasterFileTransmitterThread(String action, String file,
			String rootPath) {
		this.action = action;
		this.file = file;
		this.rootPath = rootPath;
	}

	@Override
	public void run() {
		try {
			System.out.println(String.format("Start Processing: %s %s", action,
					file));
			this.processFile(action, file, rootPath);
			// reindex it
			System.out.println("Reindex FileSystem");
			CoreConstants.setFileIndex(rootPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void processFile(String action, String file, String rootPath) {
		if (FileEvent.ENTRY_DELETE.name().equals(action)) {
			this.delete(file);
		} else if (FileEvent.ENTRY_CREATE.name().equals(action)) {
			this.create(file);
		} else if (FileEvent.ENTRY_MODIFY.name().equals(action)) {
			this.update(file);
		}

	}

	private void delete(String file) {
		FileSenderForMaster.delete(rootPath, file);
	}

	/**
	 * Create file
	 * 
	 * @param file
	 */
	private void create(String file) {
		if (new File(rootPath + file).isDirectory()) {
			FileSenderForMaster.sendDir(rootPath, file, "MKDIR");
		} else {
			FileSenderForMaster.sendFile(rootPath, file, "MKFILE");
		}

	}

	private void update(String file) {

		if (new File(rootPath + file).isDirectory()) {
			FileSenderForMaster.sendDirRecursive(rootPath, file, "MKDIR");
		} else {
			FileSenderForMaster.sendFile(rootPath, file, "MDFILE");

		}
	}

}
