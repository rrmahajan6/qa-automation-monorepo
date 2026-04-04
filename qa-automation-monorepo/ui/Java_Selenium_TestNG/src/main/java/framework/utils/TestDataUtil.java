package framework.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test Data Utility Class
 * Handles loading test data from JSON files
 * Useful for data-driven testing
 */
public class TestDataUtil {

    private static final Gson gson = new Gson();

    /**
     * Load JSON test data from file
     * @param filePath path to JSON file
     * @return JsonObject containing the data
     */
    public static JsonObject loadTestData(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            LoggerUtil.error(TestDataUtil.class, "Failed to load test data from: " + filePath, e);
            throw new RuntimeException("Test data file not found: " + filePath, e);
        }
    }

    /**
     * Load JSON object from classpath resources.
     */
    public static JsonObject loadJsonObjectFromClasspath(String resourcePath) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
        } catch (IOException e) {
            LoggerUtil.error(TestDataUtil.class, "Failed to load JSON object from classpath: " + resourcePath, e);
            throw new RuntimeException("Test data resource not found: " + resourcePath, e);
        }
    }

    /**
     * Load JSON array from classpath resources.
     */
    public static JsonArray loadJsonArrayFromClasspath(String resourcePath) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonArray.class);
        } catch (IOException e) {
            LoggerUtil.error(TestDataUtil.class, "Failed to load JSON array from classpath: " + resourcePath, e);
            throw new RuntimeException("Test data resource not found: " + resourcePath, e);
        }
    }

    /**
     * Get value from JSON object by key
     * @param jsonObject JSON object
     * @param key key to retrieve
     * @return value as string
     */
    public static String getValue(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsString();
        }
        LoggerUtil.warn(TestDataUtil.class, "Key not found in JSON: " + key);
        return null;
    }

    /**
     * Get value from JSON object with default
     * @param jsonObject JSON object
     * @param key key to retrieve
     * @param defaultValue default value if key not found
     * @return value or default
     */
    public static String getValue(JsonObject jsonObject, String key, String defaultValue) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsString();
        }
        return defaultValue;
    }

    /**
     * Convert JSON object to Map
     * @param jsonObject JSON object
     * @return Map with key-value pairs
     */
    public static Map<String, String> jsonToMap(JsonObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        jsonObject.keySet().forEach(key -> 
            map.put(key, jsonObject.get(key).getAsString())
        );
        return map;
    }

    /**
     * Convert a JSON array of objects into TestNG Object[][].
     */
    public static Object[][] jsonArrayToDataProvider(JsonArray dataArray) {
        List<Object[]> rows = new ArrayList<>();
        for (JsonElement element : dataArray) {
            rows.add(new Object[] { jsonToMap(element.getAsJsonObject()) });
        }
        return rows.toArray(new Object[0][]);
    }
}
