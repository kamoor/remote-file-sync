package com.rfsync.core.tasks;

import java.util.Properties;

import com.rfsync.core.constants.CoreConstants;

/**
 * Pre initialize tasks goes here Min Software hardware check etc
 * 
 * @author MIBRLK0
 * 
 */
public class PreInitializer implements SimpleTask {

	@Override
	public void execute() throws Exception {
		checkJavaVersion();
	}

	/**
	 * Check java version
	 * 
	 * @throws Exception
	 */
	private void checkJavaVersion() throws Exception {
		Properties properties = System.getProperties();
		String sVersion = properties.getProperty("java.version")
				.substring(0, 3);
		Float f = Float.valueOf(sVersion);
		if (f.floatValue() < (float) CoreConstants.MIN_JAVA_VERSION) {
			throw new Exception(
					"Please upgrade your JRE. Minimum Required version : "
							+ CoreConstants.MIN_JAVA_VERSION);
		}
		System.out.println("JRE Version[" + sVersion + "] check successful.");
	}

}
