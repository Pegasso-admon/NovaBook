package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load and access configuration properties from config.properties file.
 * Properties are loaded once at class initialization.
 */
public class ConfigLoader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("CRITICAL ERROR: config.properties not found in resources folder.");
            }
            PROPERTIES.load(input);
            System.out.println("[ConfigLoader] Configuration loaded successfully.");
        } catch (IOException ex) {
            throw new RuntimeException("CRITICAL ERROR: Failed to load config.properties - " + ex.getMessage(), ex);
        }
    }

    // Prevents instantiation
    private ConfigLoader() {}

    /**
     * Retrieves a configuration property by key.
     * @param key The property key
     * @return The property value, or null if not found
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Retrieves a configuration property with a default value.
     * @param key The property key
     * @param defaultValue The default value if key is not found
     * @return The property value, or defaultValue if not found
     */
    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    /**
     * Retrieves an integer configuration property.
     * @param key The property key
     * @return The property value as integer
     * @throws NumberFormatException if value cannot be parsed
     */
    public static int getIntProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in config.properties");
        }
        return Integer.parseInt(value);
    }

    /**
     * Retrieves an integer configuration property with a default value.
     * @param key The property key
     * @param defaultValue The default value if key is not found
     * @return The property value as integer, or defaultValue if not found or invalid
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = PROPERTIES.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Checks if a property exists.
     * @param key The property key
     * @return true if the property exists, false otherwise
     */
    public static boolean hasProperty(String key) {
        return PROPERTIES.containsKey(key);
    }
}