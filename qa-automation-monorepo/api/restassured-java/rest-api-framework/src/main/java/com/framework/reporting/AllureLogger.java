package com.framework.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Helper methods for attaching structured data to the Allure report.
 */
public final class AllureLogger {

    private static final Logger log = LogManager.getLogger(AllureLogger.class);

    private AllureLogger() {}

    public static void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json",
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), ".json");
        log.debug("Attached JSON to Allure report: {}", name);
    }

    public static void attachText(String name, String text) {
        Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    public static void step(String description) {
        Allure.step(description);
        log.info("Step: {}", description);
    }

    public static void addParameter(String name, Object value) {
        Allure.parameter(name, value);
    }
}
