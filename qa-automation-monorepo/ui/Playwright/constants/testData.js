/**
 * Constants - Test Data
 * Common test data constants
 */
export const TEST_USERS = {
  ADMIN: {
    username: 'admin',
    email: 'admin@test.com',
    password: 'Admin@123',
    role: 'admin',
  },
  STANDARD_USER: {
    username: 'testuser',
    email: 'testuser@test.com',
    password: 'Test@123',
    role: 'user',
  },
  INVALID_USER: {
    username: 'invalid',
    email: 'invalid@test.com',
    password: 'wrong_password',
  },
};

export const TEST_DATA = {
  VALID_EMAIL: 'valid@example.com',
  INVALID_EMAIL: 'invalid-email',
  PHONE_NUMBER: '+1234567890',
  ZIP_CODE: '12345',
};
