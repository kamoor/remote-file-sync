package com.rfsync.core.network;

import java.net.*;
import java.util.Map.Entry;
import java.io.*;

import com.rfsync.core.constants.CoreConstants;

/**
 * File sender when some thing changes in master
 * 
 * @author MIBRLK0
 * 
 */
public class FileSenderForMaster {

	private static String SPACE = " ";

	/**
	 * Send new to all clients
	 * 
	 * @param fileAbsolutePath
	 * @return
	 */
	public static boolean sendFile(String path, String file, String action) {

		try {

			for (Entry<String, String> entry : CoreConstants.getClients()
					.entrySet()) {
				System.out.println("Trasmitting FILE [" + file + "] TO "
						+ entry.getKey() + ":" + entry.getValue());
				Socket sock = new Socket(entry.getKey(), Integer.parseInt(entry
						.getValue()));
				File f = new File(path + file);
				FileInputStream fileInputSteam = new FileInputStream(f);
				byte[] mybytearray = new byte[(int) f.length()];
				BufferedInputStream bis = new BufferedInputStream(
						fileInputSteam);
				bis.read(mybytearray, 0, mybytearray.length);

				OutputStream os = sock.getOutputStream();

				String command = action + SPACE + file + "~~";
				os.write(command.getBytes(), 0, command.length());
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				os.close();
				bis.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Send new to all clients
	 * 
	 * @param fileAbsolutePath
	 * @return
	 */
	public static boolean sendDir(String path, String dir, String action) {

		try {

			for (Entry<String, String> entry : CoreConstants.getClients()
					.entrySet()) {
				System.out.println("Trasmitting DIR [" + dir + "] TO "
						+ entry.getKey() + ":" + entry.getValue());
				Socket sock = new Socket(entry.getKey(), Integer.parseInt(entry
						.getValue()));
				File f = new File(path + dir);
				if (f.exists()) {
					String command = action + SPACE + dir + "~~";
					OutputStream os = sock.getOutputStream();
					os.write(command.getBytes(), 0, command.length());
					os.flush();
					os.close();
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Send new to all clients
	 * 
	 * @param fileAbsolutePath
	 * @return
	 */
	public static boolean sendDirRecursive(String root, String path,
			String action) {

		try {
			System.out.println("Recurse Send " + path);
			File fPath = new File(root + "/" + path);
			if (fPath.isDirectory()) {
				// Create Dir
				sendDir(root, path, "MKDIR");
				// Now check all childs
				File[] childs = fPath.listFiles();
				if (childs == null)
					return true;
				for (File child : childs) {
					if (child.isDirectory()) {
						sendDirRecursive(root, path + "/" + child.getName(),
								null);
					} else {
						sendFile(root, path + "/" + child.getName(), "MKFILE");
					}

				}
				// then call again
			} else {
				sendFile(fPath.getPath(), fPath.getName(), "MKFILE");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public static boolean delete(String path, String file) {

		try {

			for (Entry<String, String> entry : CoreConstants.getClients()
					.entrySet()) {
				System.out.println("Trasmitting FILE [" + file + "] TO "
						+ entry.getKey() + ":" + entry.getValue());
				Socket sock = new Socket(entry.getKey(), Integer.parseInt(entry
						.getValue()));
				File f = new File(path + "\\" + file);
				OutputStream os = sock.getOutputStream();
				String command = "DLFILE " + file + "~~";
				if (f.isDirectory()) {
					command = "DLDIR " + file + "~~";
				}
				os.write(command.getBytes(), 0, command.length());
				os.flush();
				os.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

}