#!/usr/bin/env pwsh
# Azure Key Vault Setup Script for Database Credentials
# This script helps you store database credentials in Azure Key Vault

param(
    [Parameter(Mandatory=$true)]
    [string]$KeyVaultName,
    
    [Parameter(Mandatory=$true)]
    [ValidateSet('dev','qa','staging','prod')]
    [string]$Environment,
    
    [Parameter(Mandatory=$true)]
    [string]$DbHost,
    
    [Parameter(Mandatory=$false)]
    [int]$DbPort = 5432,
    
    [Parameter(Mandatory=$true)]
    [string]$DbName,
    
    [Parameter(Mandatory=$true)]
    [string]$DbUser,
    
    [Parameter(Mandatory=$true)]
    [string]$DbPassword
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Azure Key Vault Setup for Database" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Azure CLI is installed
Write-Host "Checking Azure CLI installation..." -ForegroundColor Yellow
try {
    az version | Out-Null
    Write-Host "✓ Azure CLI is installed" -ForegroundColor Green
} catch {
    Write-Host "✗ Azure CLI is not installed" -ForegroundColor Red
    Write-Host "Please install Azure CLI from: https://aka.ms/installazurecliwindows" -ForegroundColor Yellow
    exit 1
}

# Check if logged in to Azure
Write-Host "`nChecking Azure login status..." -ForegroundColor Yellow
$account = az account show 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Not logged in to Azure" -ForegroundColor Red
    Write-Host "Running 'az login'..." -ForegroundColor Yellow
    az login
    if ($LASTEXITCODE -ne 0) {
        Write-Host "✗ Failed to login to Azure" -ForegroundColor Red
        exit 1
    }
}
Write-Host "✓ Logged in to Azure" -ForegroundColor Green

# Create secrets in Key Vault
Write-Host "`nStoring database credentials in Key Vault: $KeyVaultName" -ForegroundColor Yellow
Write-Host "Environment: $Environment" -ForegroundColor Cyan

$secretPrefix = "db-$Environment"

# Store each credential
Write-Host "`nCreating secrets..." -ForegroundColor Yellow

Write-Host "  - ${secretPrefix}-host"
az keyvault secret set --vault-name $KeyVaultName --name "${secretPrefix}-host" --value $DbHost --output none
if ($LASTEXITCODE -eq 0) {
    Write-Host "    ✓ Stored successfully" -ForegroundColor Green
} else {
    Write-Host "    ✗ Failed to store" -ForegroundColor Red
    exit 1
}

Write-Host "  - ${secretPrefix}-port"
az keyvault secret set --vault-name $KeyVaultName --name "${secretPrefix}-port" --value $DbPort --output none
if ($LASTEXITCODE -eq 0) {
    Write-Host "    ✓ Stored successfully" -ForegroundColor Green
} else {
    Write-Host "    ✗ Failed to store" -ForegroundColor Red
    exit 1
}

Write-Host "  - ${secretPrefix}-database"
az keyvault secret set --vault-name $KeyVaultName --name "${secretPrefix}-database" --value $DbName --output none
if ($LASTEXITCODE -eq 0) {
    Write-Host "    ✓ Stored successfully" -ForegroundColor Green
} else {
    Write-Host "    ✗ Failed to store" -ForegroundColor Red
    exit 1
}

Write-Host "  - ${secretPrefix}-user"
az keyvault secret set --vault-name $KeyVaultName --name "${secretPrefix}-user" --value $DbUser --output none
if ($LASTEXITCODE -eq 0) {
    Write-Host "    ✓ Stored successfully" -ForegroundColor Green
} else {
    Write-Host "    ✗ Failed to store" -ForegroundColor Red
    exit 1
}

Write-Host "  - ${secretPrefix}-password"
az keyvault secret set --vault-name $KeyVaultName --name "${secretPrefix}-password" --value $DbPassword --output none
if ($LASTEXITCODE -eq 0) {
    Write-Host "    ✓ Stored successfully" -ForegroundColor Green
} else {
    Write-Host "    ✗ Failed to store" -ForegroundColor Red
    exit 1
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "✅ All credentials stored successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Update .env file with:" -ForegroundColor White
Write-Host "   AZURE_KEYVAULT_NAME=$KeyVaultName" -ForegroundColor Cyan
Write-Host "   ENV=$Environment" -ForegroundColor Cyan
Write-Host ""
Write-Host "2. Set up Azure authentication (choose one):" -ForegroundColor White
Write-Host "   a) Azure CLI: Run 'az login'" -ForegroundColor Cyan
Write-Host "   b) Service Principal: Set AZURE_TENANT_ID, AZURE_CLIENT_ID, AZURE_CLIENT_SECRET" -ForegroundColor Cyan
Write-Host ""
Write-Host "3. Test the connection:" -ForegroundColor White
Write-Host "   npm run test:keyvault" -ForegroundColor Cyan
Write-Host ""
