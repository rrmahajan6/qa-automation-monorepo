package framework.utils.db;

import framework.config.GlobalConfig;
import framework.utils.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager
 * Centralized class for creating MySQL database connections.
 */
public final class DatabaseManager {

    private static final Class<?> logger = DatabaseManager.class;

    private DatabaseManager() {
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("MySQL JDBC driver not found in classpath", e);
        }
    }

    /**
     * Creates a new database connection from framework configuration.
     * @return active JDBC connection
     */
    public static Connection getConnection() {
        validateDbConfiguration();

        try {
            DriverManager.setLoginTimeout(GlobalConfig.getDbConnectionTimeoutSeconds());
            Connection connection = DriverManager.getConnection(
                    GlobalConfig.getDbUrl(),
                    GlobalConfig.getDbUsername(),
                    GlobalConfig.getDbPassword()
            );
            LoggerUtil.debug(logger, "Database connection created successfully");
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to create DB connection. Check db.url/db.username/db.password", e);
        }
    }

    /**
     * Checks whether DB connectivity is available using current configuration.
     * @return true if connection can be established
     */
    public static boolean isReachable() {
        try (Connection connection = getConnection()) {
            return connection.isValid(GlobalConfig.getDbConnectionTimeoutSeconds());
        } catch (Exception e) {
            LoggerUtil.warn(logger, "Database connectivity check failed: " + e.getMessage());
            return false;
        }
    }

    private static void validateDbConfiguration() {
        if (!GlobalConfig.isDbEnabled()) {
            throw new DatabaseException("Database integration is disabled. Set db.enabled=true in application.properties");
        }

        String dbUrl = GlobalConfig.getDbUrl();
        if (dbUrl == null || dbUrl.isBlank()) {
            throw new DatabaseException("Database URL is missing. Configure db.url in application.properties");
        }
    }
}
