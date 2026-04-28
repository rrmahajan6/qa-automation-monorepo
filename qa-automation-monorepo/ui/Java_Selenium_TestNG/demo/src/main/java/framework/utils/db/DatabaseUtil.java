package framework.utils.db;

import framework.utils.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DatabaseUtil
 * Helper methods for running parameterized queries and updates.
 */
public final class DatabaseUtil {

    private static final Class<?> logger = DatabaseUtil.class;

    private DatabaseUtil() {
    }

    /**
     * Executes a SELECT query and returns all rows as list of column-value maps.
     * @param sql SQL query with optional placeholders
     * @param params query parameters in order
     * @return list of rows
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            setParameters(preparedStatement, params);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute query: " + sql, e);
        }
    }

    /**
     * Executes INSERT/UPDATE/DELETE operation.
     * @param sql SQL statement with optional placeholders
     * @param params statement parameters in order
     * @return number of affected rows
     */
    public static int executeUpdate(String sql, Object... params) {
        try (
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            setParameters(preparedStatement, params);
            int affectedRows = preparedStatement.executeUpdate();
            LoggerUtil.info(logger, "Database update executed. Rows affected: " + affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute update: " + sql, e);
        }
    }

    /**
     * Executes query and returns the first column of the first row.
     * @param sql SQL query with optional placeholders
     * @param params query parameters in order
     * @return first column value or null when no rows exist
     */
    public static Object fetchSingleValue(String sql, Object... params) {
        List<Map<String, Object>> rows = executeQuery(sql, params);
        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> firstRow = rows.get(0);
        if (firstRow.isEmpty()) {
            return null;
        }

        return firstRow.values().iterator().next();
    }

    /**
     * Convenience method for checking DB reachability.
     * @return true if DB connection can be established
     */
    public static boolean isDatabaseReachable() {
        return DatabaseManager.isReachable();
    }

    /**
     * Checks if a record exists based on query condition.
     * @param tableName table name
     * @param whereClause WHERE condition (without WHERE keyword)
     * @param params query parameters
     * @return true if at least one record exists
     */
    public static boolean recordExists(String tableName, String whereClause, Object... params) {
        if (!hasWhereClause(whereClause)) {
            return recordExists(tableName);
        }
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause;
        Object count = fetchSingleValue(sql, params);
        return count != null && ((Number) count).intValue() > 0;
    }

    /**
     * Checks if a table has at least one record.
     * @param tableName table name
     * @return true if table has one or more records
     */
    public static boolean recordExists(String tableName) {
        return getTableCount(tableName) > 0;
    }

    /**
     * Gets count of records matching the condition.
     * @param tableName table name
     * @param whereClause WHERE condition (without WHERE keyword)
     * @param params query parameters
     * @return count of matching records
     */
    public static int getRecordCount(String tableName, String whereClause, Object... params) {
        if (!hasWhereClause(whereClause)) {
            return getTableCount(tableName);
        }
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause;
        Object count = fetchSingleValue(sql, params);
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Gets total count of records in a table.
     * @param tableName table name
     * @return total record count
     */
    public static int getRecordCount(String tableName) {
        return getTableCount(tableName);
    }

    /**
     * Gets total count of records in a table.
     * @param tableName table name
     * @return total record count
     */
    public static int getTableCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Object count = fetchSingleValue(sql);
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Fetches a single row as a map.
     * @param sql SQL query with optional placeholders
     * @param params query parameters
     * @return first row as map, or null if no rows found
     */
    public static Map<String, Object> fetchSingleRow(String sql, Object... params) {
        List<Map<String, Object>> rows = executeQuery(sql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * Fetches a specific column value from a single row.
     * @param sql SQL query with optional placeholders
     * @param columnName column name to fetch
     * @param params query parameters
     * @return column value or null if not found
     */
    public static Object fetchColumnValue(String sql, String columnName, Object... params) {
        Map<String, Object> row = fetchSingleRow(sql, params);
        return row != null ? row.get(columnName) : null;
    }

    /**
     * Fetches values from a specific column as a list.
     * @param sql SQL query with optional placeholders
     * @param columnName column name to extract
     * @param params query parameters
     * @return list of column values
     */
    public static List<Object> fetchColumnValues(String sql, String columnName, Object... params) {
        List<Map<String, Object>> rows = executeQuery(sql, params);
        List<Object> values = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            values.add(row.get(columnName));
        }
        return values;
    }

    /**
     * Inserts a record into a table.
     * @param tableName table name
     * @param columnValues map of column names to values
     * @return number of rows inserted (typically 1)
     */
    public static int insertRecord(String tableName, Map<String, Object> columnValues) {
        if (columnValues.isEmpty()) {
            throw new DatabaseException("Cannot insert empty record into " + tableName);
        }

        List<String> columns = new ArrayList<>(columnValues.keySet());
        List<Object> values = new ArrayList<>(columnValues.values());

        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder placeholders = new StringBuilder("VALUES (");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
            placeholders.append("?");
            if (i < columns.size() - 1) {
                sql.append(", ");
                placeholders.append(", ");
            }
        }

        sql.append(") ").append(placeholders).append(")");
        return executeUpdate(sql.toString(), values.toArray());
    }

    /**
     * Updates records in a table.
     * @param tableName table name
     * @param columnValues map of column names to new values
     * @param whereClause WHERE condition (without WHERE keyword)
     * @param whereParams parameters for WHERE clause
     * @return number of rows updated
     */
    public static int updateRecord(String tableName, Map<String, Object> columnValues, String whereClause, Object... whereParams) {
        if (columnValues.isEmpty()) {
            throw new DatabaseException("Cannot update with empty values for table " + tableName);
        }

        List<String> columns = new ArrayList<>(columnValues.keySet());
        List<Object> values = new ArrayList<>(columnValues.values());

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i)).append(" = ?");
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }

        if (hasWhereClause(whereClause)) {
            sql.append(" WHERE ").append(whereClause);
        }

        // Combine SET values and WHERE params
        if (hasWhereClause(whereClause) && whereParams != null && whereParams.length > 0) {
            values.addAll(Arrays.asList(whereParams));
        }

        return executeUpdate(sql.toString(), values.toArray());
    }

    /**
     * Updates all records in a table.
     * @param tableName table name
     * @param columnValues map of column names to new values
     * @return number of rows updated
     */
    public static int updateAllRecords(String tableName, Map<String, Object> columnValues) {
        return updateRecord(tableName, columnValues, null);
    }

    /**
     * Deletes records from a table.
     * @param tableName table name
     * @param whereClause WHERE condition (without WHERE keyword)
     * @param params parameters for WHERE clause
     * @return number of rows deleted
     */
    public static int deleteRecords(String tableName, String whereClause, Object... params) {
        if (!hasWhereClause(whereClause)) {
            return deleteRecords(tableName);
        }
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
        return executeUpdate(sql, params);
    }

    /**
     * Deletes all records from a table.
     * @param tableName table name
     * @return number of rows deleted
     */
    public static int deleteRecords(String tableName) {
        String sql = "DELETE FROM " + tableName;
        return executeUpdate(sql);
    }

    /**
     * Executes a batch of SQL statements.
     * @param sqlStatements list of SQL statements to execute
     * @return array of update counts
     */
    public static int[] executeBatch(List<String> sqlStatements) {
        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int[] results = new int[sqlStatements.size()];
                for (int i = 0; i < sqlStatements.size(); i++) {
                    results[i] = executeUpdate(sqlStatements.get(i));
                }
                connection.commit();
                LoggerUtil.info(logger, "Batch execution completed: " + sqlStatements.size() + " statements");
                return results;
            } catch (SQLException e) {
                connection.rollback();
                throw new DatabaseException("Batch execution failed and was rolled back", e);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute batch operations", e);
        }
    }

    /**
     * Prints query results to console (for debugging).
     * @param sql SQL query
     * @param params query parameters
     */
    public static void printQueryResults(String sql, Object... params) {
        List<Map<String, Object>> results = executeQuery(sql, params);
        
        if (results.isEmpty()) {
            LoggerUtil.info(logger, "Query returned no results: " + sql);
            return;
        }

        LoggerUtil.info(logger, "========== Query Results (" + results.size() + " row(s)) ==========");
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> row = results.get(i);
            LoggerUtil.info(logger, "Row " + (i + 1) + ": " + row);
        }
        LoggerUtil.info(logger, "================================================");
    }

    /**
     * Validates that a record exists, throws exception if not found.
     * @param tableName table name
     * @param whereClause WHERE condition
     * @param params query parameters
     * @throws AssertionError if record doesn't exist
     */
    public static void assertRecordExists(String tableName, String whereClause, Object... params) {
        if (!recordExists(tableName, whereClause, params)) {
            throw new AssertionError("Expected record not found in table '" + tableName + "' with condition: " + whereClause);
        }
    }

    /**
     * Validates that at least one record exists in table.
     * @param tableName table name
     * @throws AssertionError if table has no records
     */
    public static void assertRecordExists(String tableName) {
        if (!recordExists(tableName)) {
            throw new AssertionError("Expected at least one record in table '" + tableName + "'");
        }
    }

    /**
     * Validates that a record does NOT exist.
     * @param tableName table name
     * @param whereClause WHERE condition
     * @param params query parameters
     * @throws AssertionError if record exists
     */
    public static void assertRecordNotExists(String tableName, String whereClause, Object... params) {
        if (recordExists(tableName, whereClause, params)) {
            throw new AssertionError("Unexpected record found in table '" + tableName + "' with condition: " + whereClause);
        }
    }

    /**
     * Validates that table is empty.
     * @param tableName table name
     * @throws AssertionError if table has any records
     */
    public static void assertRecordNotExists(String tableName) {
        if (recordExists(tableName)) {
            throw new AssertionError("Unexpected record(s) found in table '" + tableName + "'");
        }
    }

    private static boolean hasWhereClause(String whereClause) {
        return whereClause != null && !whereClause.isBlank();
    }

    private static void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        if (params == null) {
            return;
        }

        for (int index = 0; index < params.length; index++) {
            preparedStatement.setObject(index + 1, params[index]);
        }
    }

    private static List<Map<String, Object>> mapResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int column = 1; column <= columnCount; column++) {
                row.put(metaData.getColumnLabel(column), resultSet.getObject(column));
            }
            rows.add(row);
        }

        return rows;
    }
}
