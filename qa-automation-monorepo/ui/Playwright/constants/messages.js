/**
 * Constants - Messages
 * Centralized error and success messages
 */
export const ERROR_MESSAGES = {
  LOGIN_FAILED: 'Login failed. Please check credentials.',
  ELEMENT_NOT_FOUND: 'Element not found on the page.',
  TIMEOUT: 'Operation timed out.',
  NETWORK_ERROR: 'Network request failed.',
  DATABASE_ERROR: 'Database operation failed.',
};

export const SUCCESS_MESSAGES = {
  LOGIN_SUCCESS: 'Login successful',
  LOGOUT_SUCCESS: 'Logout successful',
  DATA_SAVED: 'Data saved successfully',
  RECORD_CREATED: 'Record created successfully',
  RECORD_DELETED: 'Record deleted successfully',
};

export const VALIDATION_MESSAGES = {
  REQUIRED_FIELD: 'This field is required',
  INVALID_EMAIL: 'Please enter a valid email',
  PASSWORD_MISMATCH: 'Passwords do not match',
  INVALID_FORMAT: 'Invalid format',
};
