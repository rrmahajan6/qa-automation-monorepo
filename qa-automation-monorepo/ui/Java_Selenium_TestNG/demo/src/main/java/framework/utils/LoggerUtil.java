package framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger Utility Class
 * Provides centralized logging for the framework
 */
public class LoggerUtil {

    /**
     * Get logger instance for a specific class
     * @param clazz the class for which logger is needed
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Log info message
     * @param clazz the calling class
     * @param message the message to log
     */
    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    /**
     * Log debug message
     * @param clazz the calling class
     * @param message the message to log
     */
    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    /**
     * Log warning message
     * @param clazz the calling class
     * @param message the message to log
     */
    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    /**
     * Log error message
     * @param clazz the calling class
     * @param message the message to log
     * @param throwable the throwable/exception
     */
    public static void error(Class<?> clazz, String message, Throwable throwable) {
        getLogger(clazz).error(message, throwable);
    }

    /**
     * Log error message
     * @param clazz the calling class
     * @param message the message to log
     */
    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }
}
