# Azure Key Vault Integration for SQL Database Connection

This guide explains how to securely connect to your SQL database using Azure Key Vault to store and retrieve credentials.

## Prerequisites

1. **Azure Key Vault** created in your Azure subscription
2. **Azure credentials** configured (Service Principal or Managed Identity)
3. **Database credentials** stored in Azure Key Vault

## Setup Steps

### 1. Create Azure Key Vault

```bash
# Login to Azure
az login

# Create resource group (if needed)
az group create --name rg-playwright-tests --location eastus

# Create Key Vault
az keyvault create \
  --name your-keyvault-name \
  --resource-group rg-playwright-tests \
  --location eastus
```

### 2. Store Database Credentials in Key Vault

```bash
# Store dev environment credentials
az keyvault secret set --vault-name your-keyvault-name --name db-dev-host --value "your-db-server.database.windows.net"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-port --value "1433"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-database --value "your-database-name"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-user --value "your-username"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-password --value "your-password"

# Store qa environment credentials
az keyvault secret set --vault-name your-keyvault-name --name db-qa-host --value "qa-db-server.database.windows.net"
az keyvault secret set --vault-name your-keyvault-name --name db-qa-port --value "1433"
az keyvault secret set --vault-name your-keyvault-name --name db-qa-database --value "qa-database"
az keyvault secret set --vault-name your-keyvault-name --name db-qa-user --value "qa-username"
az keyvault secret set --vault-name your-keyvault-name --name db-qa-password --value "qa-password"

# Repeat for staging and prod environments
```

### 3. Set Up Authentication

#### Option A: Service Principal (Local Development & CI/CD)

```bash
# Create service principal
az ad sp create-for-rbac --name playwright-keyvault-sp

# Output will contain:
# - appId (AZURE_CLIENT_ID)
# - password (AZURE_CLIENT_SECRET)
# - tenant (AZURE_TENANT_ID)

# Grant access to Key Vault
az keyvault set-policy \
  --name your-keyvault-name \
  --spn <appId> \
  --secret-permissions get list
```

#### Option B: Managed Identity (Azure-hosted applications)

```bash
# Enable system-assigned managed identity on your Azure resource
az webapp identity assign --name your-app-name --resource-group your-rg

# Grant Key Vault access to the managed identity
az keyvault set-policy \
  --name your-keyvault-name \
  --object-id <managed-identity-principal-id> \
  --secret-permissions get list
```

### 4. Configure Environment Variables

Copy `.env.example` to `.env` and update:

```env
# Environment
ENV=dev

# Azure Key Vault
AZURE_KEYVAULT_NAME=your-keyvault-name

# Service Principal (for local development)
AZURE_TENANT_ID=your-tenant-id
AZURE_CLIENT_ID=your-client-id
AZURE_CLIENT_SECRET=your-client-secret

# Database SSL
DB_SSL=true
```

### 5. Update Your Test Code

#### Using the Key Vault-enabled database connection:

```javascript
// In your test setup or hooks
import { ConnectDB, executeQuery, closePool } from './lib/database/dbHelper.js';

// Create connection pool
await ConnectDB('SureReturns');

// Execute queries
const result = await executeQuery('SureReturns', 'SELECT TOP 1 * FROM taxForm');

// Close pool when done
await closePool();
```

#### Using in test hooks:

```javascript
// hooks/globalSetup.js
import { ConnectDB } from './lib/database/dbHelper.js';

export default async function globalSetup() {
  console.log('Setting up database connection...');
  await ConnectDB('SureReturns');
}
```

```javascript
// hooks/globalTeardown.js
import { closePool } from './lib/database/dbHelper.js';

export default async function globalTeardown() {
  console.log('Closing database connection...');
  await closePool();
}
```

## Authentication Methods

### Local Development

1. **Azure CLI**: Run `az login` and the DefaultAzureCredential will use your Azure CLI credentials
2. **Service Principal**: Set environment variables in `.env` file

### CI/CD Pipeline

Use Service Principal credentials as pipeline secrets:

```yaml
# GitHub Actions example
env:
  AZURE_KEYVAULT_NAME: ${{ secrets.AZURE_KEYVAULT_NAME }}
  AZURE_TENANT_ID: ${{ secrets.AZURE_TENANT_ID }}
  AZURE_CLIENT_ID: ${{ secrets.AZURE_CLIENT_ID }}
  AZURE_CLIENT_SECRET: ${{ secrets.AZURE_CLIENT_SECRET }}
```

### Azure-hosted Applications

Enable Managed Identity on your Azure resource (App Service, Azure Functions, VM, etc.)

```env
USE_MANAGED_IDENTITY=true
```

## Secret Naming Convention

Secrets in Key Vault follow this pattern:

```
db-{environment}-{credential-type}
```

Examples:
- `db-dev-host`
- `db-dev-password`
- `db-qa-user`
- `db-prod-database`

## Testing the Setup

```javascript
// test-keyvault-connection.js
import keyVaultClient from './lib/azure/keyVaultClient.js';
import { ConnectDB, executeQuery, closePool } from './lib/database/dbHelper.js';

async function testConnection() {
  try {
    // Initialize Key Vault
    await keyVaultClient.initialize(process.env.AZURE_KEYVAULT_NAME);
    
    // List available secrets
    const secrets = await keyVaultClient.listSecretNames();
    console.log('Available secrets:', secrets);
    
    // Create database pool
    await ConnectDB('SureReturns');
    
    // Test query
    const result = await executeQuery('SureReturns', 'SELECT GETDATE() AS currentTime');
    console.log('Database connection successful!', result[0]);
    
    // Cleanup
    await closePool();
  } catch (error) {
    console.error('Error:', error.message);
  }
}

testConnection();
```

## For Azure SQL Database

This framework is already configured for Azure SQL Database.

1. Install the SQL Server driver:
```bash
npm install mssql
```

2. Ensure your Key Vault `ComplianceDBConnection` secret resolves to a valid SQL Server connection string.

3. Connection settings expected by the helper:
```javascript
const config = {
  server: dbConfig.host,
  database: dbConfig.database,
  user: dbConfig.user,
  password: dbConfig.password,
  port: dbConfig.port,
  options: {
    encrypt: true, // Required for Azure SQL
    trustServerCertificate: false
  }
};
```

## Security Best Practices

1. **Never commit `.env` file** - Add to `.gitignore`
2. **Use least privilege access** - Grant only required permissions
3. **Rotate secrets regularly** - Update passwords periodically
4. **Use Managed Identity in production** - Avoid storing credentials
5. **Enable Key Vault logging** - Monitor access for security
6. **Use separate Key Vaults** - Per environment for isolation

## Troubleshooting

### "Key Vault client not initialized"
- Ensure `keyVaultClient.initialize()` is called before accessing secrets

### "AZURE_KEYVAULT_NAME environment variable is not set"
- Check `.env` file exists and contains the Key Vault name

### "Access denied"
- Verify service principal or managed identity has "Get" and "List" permissions
- Check `az keyvault set-policy` was executed correctly

### "Secret not found"
- Verify secret naming convention matches your configuration
- Use `az keyvault secret list` to see available secrets

## Additional Resources

- [Azure Key Vault Documentation](https://docs.microsoft.com/azure/key-vault/)
- [Azure Identity SDK](https://docs.microsoft.com/javascript/api/@azure/identity)
- [Azure Key Vault Secrets SDK](https://docs.microsoft.com/javascript/api/@azure/keyvault-secrets)
