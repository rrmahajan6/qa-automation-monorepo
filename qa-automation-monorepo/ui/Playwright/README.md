# Playwright Enterprise Automation Framework

## 🏗️ Architecture Overview

This is an enterprise-grade, scalable Playwright automation framework implementing industry best practices:

- ✅ **Page Object Model (POM)** with separate locators
- ✅ **Playwright's robust selectors** (getByRole, getByLabel)
- ✅ **Multi-environment support** (dev, qa, staging, prod)
- ✅ **Database integration** (Azure SQL / MSSQL with connection pooling)
- ✅ **BasePage wrapper methods** with logging and error handling
- ✅ **Custom fixtures** for enhanced testing
- ✅ **Global setup/teardown hooks**
- ✅ **Centralized logging and reporting**

---

## 📁 Framework Structure

```
playwright-automation-framework/
├── config/                      # Environment configurations
│   ├── environments/           # Env-specific configs (dev, qa, staging, prod)
│   ├── database.config.js     # Database configuration
│   └── test.config.js         # Test configuration manager
│
├── pages/                      # Page Object Model
│   ├── base/BasePage.js       # Base page with wrapper methods
│   ├── locators/              # Separate locator files (getByRole, getByLabel)
│   ├── login/LoginPage.js     # Login page object
│   ├── dashboard/DashboardPage.js
│   └── components/            # Reusable components
│
├── lib/                        # Core libraries
│   ├── database/              # Database connection & helpers
│   │   ├── dbHelper.js       # Connection pool + reusable DB operations
│   │   ├── KeyVaultSecrets.js# Key Vault secret retrieval
│   │   └── dbQueries.js      # Centralized SQL queries
│   ├── api/                   # API client (future)
│   └── dataProviders/         # CSV, Excel, JSON readers (future)
│
├── fixtures/                   # Playwright fixtures
│   └── testFixtures.js        # Custom fixtures (db, pages)
│
├── hooks/                      # Test hooks
│   ├── globalSetup.js         # Runs once before all tests
│   ├── globalTeardown.js      # Runs once after all tests
│   └── testHooks.js           # Before/after each test
│
├── utils/                      # Utilities
│   ├── logger.js              # Custom logger
│   └── helpers/               # Action, Wait, Assertion helpers
│
├── constants/                  # Constants
│   ├── timeouts.js
│   ├── messages.js
│   └── testData.js
│
├── tests/                      # Test suites
│   ├── e2e/smoke/             # Smoke tests
│   ├── e2e/regression/        # Regression tests
│   └── api/                   # API tests
│
├── data/                       # Test data
│   ├── testData/dev/
│   ├── testData/qa/
│   └── sql/                   # SQL query files
│
├── playwright.config.js        # Main Playwright config
└── package.json
```

---

## 🚀 Getting Started

### 1. Install Dependencies

```bash
yarn install
# or
npm install
```

### 2. Install Browsers

```bash
npx playwright install
```

### 3. Configure Environment

Set your environment (default is `qa`):

```bash
# Windows (PowerShell)
$env:ENV="dev"

# Linux/Mac
export ENV=dev
```

### 4. Update Configuration Files

Edit environment configs in `config/environments/`:
- Update URLs, credentials, and database settings
- Never commit real passwords (use environment variables)

---

## 🧪 Running Tests

### Run all tests
```bash
yarn test
```

### Run specific test suite
```bash
yarn test:smoke          # Smoke tests
yarn test:regression     # Regression tests
```

### Run on specific environment
```bash
yarn test:dev           # Development environment
yarn test:qa            # QA environment  
yarn test:staging       # Staging environment
```

### Run in headed mode
```bash
yarn test:headed        # See browser
```

### Debug mode
```bash
yarn debug             # Debug with Playwright Inspector
```

### UI Mode
```bash
yarn ui                # Interactive UI mode
```

---

## 📝 Writing Tests

### Example Test Using Fixtures

```javascript
import { test, expect } from '../../../fixtures/testFixtures.js';

test.describe('Login Tests', () => {
  test('should login successfully', async ({ loginPage, dashboardPage }) => {
    // Navigate and login
    await loginPage.goto();
    await loginPage.loginWithDefaultCredentials();
    
    // Verify dashboard
    expect(await dashboardPage.isLoaded()).toBe(true);
  });
});
```

### Example Test with Database

```javascript
test('should use database data', async ({ db, loginPage }) => {
  // Query database
  const user = await getUserByEmail('test@example.com');
  
  // Use data in test
  await loginPage.login(user.username, user.password);
});
```

### Example with Authenticated Page

```javascript
test('dashboard test', async ({ authenticatedPage, dashboardPage }) => {
  // Already logged in via fixture
  await dashboardPage.clickCreate();
});
```

---

## 🔧 Key Features

### 1. BasePage Wrapper Methods

All page objects inherit from `BasePage` which provides:
- Enhanced `click()` with auto-wait and error handling
- `fill()` with validation
- `fillSensitiveData()` for passwords (masked in logs)
- Automatic screenshot on error
- Custom wait strategies
- Logging for all actions

### 2. Separate Locators with Robust Selectors

```javascript
// pages/locators/login.locators.js
export class LoginLocators {
  get usernameInput() {
    return this.page.getByRole('textbox', { name: /username/i });
  }
  
  get loginButton() {
    return this.page.getByRole('button', { name: /log in/i });
  }
}
```

### 3. Multi-Environment Support

Switch environments via `ENV` variable:
```bash
ENV=qa yarn test        # QA environment
ENV=staging yarn test   # Staging environment
```

Each environment config includes:
- Base URLs and API endpoints
- Authentication credentials
- Database settings
- **Feature flags** for conditional testing

### 4. Feature Flags

Feature flags (feature toggles) allow you to control which features are enabled/disabled in different environments.

**Purpose:**
- Enable/disable features per environment (beta in QA, stable in prod)
- Test different feature configurations without code changes
- Conditional test execution based on features

**Configuration Example:**
```javascript
// config/environments/qa.config.js
features: {
  enableNewUI: true,
  enableBetaFeatures: false,
}
```

**Usage in Tests:**
```javascript
import { getConfig } from '../../config/test.config.js';

const config = getConfig();

test('new UI test', async ({ page }) => {
  if (config.features.enableNewUI) {
    // Test new UI
    await page.getByRole('button', { name: 'New Dashboard' }).click();
  } else {
    // Test old UI
    await page.locator('#old-dashboard-button').click();
  }
});

// Skip test if feature not enabled
test.skip(!config.features.enableBetaFeatures, 'beta feature test', async () => {
  // Only runs if beta features enabled
});
```

### 5. Database Integration

- Connection pooling per worker
- Global setup/teardown for data seeding/cleanup
- Database fixture available in all tests

### 6. Custom Fixtures

- `db` - Database connection
- `loginPage` - Login page object
- `dashboardPage` - Dashboard page object
- `authenticatedPage` - Pre-authenticated page

---

## 📊 Reports

### HTML Report
```bash
yarn report
```

Reports are generated in `playwright-report/`

### JSON & JUnit Reports

Available in `test-results/` for CI/CD integration

---

## 🗄️ Database Setup

1. Update database credentials in `config/environments/*.config.js`
2. Install SQL Server driver (already in package.json):
   ```bash
  yarn add mssql
   ```
3. Keep Key Vault secret `ComplianceDBConnection` configured for your target environment

---

## 🔐 Best Practices

1. **Never hardcode credentials** - Use environment variables
2. **Use getByRole/getByLabel** for robust locators
3. **Separate locators from page objects**
4. **Log all actions** via BasePage wrapper methods
5. **Take screenshots on failure** (automatic)
6. **Clean up test data** in global teardown
7. **Use fixtures** for common setup
8. **Keep tests independent** - don't rely on test order

---

## 🐛 Debugging

### Debug specific test
```bash
yarn debug tests/e2e/smoke/login.spec.js
```

### View trace
```bash
npx playwright show-trace trace.zip
```

### Logs
Check `logs/` directory for detailed execution logs

---

## 📦 CI/CD Integration

Example GitHub Actions:

```yaml
- name: Run tests
  run: ENV=qa yarn test
  
- name: Upload report
  uses: actions/upload-artifact@v3
  with:
    name: playwright-report
    path: playwright-report/
```

---

## 🛠️ Customization

### Add New Page Object

1. Create locators: `pages/locators/mypage.locators.js`
2. Create page object: `pages/mypage/MyPage.js` (extends BasePage)
3. Add fixture in `fixtures/testFixtures.js`
4. Use in tests

### Add New Environment

1. Create config: `config/environments/newenv.config.js`
2. Add to configs object in `test.config.js`
3. Run: `ENV=newenv yarn test`

---

## 📚 Resources

- [Playwright Documentation](https://playwright.dev)
- [Page Object Model](https://playwright.dev/docs/pom)
- [Best Practices](https://playwright.dev/docs/best-practices)

---

## 👨‍💻 Framework Maintainer

For questions or support, contact the automation team.

**Version:** 1.0.0  
**Last Updated:** January 2026
