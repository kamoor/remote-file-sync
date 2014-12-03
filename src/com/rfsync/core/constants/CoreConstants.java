package com.rfsync.core.constants;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.rfsync.core.util.FileUtil;

/**
 * All constants goes here
 * 
 * @author MIBRLK0
 * 
 */
public class CoreConstants {

	public final static double MIN_JAVA_VERSION = 1.7D;
	public final static int MIN_PORT = 1024;

	// File Index
	private static Map<String, Long> fileIndex = null;

	private static Map<String, String> clients = new HashMap<String, String>();

	public static Map<String, String> getClients() {
		return clients;
	}

	public static void addClient(String ip, String port) {
		clients.put(ip, port);
	}

	public static Map<String, Long> getFileIndex() {
		return fileIndex;
	}

	public static void setFileIndex(Map<String, Long> fileIndex) {
		fileIndex = fileIndex;
	}

	public static void setFileIndex(String path) throws FileNotFoundException {
		fileIndex = FileUtil.generateFileIndexMap(path);
	}

}
