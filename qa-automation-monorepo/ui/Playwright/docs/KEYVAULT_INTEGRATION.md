# Azure Key Vault Integration for Database Connection

## Overview
Database credentials are now fetched securely from Azure Key Vault instead of hardcoded config files.

## Setup Instructions

### 1. Configure Environment Variables

Create a `.env` file in the project root:

```bash
# Enable Key Vault
USE_KEYVAULT=true
AZURE_KEYVAULT_NAME=your-keyvault-name

# Azure Service Principal (Option 1)
AZURE_TENANT_ID=your-tenant-id
AZURE_CLIENT_ID=your-client-id
AZURE_CLIENT_SECRET=your-client-secret

# OR use Azure CLI (Option 2)
# Simply run: az login
```

### 2. Store Secrets in Key Vault

Secrets must follow this naming convention: `{ENV}-db-{property}`

#### For Dev Environment:
```bash
az keyvault secret set --vault-name your-keyvault-name --name "dev-db-host" --value "eusdevstx.database.windows.net"
az keyvault secret set --vault-name your-keyvault-name --name "dev-db-database" --value "DEV-SureReturns"
az keyvault secret set --vault-name your-keyvault-name --name "dev-db-user" --value "srsql"
az keyvault secret set --vault-name your-keyvault-name --name "dev-db-password" --value "YourSecurePassword"
az keyvault secret set --vault-name your-keyvault-name --name "dev-db-port" --value "1433"
```

#### For QA Environment:
```bash
az keyvault secret set --vault-name your-keyvault-name --name "qa-db-host" --value "qa-server.database.windows.net"
az keyvault secret set --vault-name your-keyvault-name --name "qa-db-database" --value "QA-Database"
az keyvault secret set --vault-name your-keyvault-name --name "qa-db-user" --value "qauser"
az keyvault secret set --vault-name your-keyvault-name --name "qa-db-password" --value "YourSecurePassword"
az keyvault secret set --vault-name your-keyvault-name --name "qa-db-port" --value "1433"
```

### 3. How It Works

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Execution Flow                      │
└─────────────────────────────────────────────────────────────┘

1. Global Setup / Worker Fixture
   ├─ Check USE_KEYVAULT environment variable
   │
   ├─ If USE_KEYVAULT=true:
   │  ├─ Initialize Key Vault client
   │  ├─ Authenticate (Service Principal or Azure CLI)
   │  ├─ Fetch secrets: {ENV}-db-host, {ENV}-db-user, etc.
   │  └─ Create connection pool with Key Vault credentials
   │
   └─ If USE_KEYVAULT=false:
      └─ Use hardcoded credentials from config files
```

### 4. Fallback Behavior

If Key Vault is disabled or unavailable:
- Set `USE_KEYVAULT=false` in environment
- Application falls back to credentials in `config/environments/*.config.js`
- Useful for local development without Azure access

### 5. Authentication Options

#### Option 1: Service Principal (CI/CD)
```bash
export AZURE_TENANT_ID="your-tenant-id"
export AZURE_CLIENT_ID="your-client-id"
export AZURE_CLIENT_SECRET="your-client-secret"
```

#### Option 2: Azure CLI (Local Development)
```bash
az login
az account set --subscription "your-subscription-id"
```

#### Option 3: Managed Identity (Azure Hosted)
No configuration needed - automatically uses the managed identity of the Azure resource.

### 6. Benefits

✅ **Security**: Credentials never stored in code or config files  
✅ **Centralized**: Single source of truth for all environments  
✅ **Rotation**: Update secrets in Key Vault without code changes  
✅ **Audit**: Track who accessed which secrets  
✅ **Caching**: Secrets are cached per worker to minimize Key Vault calls  

### 7. Troubleshooting

**Error: "Key Vault client not initialized"**
- Ensure `AZURE_KEYVAULT_NAME` is set
- Verify Key Vault exists and is accessible

**Error: "Failed to retrieve secret"**
- Check secret naming convention: `{ENV}-db-host`
- Verify your Azure credentials have "Get" permission on secrets
- Ensure the secret exists in Key Vault

**Error: "Authentication failed"**
- If using Service Principal, verify all 3 variables are set
- If using Azure CLI, run `az login` and set subscription
- Check your user/service principal has Key Vault access

### 8. Security Best Practices

1. **Never commit credentials** to git
2. Add `.env` to `.gitignore`
3. Use Key Vault access policies or RBAC
4. Rotate secrets regularly
5. Use separate Key Vaults per environment
6. Enable Key Vault audit logging

### 9. Running Tests

```bash
# With Key Vault
USE_KEYVAULT=true ENV=dev npx playwright test

# Without Key Vault (fallback)
USE_KEYVAULT=false ENV=dev npx playwright test
```
