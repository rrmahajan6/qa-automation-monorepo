/**
 * Test Fixtures
 * Custom Playwright fixtures for enhanced testing
 */
import { test as base } from '@playwright/test';
import { ConnectDB, closePool } from '../lib/database/dbHelper.js';
import { LoginPage } from '../pages/login/LoginPage.js';
import { TestHooks } from '../hooks/testHooks.js';
import logger from '../utils/logger.js';

/**
 * Extended test with custom fixtures
 */
export const test = base.extend({
  /**
   * Database fixture - provides SQL query helper to all tests in a worker
   * Worker-scoped: Created once per worker, shared across all tests in that worker
   */
  db: [async ({}, use) => {
    logger.debug('Worker started - acquiring shared SQL connection pool');
    const databaseName = process.env.DB_NAME || 'SureReturns';
    const pool = await ConnectDB(databaseName);

    const execute = async (query, params = {}) => {
      const request = pool.request();
      if (params && typeof params === 'object' && !Array.isArray(params)) {
        for (const [name, value] of Object.entries(params)) {
          request.input(name, value);
        }
      }

      const result = await request.query(query);
      return {
        rows: result.recordset,
        rowCount: result.rowsAffected?.[0] ?? result.recordset.length,
      };
    };

    try {
      await use({ query: execute });
    } finally {
      await closePool();
      logger.debug('Worker finished - SQL connection pool closed');
    }
  }, { scope: 'worker' }],

  /**
   * Page fixture with enhanced error handling
   */
  page: async ({ page }, use, testInfo) => {
    // Before each test
    await TestHooks.beforeEach(testInfo);
    
    // Make page available to test
    await use(page);
    
    // After each test
    await TestHooks.afterEach(page, testInfo);
  },

  /**
   * Login page fixture
   */
  loginPage: async ({ page }, use) => {
    const loginPage = new LoginPage(page);
    await use(loginPage);
  },

  /**
   * Authenticated page fixture - automatically logs in
   */
  authenticatedPage: async ({ page, loginPage }, use) => {
    await loginPage.goto();
    await loginPage.loginWithDefaultCredentials();
    await use(page);
  },
});

/**
 * Export expect for convenience
 */
export { expect } from '@playwright/test';

/*
authenticatedPage: async ({ page, loginPage }, use) => {
    // SETUP - runs BEFORE test
    await loginPage.goto();
    await loginPage.loginWithDefaultCredentials();
    
    await use(page);  // ← PAUSES HERE while test runs
    
    // TEARDOWN - runs AFTER test completes
    // Add cleanup code here if needed
}

┌─────────────────────────────────┐
│ Fixture Setup (before use)      │ ← Create resources
├─────────────────────────────────┤
│ await use(value)                │ ← Pass to test & WAIT
├─────────────────────────────────┤
│ Test runs using the value       │ ← Test executes
├─────────────────────────────────┤
│ use() completes                 │ ← Test finished
├─────────────────────────────────┤
│ Fixture Teardown (after use)    │ ← Cleanup resources
└─────────────────────────────────┘

*/