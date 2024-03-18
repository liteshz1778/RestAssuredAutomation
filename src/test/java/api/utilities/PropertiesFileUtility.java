package api.utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesFileUtility {

	public static String value;

	public static String readProperty(String name) {
		try {
			Properties prop = new Properties();

			FileInputStream fi = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\routes.properties");
			prop.load(fi);

			value = prop.getProperty(name);

		} catch (Exception e) {
			System.out.println("Properties Utility Exceptions are:\n" + e);
		}

		return value;
	}

}
