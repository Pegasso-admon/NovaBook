package util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties PROPERTIES = new Properties();

    static {
        // Loads config.properties once
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Error: config.properties not found.");
            }
            PROPERTIES.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}