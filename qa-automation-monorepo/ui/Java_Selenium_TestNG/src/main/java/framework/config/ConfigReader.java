package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
        try (InputStream inputStream = openInputStream(filePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    /**
     * Load base and environment specific properties.
     * Example: application.properties + application-qa.properties
     */
    public static void loadEnvironmentProperties(String env) {
        properties = new Properties();
        loadInto(properties, "application.properties");
        loadInto(properties, String.format("application-%s.properties", env));
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
        loadEnvironmentProperties("qa");
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

    private static void loadInto(Properties target, String filePath) {
        try (InputStream inputStream = openInputStream(filePath)) {
            Properties temp = new Properties();
            temp.load(inputStream);
            target.putAll(temp);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    private static InputStream openInputStream(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (Files.exists(path)) {
            return new FileInputStream(filePath);
        }

        InputStream classpathStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (classpathStream != null) {
            return classpathStream;
        }

        throw new IOException("Properties file not found at path or classpath: " + filePath);
    }
}
