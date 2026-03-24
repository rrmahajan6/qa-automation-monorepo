/**
 * Azure Key Vault Client
 * Manages secure retrieval of secrets from Azure Key Vault
 */
import { SecretClient } from '@azure/keyvault-secrets';
import { DefaultAzureCredential, ClientSecretCredential } from '@azure/identity';
import logger from '../../utils/logger.js';

class KeyVaultClient {
  constructor() {
    this.client = null;
    this.secretCache = new Map();
  }

  /**
   * Initialize Key Vault client
   * @param {string} keyVaultName - Name of your Azure Key Vault
   * @returns {SecretClient}
   */
  async initialize(keyVaultName) {
    if (this.client) {
      return this.client;
    }

    try {
      const keyVaultUrl = `https://${keyVaultName}.vault.azure.net`;
      
      // Choose authentication method based on environment
      const credential = this.getCredential();
      
      this.client = new SecretClient(keyVaultUrl, credential);
      
      logger.info(`✓ Azure Key Vault client initialized: ${keyVaultName}`);
      return this.client;
    } catch (error) {
      logger.error(`✗ Failed to initialize Key Vault client: ${error.message}`);
      throw error;
    }
  }

  /**
   * Get appropriate credential based on environment
   * @returns {DefaultAzureCredential|ClientSecretCredential}
   */
  getCredential() {
    // For local development or CI/CD with service principal
    if (process.env.AZURE_TENANT_ID && 
        process.env.AZURE_CLIENT_ID && 
        process.env.AZURE_CLIENT_SECRET) {
      logger.info('Using Service Principal authentication');
      return new ClientSecretCredential(
        process.env.AZURE_TENANT_ID,
        process.env.AZURE_CLIENT_ID,
        process.env.AZURE_CLIENT_SECRET
      );
    }
    
    // For Azure-hosted environments (uses Managed Identity)
    // or local development with Azure CLI
    logger.info('Using DefaultAzureCredential (Managed Identity or Azure CLI)');
    return new DefaultAzureCredential();
  }

  /**
   * Get secret from Key Vault with caching
   * @param {string} secretName - Name of the secret in Key Vault
   * @param {boolean} useCache - Whether to use cached value (default: true)
   * @returns {Promise<string>} Secret value
   */
  async getSecret(secretName, useCache = true) {
    if (!this.client) {
      throw new Error('Key Vault client not initialized. Call initialize() first.');
    }

    // Return cached value if available and caching is enabled
    if (useCache && this.secretCache.has(secretName)) {
      logger.info(`✓ Retrieved secret from cache: ${secretName}`);
      return this.secretCache.get(secretName);
    }

    try {
      const secret = await this.client.getSecret(secretName);
      const secretValue = secret.value;
      
      // Cache the secret
      this.secretCache.set(secretName, secretValue);
      
      logger.info(`✓ Retrieved secret from Key Vault: ${secretName}`);
      return secretValue;
    } catch (error) {
      logger.error(`✗ Failed to retrieve secret '${secretName}': ${error.message}`);
      throw error;
    }
  }

  /**
   * Get multiple secrets at once
   * @param {string[]} secretNames - Array of secret names
   * @returns {Promise<Object>} Object with secretName: secretValue pairs
   */
  async getSecrets(secretNames) {
    const secrets = {};
    
    for (const secretName of secretNames) {
      secrets[secretName] = await this.getSecret(secretName);
    }
    
    return secrets;
  }

  /**
   * Clear secret cache
   */
  clearCache() {
    this.secretCache.clear();
    logger.info('Secret cache cleared');
  }

  /**
   * List all secret names (useful for debugging)
   * @returns {Promise<string[]>} Array of secret names
   */
  async listSecretNames() {
    if (!this.client) {
      throw new Error('Key Vault client not initialized. Call initialize() first.');
    }

    try {
      const secretNames = [];
      for await (const secretProperties of this.client.listPropertiesOfSecrets()) {
        secretNames.push(secretProperties.name);
      }
      
      logger.info(`✓ Found ${secretNames.length} secrets in Key Vault`);
      return secretNames;
    } catch (error) {
      logger.error(`✗ Failed to list secrets: ${error.message}`);
      throw error;
    }
  }
}

// Export singleton instance
export default new KeyVaultClient();
