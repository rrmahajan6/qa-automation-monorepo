package com.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility for JSON serialization / deserialization using Jackson.
 */
public final class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialise object to JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialise JSON to " + clazz.getSimpleName(), e);
        }
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json,
                MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialise JSON array", e);
        }
    }

    /**
     * Load a JSON file from the classpath (e.g. testdata/users.json).
     */
    public static <T> T fromClasspath(String classpathPath, Class<T> clazz) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (is == null) throw new IllegalArgumentException("Resource not found: " + classpathPath);
            return MAPPER.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON from classpath: " + classpathPath, e);
        }
    }

    public static <T> T fromClasspath(String classpathPath, TypeReference<T> typeRef) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (is == null) throw new IllegalArgumentException("Resource not found: " + classpathPath);
            return MAPPER.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON from classpath: " + classpathPath, e);
        }
    }

    public static Map<String, Object> toMap(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to map", e);
        }
    }

    public static String prettyPrint(String json) {
        try {
            Object obj = MAPPER.readValue(json, Object.class);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            return json;
        }
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
