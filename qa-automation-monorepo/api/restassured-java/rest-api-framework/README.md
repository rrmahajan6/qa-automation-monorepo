# REST API Testing Framework

An **industry-standard REST API testing framework** built on REST Assured, TestNG, and Allure — designed for maintainability, scalability, and CI/CD integration.

---

## Tech Stack

| Concern            | Library / Tool              | Version  |
|--------------------|-----------------------------|----------|
| API Testing        | REST Assured                | 5.4.0    |
| Test Runner        | TestNG                      | 7.10.2   |
| Reporting          | Allure                      | 2.27.0   |
| JSON Handling      | Jackson Databind            | 2.17.1   |
| Logging            | Log4j2                      | 2.23.1   |
| Assertions         | AssertJ                     | 3.26.0   |
| Test Data          | JavaFaker                   | 1.0.2    |
| Config             | Owner                       | 1.0.12   |
| Schema Validation  | JSON Schema Validator        | 5.4.0    |
| Build              | Maven                       | 3.9+     |
| CI/CD              | GitHub Actions              | —        |

---

## Project Structure

```
rest-api-framework/
├── .github/workflows/ci.yml          # GitHub Actions pipeline
├── src/
│   ├── main/java/com/framework/
│   │   ├── config/
│   │   │   ├── ConfigManager.java    # Singleton config loader
│   │   │   └── EnvironmentConfig.java# Owner-based env config
│   │   ├── constants/
│   │   │   ├── APIEndpoints.java     # All API path constants
│   │   │   └── StatusCodes.java      # HTTP status code constants
│   │   ├── models/
│   │   │   ├── request/              # Request POJOs (Lombok + Jackson)
│   │   │   └── response/             # Response POJOs
│   │   ├── utils/
│   │   │   ├── SpecBuilder.java      # Reusable RequestSpecifications
│   │   │   ├── ResponseHelper.java   # Fluent response wrapper
│   │   │   ├── JsonUtils.java        # Jackson utilities
│   │   │   ├── SchemaValidator.java  # JSON Schema validation
│   │   │   └── TestDataFactory.java  # JavaFaker test data
│   │   └── reporting/
│   │       └── AllureLogger.java     # Allure attachment helpers
│   └── test/
│       ├── java/com/framework/
│       │   ├── base/BaseTest.java    # Suite setup / teardown
│       │   ├── listeners/
│       │   │   └── TestListener.java # TestNG + Allure listener
│       │   └── tests/
│       │       ├── UserTests.java    # Full CRUD tests for /users
│       │       └── PostTests.java    # Full CRUD + parameterised /posts
│       └── resources/
│           ├── config/               # Per-environment .properties
│           ├── schemas/              # JSON Schema files
│           ├── testdata/             # Static test data (JSON)
│           ├── testng.xml            # Test suite definition
│           └── log4j2.xml            # Logging config
└── pom.xml
```

---

## Quick Start

### Prerequisites

- Java 17 (required — set `JAVA_HOME` to Java 17, Lombok is incompatible with Java 25)
- Maven 3.9+

### Run all tests (dev environment)

```bash
# If multiple JDKs are installed, point to Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
mvn clean test
```

### Run smoke tests only

```bash
mvn clean test -Psmoke
```

### Run regression tests against staging

```bash
mvn clean test -Pregression -Denv=staging
```

### Generate and open Allure report

```bash
mvn allure:serve
```

---

## Key Design Patterns

### 1. Builder pattern — SpecBuilder

```java
// Default spec
RequestSpecification spec = SpecBuilder.defaultSpec();

// Authenticated spec
RequestSpecification authSpec = SpecBuilder.authSpec("my-token");
```

### 2. Fluent assertions — ResponseHelper

```java
new ResponseHelper(response)
    .assertStatusCode(200)
    .assertContentType("application/json")
    .assertResponseTimeLessThan(2000)
    .assertFieldEquals("name", "Leanne Graham");
```

### 3. Soft assertions

```java
SoftAssertions softly = new SoftAssertions();
softly.assertThat(user.getId()).isNotNull();
softly.assertThat(user.getName()).isNotBlank();
softly.assertAll(); // reports ALL failures at once
```

### 4. Data-driven tests with @DataProvider

```java
@Test(dataProvider = "validPostIds")
public void getPostById_shouldReturn200(int postId) { ... }

@DataProvider(name = "validPostIds")
public Object[][] validPostIds() {
    return new Object[][] {{1}, {5}, {10}};
}
```

### 5. JSON Schema validation

```java
SchemaValidator.validate(response, "user-schema.json");
```

### 6. Environment switching

```bash
# Switch environment at runtime — no code changes needed
mvn test -Denv=staging
```

---

## Allure Report Annotations

```java
@Epic("User Management")
@Feature("Users API")
@Story("Create user")
@Severity(SeverityLevel.CRITICAL)
@Description("Verify POST /users returns 201")
```

---

## CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/ci.yml`) provides:

- Triggers on push, PR, schedule (nightly), and manual dispatch
- Parameterised environment and suite selection
- Allure report generation and upload as artifacts
- Auto-publish Allure HTML to GitHub Pages on `main` branch
- Secure secret injection via `${{ secrets.API_AUTH_TOKEN }}`

---

## Adding a New Endpoint

1. Add the path constant to `APIEndpoints.java`
2. Create request/response POJOs in `models/`
3. Add a JSON schema to `src/test/resources/schemas/`
4. Create a test class extending `BaseTest`
5. Add the class to `testng.xml`
