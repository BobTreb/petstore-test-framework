package com.petcircle.utils.ReportUtils;

import com.petcircle.utils.ApiUtils.Operation;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiLogger {

    public static <T> void log(Operation operation, String endpoint, T body,
                               Map<String, String> headers,
                               Map<String, String> queryParams, Response response) {
        // Log to console
        ConsoleLogger.log(operation, endpoint, body, headers, queryParams, response);

        // Log to Extent Report
        ExtentLogger.log(operation, endpoint, body, headers, queryParams, response);
    }

    public static void logAssertionResult(String assertionDescription, boolean isPassed) {
        // Log to Console
        ConsoleLogger.logAssertionResult(assertionDescription, isPassed);

        // Log to Extent Report
        ExtentLogger.logAssertionResult(assertionDescription, isPassed);
    }

    public static void startTest(String testName, String description) {
        ExtentLogger.startTest(testName, description);
    }

    public static void setTestClassName(String className) {
        ExtentLogger.setTestClassName(className);
    }

    // Optional: Call this in your test runner shutdown hook or after test suite ends
    public static void flushReport() {
        ExtentLogger.flush();
    }
}
