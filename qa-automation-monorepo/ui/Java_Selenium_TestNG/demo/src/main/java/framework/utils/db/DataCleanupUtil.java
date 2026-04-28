package framework.utils.db;

import framework.config.ConfigReader;
import framework.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DataCleanupUtil
 * Generic utility for cleaning test data from multiple database tables.
 * Supports configurable table cleanup order to handle foreign key constraints.
 */
public final class DataCleanupUtil {

    private static final Class<?> logger = DataCleanupUtil.class;

    private DataCleanupUtil() {
    }

    /**
     * Cleans all tables defined in application.properties (db.cleanup.tables).
     * Tables are cleaned in the order specified to respect foreign key constraints.
     * Format in properties: db.cleanup.tables=order_items,orders,users,products
     */
    public static void cleanAllConfiguredTables() {
        List<String> tables = getConfiguredTables();
        
        if (tables.isEmpty()) {
            LoggerUtil.warn(logger, "No tables configured for cleanup. Add 'db.cleanup.tables' in application.properties");
            return;
        }
        
        LoggerUtil.info(logger, "Starting database cleanup for " + tables.size() + " table(s)...");
        cleanTables(tables);
        LoggerUtil.info(logger, "Database cleanup completed successfully");
    }

    /**
     * Cleans specific list of tables in the given order.
     * @param tableNames list of table names to clean (in order)
     */
    public static void cleanTables(List<String> tableNames) {
        int totalDeleted = 0;
        
        for (String tableName : tableNames) {
            try {
                int deleted = cleanTable(tableName);
                totalDeleted += deleted;
                LoggerUtil.info(logger, "Cleaned table '" + tableName + "': " + deleted + " row(s) deleted");
            } catch (DatabaseException e) {
                LoggerUtil.warn(logger, "Failed to clean table '" + tableName + "': " + e.getMessage());
            }
        }
        
        LoggerUtil.info(logger, "Total rows deleted: " + totalDeleted);
    }

    /**
     * Cleans specific list of tables in the given order.
     * @param tableNames varargs of table names to clean
     */
    public static void cleanTables(String... tableNames) {
        cleanTables(Arrays.asList(tableNames));
    }

    /**
     * Cleans a single table by deleting all rows.
     * @param tableName name of the table to clean
     * @return number of rows deleted
     */
    public static int cleanTable(String tableName) {
        String sql = "DELETE FROM " + tableName;
        int deleted = DatabaseUtil.executeUpdate(sql);
        LoggerUtil.debug(logger, "Deleted " + deleted + " row(s) from table: " + tableName);
        return deleted;
    }

    /**
     * Cleans a table with a WHERE condition.
     * @param tableName name of the table
     * @param whereClause WHERE condition (without the WHERE keyword)
     * @param params parameters for the WHERE clause
     * @return number of rows deleted
     */
    public static int cleanTableWithCondition(String tableName, String whereClause, Object... params) {
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
        int deleted = DatabaseUtil.executeUpdate(sql, params);
        LoggerUtil.info(logger, "Deleted " + deleted + " row(s) from " + tableName + " with condition: " + whereClause);
        return deleted;
    }

    /**
     * Truncates a table (faster than DELETE but doesn't respect foreign keys).
     * Use with caution - only for tables without foreign key constraints.
     * @param tableName name of the table to truncate
     */
    public static void truncateTable(String tableName) {
        try {
            String sql = "TRUNCATE TABLE " + tableName;
            DatabaseUtil.executeUpdate(sql);
            LoggerUtil.info(logger, "Truncated table: " + tableName);
        } catch (DatabaseException e) {
            LoggerUtil.warn(logger, "Failed to truncate table '" + tableName + "'. Trying DELETE: " + e.getMessage());
            cleanTable(tableName);
        }
    }

    /**
     * Truncates multiple tables.
     * @param tableNames list of table names to truncate
     */
    public static void truncateTables(String... tableNames) {
        for (String tableName : tableNames) {
            truncateTable(tableName);
        }
    }

    /**
     * Gets the list of tables configured for cleanup from application.properties.
     * Property: db.cleanup.tables=table1,table2,table3
     * @return list of table names
     */
    private static List<String> getConfiguredTables() {
        List<String> tables = new ArrayList<>();
        
        try {
            String tablesConfig = ConfigReader.getProperty("db.cleanup.tables", "");
            
            if (!tablesConfig.isEmpty()) {
                String[] tableArray = tablesConfig.split(",");
                for (String table : tableArray) {
                    String trimmed = table.trim();
                    if (!trimmed.isEmpty()) {
                        tables.add(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtil.warn(logger, "Could not read db.cleanup.tables from configuration: " + e.getMessage());
        }
        
        return tables;
    }

    /**
     * Disables foreign key checks (MySQL/MariaDB).
     * Useful before cleanup operations to avoid constraint errors.
     */
    public static void disableForeignKeyChecks() {
        try {
            DatabaseUtil.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            LoggerUtil.debug(logger, "Foreign key checks disabled");
        } catch (DatabaseException e) {
            LoggerUtil.warn(logger, "Failed to disable foreign key checks: " + e.getMessage());
        }
    }

    /**
     * Enables foreign key checks (MySQL/MariaDB).
     * Should be called after cleanup operations.
     */
    public static void enableForeignKeyChecks() {
        try {
            DatabaseUtil.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            LoggerUtil.debug(logger, "Foreign key checks enabled");
        } catch (DatabaseException e) {
            LoggerUtil.warn(logger, "Failed to enable foreign key checks: " + e.getMessage());
        }
    }

    /**
     * Cleans all configured tables with foreign key checks temporarily disabled.
     * Safer for tables with complex foreign key relationships.
     */
    public static void cleanAllTablesIgnoringConstraints() {
        try {
            disableForeignKeyChecks();
            cleanAllConfiguredTables();
        } finally {
            enableForeignKeyChecks();
        }
    }

    /**
     * Gets row count for a specific table.
     * @param tableName name of the table
     * @return number of rows in the table
     */
    public static int getTableRowCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Object count = DatabaseUtil.fetchSingleValue(sql);
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Verifies if all configured tables are empty after cleanup.
     * @return true if all tables are empty
     */
    public static boolean verifyCleanupSuccess() {
        List<String> tables = getConfiguredTables();
        boolean allEmpty = true;
        
        for (String tableName : tables) {
            try {
                int count = getTableRowCount(tableName);
                if (count > 0) {
                    LoggerUtil.warn(logger, "Table '" + tableName + "' still has " + count + " row(s) after cleanup");
                    allEmpty = false;
                } else {
                    LoggerUtil.debug(logger, "Table '" + tableName + "' is empty");
                }
            } catch (DatabaseException e) {
                LoggerUtil.warn(logger, "Could not verify cleanup for table '" + tableName + "': " + e.getMessage());
                allEmpty = false;
            }
        }
        
        return allEmpty;
    }
}
