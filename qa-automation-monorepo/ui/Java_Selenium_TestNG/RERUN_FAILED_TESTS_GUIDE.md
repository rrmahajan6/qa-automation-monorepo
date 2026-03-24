# Rerunning Failed Tests Guide

This guide explains how to rerun failed TestNG tests using `testng-failed.xml`.

## 📌 How It Works

When TestNG runs tests, it automatically generates a `testng-failed.xml` file in `target/surefire-reports/` containing **only the failed test cases**. You can use this file to rerun just the failed tests without running the entire suite.

---

## 🚀 3 Methods to Rerun Failed Tests

### **Method 1: Using the Convenience Script (Easiest)**

```bash
./rerun-failed-tests.sh
```

That's it! The script automatically:
- Checks if `testng-failed.xml` exists
- Sets up Java 21
- Runs only the failed tests
- Reports results

---

### **Method 2: Using Maven Profile**

```bash
# First run: Execute all tests
mvn clean test

# Rerun: Execute only failed tests
mvn test -Prerun-failed
```

The `-Prerun-failed` profile is configured in `pom.xml` to use `testng-failed.xml` automatically.

---

### **Method 3: Direct Maven Command**

```bash
# First run: Execute all tests
mvn clean test

# Rerun: Execute only failed tests
mvn test -DsuiteXmlFile=target/surefire-reports/testng-failed.xml
```

---

## 📁 File Locations

- **Original suite**: `testng.xml` (runs all tests)
- **Failed tests**: `target/surefire-reports/testng-failed.xml` (auto-generated)
- **Test reports**: `target/surefire-reports/`
- **Extent reports**: `test-results/extent/extent-report.html`

---

## 🔄 Complete Workflow Example

```bash
# Step 1: Run all tests
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
mvn clean test

# Step 2: Check results
# If tests failed, testng-failed.xml is created automatically

# Step 3: Rerun failed tests (choose one method)
# Option A: Use script
./rerun-failed-tests.sh

# Option B: Use Maven profile  
mvn test -Prerun-failed

# Option C: Use direct command
mvn test -DsuiteXmlFile=target/surefire-reports/testng-failed.xml
```

---

## ⚙️ Advanced: Skip Clean for Faster Reruns

If you want to rerun failed tests without cleaning:

```bash
# Faster - doesn't clean target/
mvn test -Prerun-failed

# vs slower - cleans everything first
mvn clean test -Prerun-failed
```

---

## 🛠️ Troubleshooting

**Problem**: `testng-failed.xml not found`
**Solution**: Make sure you've run the full test suite at least once with `mvn clean test`

**Problem**: "No tests found in suite"
**Solution**: All tests passed in the previous run! The failed.xml file only exists when tests fail.

**Problem**: Script permission denied
**Solution**: Run `chmod +x rerun-failed-tests.sh`

---

## 📊 Benefits

✅ **Save Time**: Only rerun tests that failed  
✅ **Fast Debugging**: Quickly verify fixes  
✅ **CI/CD Ready**: Integrate into pipelines  
✅ **Easy to Use**: Multiple convenient methods  

---

## 🔗 Related Files

- [pom.xml](pom.xml) - Contains the `rerun-failed` profile
- [testng.xml](testng.xml) - Main test suite configuration
- [rerun-failed-tests.sh](rerun-failed-tests.sh) - Convenience script

---

**Happy Testing! 🎯**
