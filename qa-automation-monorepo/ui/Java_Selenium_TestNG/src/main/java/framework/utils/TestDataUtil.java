package framework.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
}
