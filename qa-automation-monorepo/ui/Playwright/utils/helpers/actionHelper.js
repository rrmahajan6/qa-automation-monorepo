/**
 * Action Helper Functions
 * Reusable action methods with enhanced error handling
 */
import logger from '../logger.js';
import { waitForVisible } from './waitHelper.js';

/**
 * Click element with retry
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {Object} options - Click options
 */
export async function clickWithRetry(locator, options = {}) {
  const maxRetries = 3;
  let lastError;

  for (let i = 0; i < maxRetries; i++) {
    try {
      await waitForVisible(locator);
      await locator.click(options);
      logger.debug(`Clicked element (attempt ${i + 1})`);
      return;
    } catch (error) {
      lastError = error;
      logger.warn(`Click failed (attempt ${i + 1}): ${error.message}`);
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
  }

  throw new Error(`Failed to click after ${maxRetries} attempts: ${lastError.message}`);
}

/**
 * Fill input with validation
 * @param {import('@playwright/test').Locator} locator - Element locator
 * @param {string} text - Text to fill
 * @param {boolean} clearFirst - Clear field before filling
 */
export async function fillWithValidation(locator, text, clearFirst = true) {
  try {
    await waitForVisible(locator);
    
    if (clearFirst) {
      await locator.clear();
    }
    
    await locator.fill(text);
    
    // Validate the value was set
    const value = await locator.inputValue();
    if (value !== text) {
      throw new Error(`Fill validation failed. Expected: ${text}, Got: ${value}`);
    }
    
    logger.debug(`Filled text: ${text.substring(0, 20)}...`);
  } catch (error) {
    logger.error(`Failed to fill text: ${error.message}`);
    throw error;
  }
}

/**
 * Select dropdown option
 * @param {import('@playwright/test').Locator} locator - Dropdown locator
 * @param {string|Object} value - Value or option object
 */
export async function selectOption(locator, value) {
  try {
    await waitForVisible(locator);
    
    if (typeof value === 'string') {
      await locator.selectOption(value);
    } else {
      await locator.selectOption(value);
    }
    
    logger.debug(`Selected option: ${value}`);
  } catch (error) {
    logger.error(`Failed to select option: ${error.message}`);
    throw error;
  }
}

/**
 * Upload file
 * @param {import('@playwright/test').Locator} locator - File input locator
 * @param {string|Array<string>} filePath - Path to file(s)
 */
export async function uploadFile(locator, filePath) {
  try {
    await locator.setInputFiles(filePath);
    logger.debug(`Uploaded file: ${filePath}`);
  } catch (error) {
    logger.error(`Failed to upload file: ${error.message}`);
    throw error;
  }
}

/**
 * Scroll element into view
 * @param {import('@playwright/test').Locator} locator - Element locator
 */
export async function scrollIntoView(locator) {
  try {
    await locator.scrollIntoViewIfNeeded();
    logger.debug('Scrolled element into view');
  } catch (error) {
    logger.error(`Failed to scroll: ${error.message}`);
    throw error;
  }
}
