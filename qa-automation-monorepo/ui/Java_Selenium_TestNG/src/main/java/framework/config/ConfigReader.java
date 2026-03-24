package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader - Loads properties from config files
 * Supports:
 * - application.properties for global settings
 * - environment-specific properties (dev, staging, prod)
 */
public class ConfigReader {

    private static Properties properties;

    /**
     * Initialize and load properties from the config file
     */
    public static void loadProperties(String filePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    /**
     * Get property value by key
     * @param key property key
     * @return property value
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadDefaultProperties();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    /**
     * Get property value with default fallback
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadDefaultProperties();
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Load default properties from application.properties
     */
    private static void loadDefaultProperties() {
        properties = new Properties();
        try {
            String configFilePath = "src/test/resources/application.properties";
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default properties file: " + e.getMessage(), e);
        }
    }

    /**
     * Check if property exists
     * @param key property key
     * @return true if property exists
     */
    public static boolean isPropertyExists(String key) {
        if (properties == null) {
            loadDefaultProperties();
        }
        return properties.containsKey(key);
    }
}
