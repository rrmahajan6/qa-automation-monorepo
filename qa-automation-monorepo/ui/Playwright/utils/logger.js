/**
 * Custom Logger Utility
 * Provides structured logging for the framework
 */
import fs from 'fs';
import path from 'path';

class Logger {
  constructor() {
    this.logDir = path.join(process.cwd(), 'logs');
    this.ensureLogDirectory();
  }

  /**
   * Ensure log directory exists
   */
  ensureLogDirectory() {
    if (!fs.existsSync(this.logDir)) {
      fs.mkdirSync(this.logDir, { recursive: true });
    }
  }

  /**
   * Get current timestamp
   * @returns {string} Formatted timestamp
   */
  getTimestamp() {
    return new Date().toISOString();
  }

  /**
   * Format log message
   * @param {string} level - Log level
   * @param {string} message - Log message
   * @returns {string} Formatted message
   */
  formatMessage(level, message) {
    return `[${this.getTimestamp()}] [${level.toUpperCase()}] ${message}`;
  }

  /**
   * Write log to file
   * @param {string} message - Log message
   */
  writeToFile(message) {
    const logFile = path.join(this.logDir, `test-${new Date().toISOString().split('T')[0]}.log`);
    fs.appendFileSync(logFile, message + '\n');
  }

  /**
   * Log info message
   * @param {string} message - Log message
   */
  info(message) {
    const formatted = this.formatMessage('info', message);
    console.log(`\x1b[36m${formatted}\x1b[0m`); // Cyan
    this.writeToFile(formatted);
  }

  /**
   * Log error message
   * @param {string} message - Log message
   */
  error(message) {
    const formatted = this.formatMessage('error', message);
    console.error(`\x1b[31m${formatted}\x1b[0m`); // Red
    this.writeToFile(formatted);
  }

  /**
   * Log warning message
   * @param {string} message - Log message
   */
  warn(message) {
    const formatted = this.formatMessage('warn', message);
    console.warn(`\x1b[33m${formatted}\x1b[0m`); // Yellow
    this.writeToFile(formatted);
  }

  /**
   * Log success message
   * @param {string} message - Log message
   */
  success(message) {
    const formatted = this.formatMessage('success', message);
    console.log(`\x1b[32m${formatted}\x1b[0m`); // Green
    this.writeToFile(formatted);
  }

  /**
   * Log debug message
   * @param {string} message - Log message
   */
  debug(message) {
    if (process.env.DEBUG === 'true') {
      const formatted = this.formatMessage('debug', message);
      console.log(`\x1b[90m${formatted}\x1b[0m`); // Gray
      this.writeToFile(formatted);
    }
  }

  /**
   * Log step in test
   * @param {string} message - Step description
   */
  step(message) {
    const formatted = `\n${'='.repeat(80)}\n${message}\n${'='.repeat(80)}`;
    console.log(`\x1b[35m${formatted}\x1b[0m`); // Magenta
    this.writeToFile(formatted);
  }
}

// Export singleton instance
const logger = new Logger();
export default logger;
