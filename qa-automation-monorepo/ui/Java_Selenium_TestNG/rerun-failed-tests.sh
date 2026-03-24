#!/bin/bash

# Script to rerun failed TestNG tests
# This script checks if testng-failed.xml exists and reruns only the failed tests

FAILED_SUITE="target/surefire-reports/testng-failed.xml"

echo "=========================================="
echo "  Rerun Failed Tests Script"
echo "=========================================="
echo ""

# Check if testng-failed.xml exists
if [ ! -f "$FAILED_SUITE" ]; then
    echo "❌ Error: No failed tests found!"
    echo ""
    echo "The file '$FAILED_SUITE' does not exist."
    echo "Please run the test suite first with: mvn clean test"
    echo ""
    exit 1
fi

echo "✓ Found failed test suite: $FAILED_SUITE"
echo ""
echo "Rerunning failed tests..."
echo ""

# Set JAVA_HOME if needed (adjust path if different)
if [ -d "/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home" ]; then
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
    echo "Using Java 21: $JAVA_HOME"
fi

# Run Maven with the rerun-failed profile
mvn test -Prerun-failed

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ All previously failed tests passed!"
    echo "=========================================="
else
    echo ""
    echo "=========================================="
    echo "❌ Some tests still failing"
    echo "Check: target/surefire-reports/testng-failed.xml"
    echo "=========================================="
fi
