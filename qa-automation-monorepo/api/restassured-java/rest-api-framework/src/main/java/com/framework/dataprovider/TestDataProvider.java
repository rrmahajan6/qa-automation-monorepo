package com.framework.dataprovider;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.framework.utils.JsonUtils;

/**
 * Centralized TestNG DataProviders for data-driven testing.
 *
 * <p>Loads test data from JSON files under {@code testdata/} classpath folder.
 * Each DataProvider can run in parallel via {@code parallel = true}.
 */
public class TestDataProvider {

    private static final Logger log = LogManager.getLogger(TestDataProvider.class);

    /**
     * Place API test data — multiple sets of place data.
     */
    @DataProvider(name = "placeData", parallel = true)
    public static Object[][] placeData() {
        List<Map<String, Object>> data = JsonUtils.fromClasspath(
                "testdata/places.json", new TypeReference<>() {});
        return toDataProviderArray(data);
    }

    /**
     * Shopping flow test data — user + product combos.
     */
    @DataProvider(name = "shoppingData", parallel = true)
    public static Object[][] shoppingData() {
        List<Map<String, Object>> data = JsonUtils.fromClasspath(
                "testdata/shopping.json", new TypeReference<>() {});
        return toDataProviderArray(data);
    }

    /**
     * Generic loader — reads any JSON array file and returns as DataProvider.
     */
    public static Object[][] fromJsonFile(String classpathFile) {
        List<Map<String, Object>> data = JsonUtils.fromClasspath(
                classpathFile, new TypeReference<>() {});
        return toDataProviderArray(data);
    }

    private static Object[][] toDataProviderArray(List<Map<String, Object>> data) {
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        log.debug("Loaded {} test data rows", data.size());
        return result;
    }
}
