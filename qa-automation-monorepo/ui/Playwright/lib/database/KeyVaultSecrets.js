import { SecretClient } from "@azure/keyvault-secrets";
import { DefaultAzureCredential, ClientSecretCredential, AzureCliCredential } from "@azure/identity";

class KeyVaultSecrets {
  constructor(keyVaultUrl) {
    this.keyVaultUrl = keyVaultUrl || process.env.AZURE_KEYVAULT_URL;
    this.credential = this.getCredential();
    this.client = new SecretClient(this.keyVaultUrl, this.credential);
  }
  getCredential() {
    // Option 1: Service Principal (best for CI/CD and local dev)
    if (process.env.AZURE_CLIENT_ID && process.env.AZURE_CLIENT_SECRET && process.env.AZURE_TENANT_ID) {
      console.log("Using Service Principal authentication");
      return new ClientSecretCredential(
        process.env.AZURE_TENANT_ID,
        process.env.AZURE_CLIENT_ID,
        process.env.AZURE_CLIENT_SECRET
      );
    }

    // Option 2: Azure CLI (for local development)
    try {
      console.log("Using Azure CLI authentication - ensure you're logged in with 'az login'");
      return new AzureCliCredential();
    } catch (error) {
      console.warn("Azure CLI not available, falling back to DefaultAzureCredential");
    }
      // Option 3: Default (tries multiple methods but excludes ManagedIdentity for local)
    return new DefaultAzureCredential({
      excludeCredentials: ['ManagedIdentityCredential'] // Exclude since we're running locally
    });
}
async getSecret(secretName, printToConsole = true) {
    try {
      console.log(`Attempting to retrieve secret: ${secretName}`);
      const secret = await this.client.getSecret(secretName);
      console.log("Secret retrieved successfully");

      if (printToConsole) {
      console.log(`Secret '${secretName}' value:`, secret.value);
    }

      // return secret.value;
    } catch (error) {
      console.error(`Error retrieving secret ${secretName}:`, error.message);
      
      // Provide helpful troubleshooting
      if (error.message.includes('ManagedIdentityCredential')) {
        console.error('Managed Identity is not available locally. Please use one of these options:');
        console.error('1. Run: az login');
        console.error('2. Set environment variables: AZURE_CLIENT_ID, AZURE_CLIENT_SECRET, AZURE_TENANT_ID');
      }
      
      if (error.message.includes('authentication failed')) {
        console.error('Authentication failed. Try: az login');
      }
      throw error;
    }
  }
}

export default KeyVaultSecrets;