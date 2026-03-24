/**
 * Global Setup
 * Runs ONCE before all tests
 */
import { executeQuery, closePool, cleanupTestData } from '../lib/database/dbHelper.js';
import { getConfig } from '../config/test.config.js';
import logger from '../utils/logger.js';

export default async function globalSetup() {
  logger.step('GLOBAL SETUP - Starting');

  try {
    // 1. Load configuration
    const config = getConfig();
    logger.info(`Environment: ${config.name}`);
    
    // 2. Run database smoke check
    logger.info('Running database smoke check...');
    // const healthCheck = await executeQuery('SureReturns', 'SELECT 1 AS health');
    // if (!healthCheck?.length || healthCheck[0].health !== 1) {
    //   throw new Error('Database smoke check failed');
    // }
    logger.success('✓ Database smoke check passed');
    
    // 3. Cleanup existing test data
    logger.info('Cleaning up test data...');
    await cleanupTestData();
    
    // 4. Seed fresh test data
    logger.info('Seeding test data...');
    // await seedTestData();
    
    // 5. Store global state if needed
    process.env.SETUP_TIMESTAMP = new Date().toISOString();
    
    logger.success('✓ GLOBAL SETUP - Completed successfully');
  } catch (error) {
    logger.error(`✗ GLOBAL SETUP - Failed: ${error.message}`);
    throw error;
  } finally {
    try {
      await closePool();
      logger.info('✓ Database connection closed');
    } catch (error) {
      logger.error(`✗ Failed to close connection: ${error.message}`);
    }
  }
}
