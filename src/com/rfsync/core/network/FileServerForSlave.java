package com.rfsync.core.network;

import java.net.*;
import java.io.*;

/**
 * This is a simple server thread for accepting files from master
 * 
 * @author MIBRLK0
 * 
 */
public class FileServerForSlave implements Runnable {

	static boolean isStarted = false;

	int port;
	String dir;

	private FileServerForSlave(int port, String dir) {
		this.port = port;
		this.dir = dir;
	}

	/**
	 * Start a new thread
	 */
	public static void startThread(int port, String dir) {
		if (!isStarted) {
			new Thread(new FileServerForSlave(port, dir)).start();

		}
	}

	public boolean receiveFile(Socket clientSocket) {

		try {

			InputStream in = clientSocket.getInputStream();
			int bytesRead = -1;
			byte[] buffer = new byte[1024];
			boolean firstLine = false;
			FileOutputStream output = null;
			while ((bytesRead = in.read(buffer)) != -1) {
				if (firstLine == false) {
					String line = new String(buffer);
					System.out.println("Command: " + line);
					firstLine = true;
					String fileName = line.substring(6, line.indexOf("~~"))
							.trim();
					String path = dir + fileName;
					System.out.println("Locale Path " + path);
					// create new file or modify existing
					if (line.startsWith("MKFILE") || line.startsWith("MDFILE")) {
						output = new FileOutputStream(path);
					} else if (line.startsWith("DL")) { // Delete file or dir
						// delete file or dir
						this.deleteFile(path);
					} else if (line.startsWith("MKDIR")) { // Create dir
						this.createDirectory(path);
					} else {
						System.out
								.println("Unknown Command, Update Slave software");
						break;
					}
					continue;

				}
				output.write(buffer, 0, bytesRead);
			}
			if (output != null)
				output.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 
	 * @param path
	 */
	private boolean createDirectory(String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		f.mkdir();

		return true;

	}

	private void deleteFile(String file) {
		try {
			new File(file).delete();
			System.out.println("File Deleted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {
			ServerSocket serverSocket = new ServerSocket(port);

			System.out.println("Listening for files...");
			while (true) {
				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				System.out.println("Got a connection from MASTER: "
						+ clientSocket.getInetAddress().getHostAddress());
				this.receiveFile(clientSocket);
				clientSocket.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Unable to listen for files coming from master .. restart master and slave");
		}
	}

}