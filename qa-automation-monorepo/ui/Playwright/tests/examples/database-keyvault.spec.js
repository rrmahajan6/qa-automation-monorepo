/**
 * Example: Using Azure Key Vault in Playwright Tests
 * This file demonstrates how to use Azure Key Vault for database operations in tests
 */
import logger from '../../utils/logger.js';
import { test, expect } from '../../fixtures/testFixtures.js';

test.describe('Database Operations with Azure Key Vault', () => {
  
  test('should connect to database using Key Vault credentials', async ({ db }) => {
    const result = await db.query("select Id from taxForm where TaxAuthorityId = '4899'");

    expect(result).toBeDefined();
    expect(result.rows.length).toBeGreaterThan(0);
    logger.info(`DB result: ${JSON.stringify(result.rows, null, 2)}`);
  });

  test('should query users from database', async ({ db }) => {
    const query = "select * from taxForm where TaxAuthorityId = '4899'";
    const result = await db.query(query);
    expect(result).toBeDefined();
    logger.info(`Found ${result.rowCount} users`);
  });

  test('db fixture example', async ({ db }) => {
    const result = await db.query("select Id from taxForm where TaxAuthorityId = '4899'");
    expect(result.rows.length).toBeGreaterThan(0);
    logger.info(`DB fixture result: ${JSON.stringify(result.rows, null, 2)}`);
  });
});
