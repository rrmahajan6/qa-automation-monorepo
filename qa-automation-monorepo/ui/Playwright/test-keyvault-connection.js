/**
 * Test Azure Key Vault Connection
 * Run this script to verify your Azure Key Vault setup
 * 
 * Usage: node test-keyvault-connection.js
 */
import 'dotenv/config';
import keyVaultClient from './lib/azure/keyVaultClient.js';
import { ConnectDB, executeQuery, closePool } from './lib/database/dbHelper.js';

async function testKeyVaultConnection() {
  console.log('\n========================================');
  console.log('Testing Azure Key Vault Connection');
  console.log('========================================\n');

  try {
    // Step 1: Verify environment variables
    console.log('Step 1: Checking environment variables...');
    const keyVaultName = process.env.AZURE_KEYVAULT_NAME;
    const env = process.env.ENV || 'dev';
    
    if (!keyVaultName) {
      throw new Error('AZURE_KEYVAULT_NAME is not set in environment variables');
    }
    
    console.log(`✓ Key Vault Name: ${keyVaultName}`);
    console.log(`✓ Environment: ${env}`);
    
    // Step 2: Initialize Key Vault client
    console.log('\nStep 2: Initializing Key Vault client...');
    await keyVaultClient.initialize(keyVaultName);
    console.log('✓ Key Vault client initialized successfully');

    // Step 3: List available secrets
    console.log('\nStep 3: Listing available secrets...');
    const secretNames = await keyVaultClient.listSecretNames();
    console.log(`✓ Found ${secretNames.length} secrets:`);
    secretNames.forEach(name => console.log(`  - ${name}`));

    // Step 4: Test retrieving database secrets
    console.log('\nStep 4: Retrieving database secrets...');
    const secretPrefix = `db-${env}`;
    const requiredSecrets = [
      `${secretPrefix}-host`,
      `${secretPrefix}-port`,
      `${secretPrefix}-database`,
      `${secretPrefix}-user`,
      `${secretPrefix}-password`,
    ];

    const secrets = {};
    for (const secretName of requiredSecrets) {
      try {
        const value = await keyVaultClient.getSecret(secretName);
        secrets[secretName] = secretName.includes('password') ? '***' : value;
        console.log(`✓ Retrieved: ${secretName} = ${secrets[secretName]}`);
      } catch (error) {
        console.log(`✗ Failed to retrieve: ${secretName}`);
        throw new Error(`Missing required secret: ${secretName}`);
      }
    }

    // Step 5: Test database connection
    console.log('\nStep 5: Testing database connection...');
    await ConnectDB('SureReturns');
    console.log('✓ Database connection pool created');

    // Step 6: Execute test query
    console.log('\nStep 6: Executing test query...');
    const result = await executeQuery('SureReturns', 'SELECT GETDATE() AS currentTime');
    console.log('✓ Query executed successfully');
    console.log(`  Current time from database: ${result[0].currentTime}`);

    // Step 7: Cleanup
    console.log('\nStep 7: Cleaning up...');
    await closePool();
    console.log('✓ Database connection closed');

    console.log('\n========================================');
    console.log('✅ All tests passed successfully!');
    console.log('========================================\n');

  } catch (error) {
    console.error('\n========================================');
    console.error('❌ Test failed!');
    console.error('========================================');
    console.error(`Error: ${error.message}`);
    console.error('\nTroubleshooting:');
    console.error('1. Verify AZURE_KEYVAULT_NAME is set correctly');
    console.error('2. Ensure Azure credentials are configured (Service Principal or Azure CLI)');
    console.error('3. Check that all required secrets exist in Key Vault');
    console.error('4. Verify Key Vault access policies grant "Get" and "List" permissions');
    console.error('5. Ensure database server is accessible from your network\n');
    
    process.exit(1);
  }
}

// Run the test
testKeyVaultConnection();
