# Azure Key Vault Quick Start Guide

## Quick Setup (5 minutes)

### 1. Install Dependencies
Already done! ✓
- `@azure/keyvault-secrets`
- `@azure/identity`

### 2. Login to Azure
```bash
az login
```

### 3. Store Database Credentials in Key Vault

#### Option A: Using PowerShell Script (Recommended)
```powershell
.\scripts\setup-keyvault-secrets.ps1 `
  -KeyVaultName "your-keyvault-name" `
  -Environment "dev" `
  -DbHost "your-server.database.windows.net" `
  -DbPort 1433 `
  -DbName "your-database" `
  -DbUser "your-username" `
  -DbPassword "your-password"
```

#### Option B: Using Azure CLI Commands
```bash
# For dev environment
az keyvault secret set --vault-name your-keyvault-name --name db-dev-host --value "your-server.database.windows.net"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-port --value "1433"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-database --value "your-database"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-user --value "your-username"
az keyvault secret set --vault-name your-keyvault-name --name db-dev-password --value "your-password"
```

### 4. Configure Environment Variables

Copy `.env.example` to `.env` and update:

```env
# Required
AZURE_KEYVAULT_NAME=your-keyvault-name
ENV=dev

# For local development (if not using Azure CLI)
AZURE_TENANT_ID=your-tenant-id
AZURE_CLIENT_ID=your-client-id
AZURE_CLIENT_SECRET=your-client-secret

# For Azure SQL
DB_SSL=true
```

### 5. Test the Connection
```bash
npm run test:keyvault
```

## Usage in Tests

```javascript
import { ConnectDB, executeQuery, closePool } from './lib/database/dbHelper.js';

// In test setup
await ConnectDB('SureReturns');

// Execute queries
const result = await executeQuery('SureReturns', 'SELECT TOP 1 * FROM taxForm');

// In test teardown
await closePool();
```

## Files Created

1. **[lib/azure/keyVaultClient.js](lib/azure/keyVaultClient.js)** - Azure Key Vault client
2. **[lib/database/dbHelper.js](lib/database/dbHelper.js)** - Database connection/query helper with Key Vault secret
3. **[config/azureKeyVault.config.js](config/azureKeyVault.config.js)** - Key Vault configuration
4. **[test-keyvault-connection.js](test-keyvault-connection.js)** - Test script
5. **[tests/examples/database-keyvault.spec.js](tests/examples/database-keyvault.spec.js)** - Example tests
6. **[scripts/setup-keyvault-secrets.ps1](scripts/setup-keyvault-secrets.ps1)** - Setup script

## Authentication Options

### Local Development
**Option 1: Azure CLI (Easiest)**
```bash
az login
```

**Option 2: Service Principal**
Create `.env` with:
```env
AZURE_TENANT_ID=your-tenant-id
AZURE_CLIENT_ID=your-client-id
AZURE_CLIENT_SECRET=your-client-secret
```

### CI/CD Pipeline
Store as pipeline secrets:
- `AZURE_KEYVAULT_NAME`
- `AZURE_TENANT_ID`
- `AZURE_CLIENT_ID`
- `AZURE_CLIENT_SECRET`

### Azure-hosted Apps
Use Managed Identity - no credentials needed!

## Database Support

### Azure SQL Server (Current)
Already configured ✓

### MySQL
1. Install driver:
```bash
npm install mysql2
```

2. Add a dedicated MySQL helper module (separate from current SQL Server helper)

## Troubleshooting

**Error: "AZURE_KEYVAULT_NAME environment variable is not set"**
- Create `.env` file from `.env.example`
- Set `AZURE_KEYVAULT_NAME=your-keyvault-name`

**Error: "Access denied"**
- Run: `az keyvault set-policy --name your-keyvault-name --upn your-email@domain.com --secret-permissions get list`

**Error: "Secret not found"**
- Verify secrets exist: `az keyvault secret list --vault-name your-keyvault-name`
- Check naming: secrets should be `db-{env}-{credential}` (e.g., `db-dev-host`)

## Security Best Practices

✓ Never commit `.env` file  
✓ Use Managed Identity in production  
✓ Rotate secrets regularly  
✓ Grant least privilege access  
✓ Enable Key Vault logging  

## Complete Setup Documentation

See [README_AZURE_KEYVAULT_SETUP.md](README_AZURE_KEYVAULT_SETUP.md) for detailed documentation.
