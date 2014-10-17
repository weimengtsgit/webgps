package com.autonavi.util;

import java.util.Properties;

import com.autonavi.directl.Log;

public class PropertyReader {
	protected Properties properties = null;

	/** Creates a new instance of PropertyReader 
	 * @throws Exception */

	public PropertyReader(String propertyFile) throws Exception {
		loadPropertyFile(propertyFile);
	}

	// public String getProperty(String prop) { return properties.getProperty(prop); }
	private void loadPropertyFile(String propertyFile) throws Exception {
		// Get our class loader
		ClassLoader cl = getClass().getClassLoader();
		java.io.InputStream in;

		if (cl != null) {
			in = cl.getResourceAsStream(propertyFile);
		} else {
			in = ClassLoader.getSystemResourceAsStream(propertyFile);
		}
		if (in == null) {
			throw new Exception("Configuration file '" + propertyFile
					+ "' not found");
		} else {
			try {
				properties = new Properties();

				// Load the configuration file into the properties table
				properties.load(in);
			} finally {
				// Close the input stream
				if (in != null) {
					try {
						in.close();
					} catch (Exception ex) {
						Log.getInstance().outLog(
								"PropertyReader - Exception found. "
										+ ex.toString());
						throw ex;
					}
				}
			}
		}
	}

	public String getProperty(String propertyName) {
		String val = "";
		try {

			val = properties.getProperty(propertyName);
		} catch (Exception ex) {
			Log.getInstance().outLog(
					"getProperty() - error reading property - " + propertyName
							+ ".\n" + ex);
		}

		return val;
	}
}
