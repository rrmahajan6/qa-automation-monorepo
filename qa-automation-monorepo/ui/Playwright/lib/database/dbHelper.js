import KeyVaultSecrets from "./KeyVaultSecrets.js";
import mssql from 'mssql';
import logger from '../../utils/logger.js';

const keyVaultUrl = 'https://sqluse1.vault.azure.net/';
const keyVault = new KeyVaultSecrets(keyVaultUrl);

// Global variables
let pool = null;
const environment = process.env.ENV || 'dev';

const config = {
  options: {
    encrypt: true,
    trustServerCertificate: true,
    requestTimeout: 30000,
    connectionTimeout: 30000,
  },
};

  
function parseConnectionString(connectionString) {
  if (!connectionString || typeof connectionString !== "string") {
    throw new Error("Invalid connection string provided");
  }
  connectionString.split(";").forEach((pair) => {
    if (pair.trim()) {
      const equalIndex = pair.indexOf("=");
      if (equalIndex > 0) {
        const key = pair.substring(0, equalIndex).trim().toLowerCase();
        const value = pair.substring(equalIndex + 1).trim();
        switch (key) {
          case "data source":
          case "server":
            config.server = value;
            break;
          case "initial catalog":
          case "database":
            config.database = value;
            break;
          case "user":
          case "uid":
            config.user = value;
            break;
          case "password":
          case "pwd":
            config.password = value;
            break;
          case "encrypt":
            config.options.encrypt = value.toLowerCase() === "true";
            break;
          case "trustservercertificate":
            config.options.trustServerCertificate =
              value.toLowerCase() === "true";
            break;
          case "connection timeout":
            config.options.connectionTimeout = parseInt(value) * 1000;
            break;
          case "command timeout":
            config.options.requestTimeout = parseInt(value) * 1000;
            break;
        }
      }
    }
  });
  return config;
}

async function setDBConfig(DatabaseName) {
  const connectionString = await keyVault.getSecret(
    "sqlpassword",
    false
  );

  if (!connectionString || connectionString.trim() === "") {
    throw new Error("Connection string from Key Vault is empty or null");
  }

  const config = parseConnectionString(connectionString);

  if (!config.server || !config.database || !config.user || !config.password) {
    throw new Error("Missing required database connection parameters");
  }
  return config;
}
async function ConnectDB(DatabaseName) {
  if (pool !== null) {
    if (pool.config.database.includes(`${environment}-${DatabaseName}`)) {
      return pool;
    }
    await pool.close();
  }

  const config = await setDBConfig(DatabaseName);
  pool = await mssql.connect(config);
  return pool;   
}

function applyQueryParameters(request, params = {}) {
  if (!params || typeof params !== 'object' || Array.isArray(params)) {
    return request;
  }

  for (const [name, value] of Object.entries(params)) {
    request.input(name, value);
  }

  return request;
}

async function executeQuery(DatabaseName, query, params = {}) {
  const connect = await ConnectDB(DatabaseName);

  try {
    const request = applyQueryParameters(connect.request(), params);
    const result = await request.query(query);
    return result.recordset;
  } catch (error) {
    logger.error(`Database query failed: ${error.message}`);
    throw error;
  }
}

async function closePool() {
  if (pool) {
    await pool.close();
    pool = null;
  }
}

/**
 * Cleanup test data from database
 * Add your cleanup logic here
 */
async function cleanupTestData() {
  try {
    logger.info('Starting test data cleanup...');
    // Add your cleanup queries here
    // Example:
    // await executeQuery('SureReturns', 'DELETE FROM TestTable WHERE isTestData = 1');
    logger.info('Test data cleanup completed');
  } catch (error) {
    logger.error(`Error during test data cleanup: ${error.message}`);
    throw error;
  }
}

export {
  executeQuery,
  ConnectDB,
  closePool,
  cleanupTestData,
};
