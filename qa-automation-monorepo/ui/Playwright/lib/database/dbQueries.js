/**
 * Common SQL Queries
 * Centralized SQL queries for reusability
 */

export const UserQueries = {
  // Select queries
  SELECT_USER_BY_EMAIL: 'SELECT * FROM test_users WHERE email = $1',
  SELECT_USER_BY_ID: 'SELECT * FROM test_users WHERE id = $1',
  SELECT_ALL_USERS: 'SELECT * FROM test_users WHERE created_by = $1',
  
  // Insert queries
  INSERT_USER: `
    INSERT INTO test_users (username, email, password, created_by)
    VALUES ($1, $2, $3, $4)
    RETURNING *
  `,
  
  // Update queries
  UPDATE_USER_PASSWORD: `
    UPDATE test_users 
    SET password = $1, updated_at = NOW()
    WHERE email = $2
    RETURNING *
  `,
  
  // Delete queries
  DELETE_USER_BY_EMAIL: 'DELETE FROM test_users WHERE email = $1',
  DELETE_ALL_TEST_USERS: "DELETE FROM test_users WHERE created_by = 'automation'",
};

export const OrderQueries = {
  SELECT_ORDER_BY_ID: 'SELECT * FROM test_orders WHERE id = $1',
  SELECT_ORDERS_BY_USER: 'SELECT * FROM test_orders WHERE user_id = $1',
  
  INSERT_ORDER: `
    INSERT INTO test_orders (user_id, total_amount, status, created_by)
    VALUES ($1, $2, $3, 'automation')
    RETURNING *
  `,
  
  DELETE_ALL_TEST_ORDERS: "DELETE FROM test_orders WHERE created_by = 'automation'",
};

export const CleanupQueries = {
  CLEANUP_ALL: `
    DELETE FROM test_users WHERE created_by = 'automation';
    DELETE FROM test_orders WHERE created_by = 'automation';
    DELETE FROM test_sessions WHERE created_by = 'automation';
  `,
};
