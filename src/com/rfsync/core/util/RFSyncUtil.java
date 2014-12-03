package com.rfsync.core.util;

public class RFSyncUtil {

	public static boolean isMaster(String type) {
		return "master".equals(type);
	}

	public static boolean isSlave(String type) {
		return "slave".equals(type);
	}
}
