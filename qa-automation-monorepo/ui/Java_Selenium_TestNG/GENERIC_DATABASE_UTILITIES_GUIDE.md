# Generic Database Utilities Guide

## Overview
This framework provides generic, reusable database utilities for:
- **Data Cleanup**: Configurable cleanup of multiple tables before test execution
- **SQL Execution**: Generic methods for any SELECT/INSERT/UPDATE/DELETE operations
- **Test Validation**: Flexible validation methods usable in any test case

## Quick Start

### 1. Enable Database Integration
Edit [src/test/resources/application.properties](src/test/resources/application.properties):

```properties
db.enabled=true
db.url=jdbc:mysql://localhost:3306/automation_db
db.username=root
db.password=yourpassword

# Configure tables to clean (in order - child tables first)
db.cleanup.tables=order_items,orders,cart_items,cart,users,products
```

### 2. Automatic Cleanup
Tables listed in `db.cleanup.tables` are automatically cleaned before the test suite runs via `BaseTest.beforeSuite()`.

### 3. Use Generic SQL Methods
```java
// Check if a record exists
boolean exists = DatabaseUtil.recordExists("users", "email = ?", "test@example.com");

// Get record count
int count = DatabaseUtil.getRecordCount("orders", "customer_email = ?", "test@example.com");

// Execute custom query
List<Map<String, Object>> results = DatabaseUtil.executeQuery(
    "SELECT * FROM orders WHERE customer_email = ?", 
    "test@example.com"
);
```

---

## DataCleanupUtil - Generic Data Cleanup

### Configurable Cleanup (Recommended)

**Clean all tables from properties:**
```java
DataCleanupUtil.cleanAllConfiguredTables();
```

**Clean specific tables:**
```java
// Clean in specified order (respects foreign keys)
DataCleanupUtil.cleanTables("order_items", "orders", "users");
```

**Clean with conditions:**
```java
// Clean only test data
DataCleanupUtil.cleanTableWithCondition("users", "email LIKE ?", "%@test.com");
DataCleanupUtil.cleanTableWithCondition("orders", "created_date < ?", "2026-01-01");
```

### Advanced Cleanup

**Truncate tables (faster, but no foreign key support):**
```java
DataCleanupUtil.truncateTable("temp_data");
DataCleanupUtil.truncateTables("temp1", "temp2", "temp3");
```

**Clean with foreign key constraints disabled:**
```java
// Temporarily disable FK checks, clean all, re-enable
DataCleanupUtil.cleanAllTablesIgnoringConstraints();

// Or manually control:
DataCleanupUtil.disableForeignKeyChecks();
// ... your cleanup code ...
DataCleanupUtil.enableForeignKeyChecks();
```

**Verify cleanup success:**
```java
DataCleanupUtil.cleanAllConfiguredTables();
boolean success = DataCleanupUtil.verifyCleanupSuccess();
Assert.assertTrue(success, "All tables should be empty after cleanup");
```

**Get table row counts:**
```java
int orderCount = DataCleanupUtil.getTableRowCount("orders");
System.out.println("Orders in DB: " + orderCount);
```

---

## DatabaseUtil - Generic SQL Operations

### Query Operations

**Execute any SELECT query:**
```java
String sql = "SELECT user_id, email, created_date FROM users WHERE status = ?";
List<Map<String, Object>> users = DatabaseUtil.executeQuery(sql, "active");

for (Map<String, Object> user : users) {
    System.out.println("Email: " + user.get("email"));
    System.out.println("Created: " + user.get("created_date"));
}
```

**Fetch single value:**
```java
// Get total order amount
Object total = DatabaseUtil.fetchSingleValue(
    "SELECT SUM(total_amount) FROM orders WHERE customer_email = ?",
    "customer@example.com"
);
```

**Fetch single row:**
```java
Map<String, Object> user = DatabaseUtil.fetchSingleRow(
    "SELECT * FROM users WHERE email = ?",
    "test@example.com"
);
if (user != null) {
    String name = (String) user.get("name");
    Integer age = (Integer) user.get("age");
}
```

**Fetch specific column values:**
```java
// Get all product names for an order
List<Object> products = DatabaseUtil.fetchColumnValues(
    "SELECT product_name FROM order_items WHERE order_id = ?",
    "product_name",
    orderId
);
```

**Fetch column value from single row:**
```java
Object email = DatabaseUtil.fetchColumnValue(
    "SELECT email FROM users WHERE user_id = ?",
    "email",
    userId
);
```

### Insert Operations

**Insert a record:**
```java
Map<String, Object> newUser = new HashMap<>();
newUser.put("email", "new@example.com");
newUser.put("name", "John Doe");
newUser.put("status", "active");
newUser.put("created_date", new Date());

int inserted = DatabaseUtil.insertRecord("users", newUser);
// Returns 1 if successful
```

### Update Operations

**Update records:**
```java
Map<String, Object> updates = new HashMap<>();
updates.put("status", "inactive");
updates.put("updated_date", new Date());

int updated = DatabaseUtil.updateRecord(
    "users",
    updates,
    "email = ?",
    "old@example.com"
);
System.out.println(updated + " record(s) updated");
```

**Execute custom UPDATE:**
```java
int affected = DatabaseUtil.executeUpdate(
    "UPDATE products SET stock = stock - ? WHERE product_id = ?",
    5, productId
);
```

### Delete Operations

**Delete specific records:**
```java
int deleted = DatabaseUtil.deleteRecords(
    "orders",
    "status = ? AND created_date < ?",
    "cancelled", "2025-01-01"
);
```

### Validation Methods

**Check if record exists:**
```java
boolean exists = DatabaseUtil.recordExists(
    "orders",
    "customer_email = ? AND product_id = ?",
    "test@example.com", 12345
);
Assert.assertTrue(exists, "Order should exist");
```

**Get record count:**
```java
int pendingOrders = DatabaseUtil.getRecordCount(
    "orders",
    "status = ?",
    "pending"
);
Assert.assertEquals(pendingOrders, 3, "Should have 3 pending orders");
```

**Get total table count:**
```java
int totalUsers = DatabaseUtil.getTableCount("users");
```

**Assert record exists (throws exception if not found):**
```java
// Will throw AssertionError if record doesn't exist
DatabaseUtil.assertRecordExists(
    "users",
    "email = ?",
    "required@example.com"
);
```

**Assert record does NOT exist:**
```java
// Will throw AssertionError if record exists
DatabaseUtil.assertRecordNotExists(
    "users",
    "email = ?",
    "deleted@example.com"
);
```

### Debugging

**Print query results:**
```java
// Useful for debugging - prints all results to logs
DatabaseUtil.printQueryResults(
    "SELECT * FROM orders WHERE customer_email = ?",
    "test@example.com"
);
```

### Batch Operations

**Execute multiple statements:**
```java
List<String> statements = Arrays.asList(
    "UPDATE products SET stock = 0 WHERE discontinued = 1",
    "DELETE FROM cart_items WHERE created_date < '2025-01-01'",
    "UPDATE users SET last_login = NULL WHERE status = 'inactive'"
);

int[] results = DatabaseUtil.executeBatch(statements);
// Results array contains affected row counts
```

---

## Usage Examples in Tests

### Example 1: Verify Product Added to Cart
```java
@Test
public void testAddToCart() {
    // ... UI actions to add product to cart ...
    
    if (GlobalConfig.isDbEnabled()) {
        // Verify cart item exists in database
        boolean inCart = DatabaseUtil.recordExists(
            "cart_items",
            "user_id = ? AND product_id = ?",
            userId, productId
        );
        Assert.assertTrue(inCart, "Product should be in cart");
        
        // Get cart item details
        Map<String, Object> cartItem = DatabaseUtil.fetchSingleRow(
            "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?",
            userId, productId
        );
        assertEquals(cartItem.get("quantity"), 1);
    }
}
```

### Example 2: Verify Order Total
```java
@Test
public void testOrderTotal() {
    // ... complete purchase ...
    
    if (GlobalConfig.isDbEnabled()) {
        // Get order total from database
        Object dbTotal = DatabaseUtil.fetchSingleValue(
            "SELECT total_amount FROM orders WHERE order_id = ?",
            orderId
        );
        
        // Compare with UI total
        Assert.assertEquals(dbTotal, uiTotal, "Order totals should match");
    }
}
```

### Example 3: Verify User Registration
```java
@Test
public void testUserRegistration() {
    String email = "newuser@example.com";
    
    // ... register user in UI ...
    
    if (GlobalConfig.isDbEnabled()) {
        // Check user exists
        DatabaseUtil.assertRecordExists("users", "email = ?", email);
        
        // Verify user details
        Map<String, Object> user = DatabaseUtil.fetchSingleRow(
            "SELECT * FROM users WHERE email = ?",
            email
        );
        Assert.assertEquals(user.get("status"), "active");
        Assert.assertNotNull(user.get("created_date"));
    }
}
```

### Example 4: Clean Specific Test Data
```java
@BeforeClass
public void setupTestData() {
    if (GlobalConfig.isDbEnabled()) {
        // Clean only test user's data
        DataCleanupUtil.cleanTableWithCondition(
            "users",
            "email LIKE ?",
            "%@test-automation.com"
        );
    }
}
```

### Example 5: Verify Multiple Products
```java
@Test
public void testMultiProductPurchase() {
    List<String> products = Arrays.asList("Product A", "Product B", "Product C");
    
    // ... purchase multiple products ...
    
    if (GlobalConfig.isDbEnabled()) {
        // Get all purchased products from DB
        List<Object> dbProducts = DatabaseUtil.fetchColumnValues(
            "SELECT oi.product_name FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "WHERE o.order_id = ?",
            "product_name",
            orderId
        );
        
        // Verify all products are in DB
        Assert.assertEquals(dbProducts.size(), products.size());
        Assert.assertTrue(dbProducts.containsAll(products));
    }
}
```

### Example 6: Custom Join Queries
```java
@Test
public void testUserOrderHistory() {
    if (GlobalConfig.isDbEnabled()) {
        String sql = "SELECT u.name, o.order_id, o.total_amount, oi.product_name " +
                    "FROM users u " +
                    "JOIN orders o ON u.user_id = o.user_id " +
                    "JOIN order_items oi ON o.order_id = oi.order_id " +
                    "WHERE u.email = ?";
        
        List<Map<String, Object>> orderHistory = DatabaseUtil.executeQuery(sql, userEmail);
        
        Assert.assertFalse(orderHistory.isEmpty(), "User should have order history");
        
        for (Map<String, Object> row : orderHistory) {
            LoggerUtil.info(this.getClass(), 
                "Order: " + row.get("order_id") + 
                ", Product: " + row.get("product_name") +
                ", Amount: " + row.get("total_amount"));
        }
    }
}
```

---

## Configuration Reference

### application.properties Settings

```properties
# Enable/disable database integration
db.enabled=true

# Database connection
db.url=jdbc:mysql://localhost:3306/automation_db
db.username=root
db.password=yourpassword
db.connection.timeout.seconds=10

# Tables to clean before test execution
# Order is important: child tables first (to respect foreign keys)
db.cleanup.tables=order_items,orders,cart_items,cart,users,products,categories
```

### Environment-Specific Configurations

You can maintain different property files:
- `application.properties` - Default/Dev
- `staging.properties` - Staging environment  
- `prod.properties` - Production (use with caution!)

Run with specific config:
```bash
mvn clean test -DconfigPath=src/test/resources/staging.properties
```

---

## Best Practices

### 1. **Always Use Parameterized Queries**
```java
// ✅ GOOD - Safe from SQL injection
DatabaseUtil.executeQuery("SELECT * FROM users WHERE email = ?", userEmail);

// ❌ BAD - SQL injection risk
DatabaseUtil.executeQuery("SELECT * FROM users WHERE email = '" + userEmail + "'");
```

### 2. **Handle Nullable Results**
```java
Object result = DatabaseUtil.fetchSingleValue("SELECT name FROM users WHERE id = ?", userId);
if (result != null) {
    String name = (String) result;
}
```

### 3. **Use Appropriate Cleanup Methods**
```java
// For tables with foreign keys - clean children first
DataCleanupUtil.cleanTables("order_items", "orders");

// Or disable FK checks temporarily
DataCleanupUtil.cleanAllTablesIgnoringConstraints();
```

### 4. **Verify Cleanup**
```java
DataCleanupUtil.cleanAllConfiguredTables();
Assert.assertTrue(DataCleanupUtil.verifyCleanupSuccess());
```

### 5. **Log Important DB Operations**
```java
LoggerUtil.info(getClass(), "Verifying order in database...");
boolean exists = DatabaseUtil.recordExists("orders", "order_id = ?", orderId);
LoggerUtil.info(getClass(), "Order exists: " + exists);
```

---

## Troubleshooting

### Issue: "Table doesn't exist"
- Ensure table name is correct (case-sensitive in some databases)
- Verify database connection is to correct schema

### Issue: Foreign key constraint errors
```java
// Use this instead of regular cleanup:
DataCleanupUtil.cleanAllTablesIgnoringConstraints();
```

### Issue: No tables configured warning
- Add `db.cleanup.tables` property with comma-separated table names

### Issue: Cleanup not happening
- Verify `db.enabled=true` in properties
- Check logs for cleanup messages in `@BeforeSuite`

---

## Framework Files

### Core Utilities
- [DatabaseManager.java](src/main/java/framework/utils/db/DatabaseManager.java) - Connection management
- [DatabaseUtil.java](src/main/java/framework/utils/db/DatabaseUtil.java) - Generic SQL operations
- [DataCleanupUtil.java](src/main/java/framework/utils/db/DataCleanupUtil.java) - Data cleanup utilities
- [DatabaseException.java](src/main/java/framework/utils/db/DatabaseException.java) - Custom exception

### Configuration
- [application.properties](src/test/resources/application.properties) - DB config & table list
- [BaseTest.java](src/main/java/framework/base/BaseTest.java) - Auto-cleanup in `@BeforeSuite`

### Example Usage
- [PurchaseProduct.java](src/test/java/tests/PurchaseProduct.java) - Test examples with DB validation

---

**Created:** March 9, 2026  
**Framework Version:** 2.0 (Generic SQL Utilities)
