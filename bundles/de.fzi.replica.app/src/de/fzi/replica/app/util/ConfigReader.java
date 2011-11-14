package de.fzi.replica.app.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.10.2011
 *
 */
public class ConfigReader {
	
	// public final static String DEFAULT_PROPERTIES_LOCATION = "replica.properties";
	
	// new FileInputStream("replica.properties")
	
	public static Properties read(InputStream is) throws IOException {
		Properties properties = new Properties();
		BufferedInputStream stream = new BufferedInputStream(is);
		properties.load(stream);
		stream.close();
//		String lang = properties.getProperty("lang");
//		System.out.println("reading properties file with language: "+lang);
		return properties;
	}
	
}
