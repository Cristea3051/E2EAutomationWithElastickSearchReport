#!/bin/bash

echo "Installing Playwright browsers..."
echo "=================================="

echo ""
echo "Installing Chromium..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"

echo ""
echo "Installing Firefox..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install firefox"

echo ""
echo "Installing WebKit..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install webkit"

echo ""
echo "=================================="
echo "All browsers installed successfully!"
echo ""
echo "You can now run tests with:"
echo "  mvn clean test"
