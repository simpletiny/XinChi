package generator.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static Properties config;
	
	public static String getProperty(String key){
		config = new Properties();
		try {
			InputStream in = PropertiesUtils.class.getResourceAsStream("jdbc.properties");
			config.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config.getProperty(key);
	}

	
}
