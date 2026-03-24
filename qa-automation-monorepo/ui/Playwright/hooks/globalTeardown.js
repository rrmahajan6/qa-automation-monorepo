/**
 * Global Teardown
 * Runs ONCE after all tests complete
 */
import { cleanupTestData, closePool } from '../lib/database/dbHelper.js';
import logger from '../utils/logger.js';

export default async function globalTeardown() {
  logger.step('GLOBAL TEARDOWN - Starting');

  try {
    // 1. Create temporary database connection
    logger.info('Creating database connection...');
    // No-op: shared SQL pool is managed by dbHelper
    
    // 2. Final cleanup of test data
    logger.info('Performing final cleanup...');
    // await cleanupTestData();
    
    // 3. Generate summary report (optional)
    logger.info('Test execution completed at: ' + new Date().toISOString());
    logger.info('Setup timestamp: ' + process.env.SETUP_TIMESTAMP);
    
    // 4. Send notifications (optional)
    // await sendSlackNotification();
    // await sendEmailReport();
    
    logger.success('✓ GLOBAL TEARDOWN - Completed successfully');
  } catch (error) {
    logger.error(`✗ GLOBAL TEARDOWN - Failed: ${error.message}`);
    // Don't throw error in teardown to avoid masking test failures
  } finally {
    await closePool();
  }
}
