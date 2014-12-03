package com.rfsync.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

	/**
	 * Get List of file and last modified date
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Map<String, Long> generateFileIndexMap(String filePath)
			throws FileNotFoundException {
		System.out.println("Start Indexing " + filePath);
		Map<String, Long> list = new HashMap<String, Long>();
		File parentFile = new File(filePath);
		if (parentFile.exists() && parentFile.isDirectory()) {

			generateRecursiveFileList(parentFile, list);
		} else {
			throw new FileNotFoundException(
					"Path does not exists or not a valid directory ");
		}
		return list;
	}

	// File all files in a folder (recursive) and
	private static void generateRecursiveFileList(File f, Map<String, Long> list) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File subDirOrFile : files) {
				generateRecursiveFileList(subDirOrFile, list);
			}

		} else {
			System.out.println("......Indexing " + f.getAbsolutePath());
			list.put(f.getAbsolutePath(), f.lastModified());
		}
	}

}
