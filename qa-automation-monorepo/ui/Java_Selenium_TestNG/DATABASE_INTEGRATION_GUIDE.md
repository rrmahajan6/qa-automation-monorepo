# Database Integration Guide

## Overview
This framework now supports database integration for cleaning test data before execution and verifying purchase records after tests complete.

## Configuration

### 1. Enable Database Integration
Edit `src/test/resources/application.properties`:

```properties
# ============= DATABASE CONFIGURATION =============
db.enabled=true
db.url=jdbc:mysql://localhost:3306/automation_db
db.username=root
db.password=yourpassword
db.connection.timeout.seconds=10
```

### 2. Required Database Schema
Create the following tables in your MySQL database:

```sql
-- Orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_email VARCHAR(255) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2),
    status VARCHAR(50)
);

-- Order items table
CREATE TABLE order_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT DEFAULT 1,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

**Note:** If your schema differs, update the SQL queries in `PurchaseDataUtil.java` accordingly.

## Features Implemented

### 1. Automatic Cleanup Before Test Execution
- **Where:** `BaseTest.beforeSuite()` method
- **What:** Deletes all records from `orders` and `order_items` tables
- **When:** Once before the entire test suite runs
- **Condition:** Only runs if `db.enabled=true`

### 2. Purchase Verification in Tests
- **Where:** All test methods in `PurchaseProduct.java`
- **What:** Verifies that purchase records exist in the database after checkout
- **How:** Uses `PurchaseDataUtil.verifyPurchaseExists(productName, customerEmail)`
- **Output:** Logs purchase details to console and test reports

## Available Utility Methods

### PurchaseDataUtil Class

| Method | Description | Usage |
|--------|-------------|-------|
| `cleanAllPurchaseData()` | Deletes all orders and order items | Called automatically in `@BeforeSuite` |
| `cleanPurchaseDataByCustomer(email)` | Deletes orders for specific customer | Manual cleanup per customer |
| `verifyPurchaseExists(product, email)` | Checks if purchase record exists | Returns `true/false` |
| `getPurchaseDetails(product, email)` | Retrieves purchase records | Returns `List<Map<String, Object>>` |
| `getAllPurchasesByCustomer(email)` | Gets all purchases for a customer | Returns `List<Map<String, Object>>` |
| `getTotalOrderCount()` | Counts total orders in DB | Returns `int` |
| `printPurchaseDetails(product, email)` | Logs purchase details to console | Debugging helper |

## Usage Examples

### Example 1: Verify Purchase in Test
```java
@Test
public void testPurchaseProduct() {
    String customerEmail = "test@example.com";
    String productName = "ADIDAS ORIGINAL";
    
    // ... perform purchase actions ...
    
    // Verify in database
    if (GlobalConfig.isDbEnabled()) {
        boolean exists = PurchaseDataUtil.verifyPurchaseExists(productName, customerEmail);
        Assert.assertTrue(exists, "Purchase should exist in database");
    }
}
```

### Example 2: Get Purchase Details
```java
List<Map<String, Object>> purchases = 
    PurchaseDataUtil.getPurchaseDetails("ZARA COAT 3", "test@example.com");

for (Map<String, Object> purchase : purchases) {
    System.out.println("Order ID: " + purchase.get("order_id"));
    System.out.println("Product: " + purchase.get("product_name"));
    System.out.println("Price: " + purchase.get("price"));
}
```

### Example 3: Custom Cleanup
```java
@BeforeClass
public void setupClass() {
    // Clean only specific customer's data
    PurchaseDataUtil.cleanPurchaseDataByCustomer("test@example.com");
}
```

## Running Tests

### With Database Integration Enabled
```bash
mvn clean test -Ddb.enabled=true
```

### With Custom Database Properties
```bash
mvn clean test -DconfigPath=src/test/resources/prod.properties
```

### Without Database Integration
```bash
mvn clean test -Ddb.enabled=false
```
Or simply set `db.enabled=false` in `application.properties`.

## Troubleshooting

### Issue: "Database integration is disabled"
**Solution:** Set `db.enabled=true` in `application.properties`

### Issue: "MySQL JDBC driver not found"
**Solution:** Ensure MySQL connector dependency is in `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Issue: "Table doesn't exist" error
**Solution:** 
1. Create the required tables using the SQL schema above
2. Or update table names in `PurchaseDataUtil.java` to match your schema

### Issue: Connection timeout
**Solution:** 
1. Verify MySQL is running
2. Check connection details in `application.properties`
3. Increase `db.connection.timeout.seconds` value

## Important Notes

1. **Data Cleanup:** All purchase data is deleted before each test suite execution. Ensure you're not using a production database.

2. **Schema Flexibility:** The default table names are `orders` and `order_items`. If your schema differs, update the SQL queries in `PurchaseDataUtil.java`.

3. **Thread Safety:** Database operations are thread-safe and support parallel test execution.

4. **Optional Feature:** Database verification is optional and only runs when `db.enabled=true`. Tests will pass/fail based on UI actions alone if DB is disabled.

5. **Logging:** All database operations are logged for debugging. Check logs in `logs/` directory.

## Files Modified

- `src/main/java/framework/utils/db/PurchaseDataUtil.java` (NEW)
- `src/main/java/framework/base/BaseTest.java` (UPDATED)
- `src/test/java/tests/PurchaseProduct.java` (UPDATED)
- `src/test/resources/application.properties` (CONFIG)

---

**Created:** March 9, 2026  
**Framework Version:** 1.0
