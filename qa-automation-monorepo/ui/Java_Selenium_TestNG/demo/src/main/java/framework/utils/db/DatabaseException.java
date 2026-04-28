package framework.utils.db;

/**
 * DatabaseException
 * Runtime exception wrapper for database operations in framework utilities.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
