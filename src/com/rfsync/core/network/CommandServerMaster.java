package com.rfsync.core.network;

import java.net.*;

import java.io.*;

import com.rfsync.core.constants.CoreConstants;

/**
 * This is running at master mode for any command coming from each slaves For
 * example register client
 * 
 * @author MIBRLK0
 * 
 */
public class CommandServerMaster implements Runnable {

	static boolean isStarted = false;
	int port = 0;

	private CommandServerMaster(int port) {
		this.port = port;
	}

	/**
	 * Start a new thread
	 */
	public static void startThread(int port) {
		if (!isStarted) {
			new Thread(new CommandServerMaster(port)).start();
		}
	}

	@Override
	public void run() {

		try {
			System.out.println("Command Server Starting at port  " + port);
			ServerSocket serverSocket = new ServerSocket(port);
			isStarted = true;
			while (true) {

				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				String clientIP = clientSocket.getInetAddress()
						.getHostAddress();
				System.out.println("Command from : " + clientIP);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				String command = null;

				while ((command = reader.readLine()) != null) {
					if (command.startsWith("REGCLIENT")) {
						String port = command.split(" ")[1];
						System.out.println("Got a new client " + clientIP + ":"
								+ port);
						CoreConstants.addClient(clientIP, port);
					} else {
						System.out.println("Unknown Command " + command);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to start command thread");
		}

	}
}