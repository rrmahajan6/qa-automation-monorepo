package com.framework.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Fluent response helper — wraps REST Assured Response to provide
 * type-safe deserialization and common assertion shortcuts.
 *
 * Usage:
 * <pre>
 *   ResponseHelper res = new ResponseHelper(response);
 *   UserResponse user = res.as(UserResponse.class);
 *   res.assertStatusCode(200).assertFieldEquals("name", "John");
 * </pre>
 */
public class ResponseHelper {

    private static final Logger log = LogManager.getLogger(ResponseHelper.class);
    private final Response response;

    public ResponseHelper(Response response) {
        this.response = response;
    }

    // ─── Deserialization ──────────────────────────────────────────────────

    public <T> T as(Class<T> clazz) {
        return response.as(clazz);
    }

    public <T> List<T> asList(Class<T> clazz) {
        return response.jsonPath().getList("", clazz);
    }

    // ─── Field extraction ─────────────────────────────────────────────────

    public <T> T extract(String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }

    public String extractString(String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    public int extractInt(String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    public String getHeader(String name) {
        return response.getHeader(name);
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public long getResponseTime() {
        return response.getTime();
    }

    public String getBody() {
        return response.getBody().asString();
    }

    // ─── Fluent assertions ────────────────────────────────────────────────

    public ResponseHelper assertStatusCode(int expected) {
        int actual = response.getStatusCode();
        if (actual != expected) {
            throw new AssertionError(
                "Expected status code " + expected + " but got " + actual
                + "\nBody: " + response.getBody().asString());
        }
        log.debug("Status code assertion passed: {}", expected);
        return this;
    }

    public ResponseHelper assertFieldEquals(String jsonPath, Object expected) {
        Object actual = response.jsonPath().get(jsonPath);
        if (!expected.equals(actual)) {
            throw new AssertionError(
                "Expected '" + jsonPath + "' = '" + expected + "' but was '" + actual + "'");
        }
        return this;
    }

    public ResponseHelper assertFieldNotNull(String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        if (value == null) {
            throw new AssertionError("Expected '" + jsonPath + "' to be non-null");
        }
        return this;
    }

    public ResponseHelper assertResponseTimeLessThan(long maxMillis) {
        long actual = response.getTime();
        if (actual > maxMillis) {
            throw new AssertionError(
                "Expected response time < " + maxMillis + "ms but was " + actual + "ms");
        }
        log.debug("Response time {} ms (threshold {} ms)", actual, maxMillis);
        return this;
    }

    public ResponseHelper assertHeaderPresent(String headerName) {
        if (response.getHeader(headerName) == null) {
            throw new AssertionError("Expected header '" + headerName + "' to be present");
        }
        return this;
    }

    public ResponseHelper assertContentType(String expectedContentType) {
        String actual = response.getContentType();
        if (actual == null || !actual.contains(expectedContentType)) {
            throw new AssertionError(
                "Expected Content-Type to contain '" + expectedContentType + "' but was '" + actual + "'");
        }
        return this;
    }

    /** Delegates to underlying REST Assured response for full API access. */
    public Response raw() {
        return response;
    }
}
