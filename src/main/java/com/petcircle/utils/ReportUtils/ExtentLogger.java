package com.petcircle.utils.ReportUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcircle.utils.ApiUtils.Operation;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class ExtentLogger {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static ExtentReports extent;
    private static ExtentTest test;
    private static boolean isInitialized = false;
    private static String reportPath;

    // Sets up the report file path and initializes the extent report if not already initialized.
    public static void setTestClassName(String className) {
        // Debug: Log the class name to ensure it's being correctly set
        System.out.println("Setting class name for report: " + className);

        if (!isInitialized) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportPath = "build/reports/extent/" + className + "_" + timestamp + ".html";

            // Debug: Log the report path to verify it’s being correctly constructed
            System.out.println("Generated report path: " + reportPath);

            File reportDir = new File("build/reports/extent");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);

            isInitialized = true;
        }
    }

    // Logs API request and response details to the extent report.
    public static <T> void log(Operation operation, String endpoint, T body,
                               Map<String, String> headers, Map<String, String> queryParams,
                               Response response) {

        // Create or reuse the current test node
        ExtentTest parentTest = (test != null)
                ? test
                : extent.createTest("API Test - " + operation + " " + endpoint);

        // Log request details
        ExtentTest requestNode = parentTest.createNode("Request");
        requestNode.info("URL: " + endpoint);
        requestNode.info("Method: " + operation);

        logHeaders(requestNode.createNode("Headers"), headers);
        logQueryParams(requestNode.createNode("Query Params"), queryParams);
        logBody(requestNode.createNode("Body"), body);

        // Log response details
        ExtentTest responseNode = parentTest.createNode("Response");
        responseNode.info("Status Code: " + response.getStatusCode());

        logHeaders(responseNode.createNode("Headers"), response.getHeaders());
        logJsonBody(responseNode.createNode("Body"), response.getBody().asString());
    }

    // Helper methods to log headers, query params, body, and response body
    private static void logHeaders(ExtentTest node, Map<String, String> headers) {
        node.info("Request Headers:");
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> node.info("    " + k + " : " + v));
        } else {
            node.info("    (No headers present)");
        }
    }

    // Helper method to log assertion result
    public static void logAssertionResult(String assertionMessage, boolean isPassed) {
        if (isPassed) {
            test.pass("✅ Assertion Passed: " + assertionMessage);
        } else {
            test.fail("❌ Assertion Failed: " + assertionMessage);
        }
    }


    private static void logHeaders(ExtentTest node, Headers headers) {
        node.info("Response Headers:");
        if (headers != null && !headers.asList().isEmpty()) {
            for (Header header : headers) {
                node.info("    " + header.getName() + " : " + header.getValue());
            }
        } else {
            node.info("    (No headers present)");
        }
    }

    private static void logQueryParams(ExtentTest node, Map<String, String> queryParams) {
        node.info("Request Query Params : " + (queryParams != null ? queryParams : "(No query params present)"));
    }

    private static <T> void logBody(ExtentTest node, T body) {
        node.info("Request Body:");
        try {
            if (body != null) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                node.info("    " + json.replace("\n", "\n    "));
            } else {
                node.info("    (null)");
            }
        } catch (Exception e) {
            node.info("    (Error serializing body)");
        }
    }

    private static void logJsonBody(ExtentTest node, String body) {
        node.info("Response Body:");
        if (body != null && !body.trim().isEmpty()) {
            try {
                Object json = mapper.readValue(body, Object.class);
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                node.info("    " + prettyJson.replace("\n", "\n    "));
            } catch (Exception e) {
                node.info("    (Unable to parse body)");
                node.info("    Raw Body: " + body);
            }
        } else {
            node.info("    (null or empty)");
        }
    }

    // Flushes the report only if it has been initialized.
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    // Starts a new test in the report.
    public static void startTest(String testName, String description) {
        if (extent != null) {
            test = extent.createTest(testName, description);
        }
    }

    // Returns the current report path (useful for debugging)
    public static String getReportPath() {
        return reportPath;
    }
}
