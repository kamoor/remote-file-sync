package com.rfsync.core.network;

import java.net.*;
import java.io.*;

/**
 * This will run when u start slave, It will contact master and register itself
 * as a slave Master will send files to each slaves
 * 
 * @author MIBRLK0
 * 
 */
public class SlaveRegister {

	/**
	 * Contact Master and Register Client
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public static void handshake(String masterURL, int myPort)
			throws IOException {

		Socket sock = new Socket(masterURL.split("[:]")[0],
				Integer.parseInt(masterURL.split("[:]")[1]));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				sock.getOutputStream()));
		// os.write(mybytearray, 0, mybytearray.length);
		writer.write("REGCLIENT " + myPort);
		writer.flush();

		sock.close();
	}
}