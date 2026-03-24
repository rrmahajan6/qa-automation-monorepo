package com.framework.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Utility for validating API responses against JSON schemas.
 *
 * Schemas live under src/test/resources/schemas/ and are loaded from classpath.
 *
 * Usage:
 * <pre>
 *   SchemaValidator.validate(response, "user-schema.json");
 * </pre>
 */
public final class SchemaValidator {

    private static final Logger log = LogManager.getLogger(SchemaValidator.class);
    private static final String SCHEMA_DIR = "schemas/";

    private SchemaValidator() {}

    /**
     * Validates the response body against a JSON schema file on the classpath.
     *
     * @param response   REST Assured response
     * @param schemaFile filename (e.g. "user-schema.json"), relative to schemas/
     */
    public static void validate(Response response, String schemaFile) {
        String path = SCHEMA_DIR + schemaFile;
        log.debug("Validating response against schema: {}", path);
        assertThat(response.getBody().asString(),
                JsonSchemaValidator.matchesJsonSchemaInClasspath(path));
        log.debug("Schema validation passed: {}", schemaFile);
    }

    /**
     * Validates raw JSON string against schema.
     */
    public static void validate(String json, String schemaFile) {
        String path = SCHEMA_DIR + schemaFile;
        log.debug("Validating JSON against schema: {}", path);
        assertThat(json, JsonSchemaValidator.matchesJsonSchemaInClasspath(path));
        log.debug("Schema validation passed: {}", schemaFile);
    }
}
