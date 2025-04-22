package com.petcircle.utils.ReportUtils;

import com.petcircle.utils.ApiUtils.Operation;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsoleLogger {

    public static <T> void log(Operation operation, String endpoint, T body,
                                                 Map<String, String> headers,
                                                 Map<String, String> queryParams, Response response) {

        logToConsole(operation, endpoint, body, headers, queryParams, response);
    }

    public static void logAssertionResult(String assertionMessage, boolean isPassed) {
        if (isPassed) {
            System.out.println("✅ Assertion Passed: " + assertionMessage);
        } else {
            System.out.println("❌ Assertion Failed: " + assertionMessage);
        }
    }

    private static <T> void logToConsole(Operation operation, String endpoint, T body,
                                         Map<String, String> headers, Map<String, String> queryParams,
                                         Response response) {

        System.out.println("=======================================================");
        System.out.println("Logging Request");
        System.out.println("=======================================================");
        System.out.println("Request URL : " + endpoint);
        System.out.println("Request Method : " + operation);
        logHeaders("Request Headers", headers);
        logQueryParams(queryParams);
        logBody("Request Body", body);

        System.out.println("=======================================================");
        System.out.println("Logging Response");
        System.out.println("=======================================================");
        System.out.println("Response Status Code : " + response.getStatusCode());
        logHeaders("Response Headers", response.getHeaders());
        logJsonBody("Response Body", response.getBody().asString());
    }

    private static void logHeaders(String label, Map<String, String> headers) {
        System.out.println(label + " :");
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, value) -> System.out.println("    " + key + " : " + value));
        } else {
            System.out.println("    (No headers present)");
        }
    }

    private static void logHeaders(String label, Headers headers) {
        System.out.println(label + " :");
        if (headers != null && !headers.asList().isEmpty()) {
            for (Header header : headers) {
                System.out.println("    " + header.getName() + " : " + header.getValue());
            }
        } else {
            System.out.println("    (No headers present)");
        }
    }

    private static void logQueryParams(Map<String, String> queryParams) {
        System.out.println("Request Query Params : " + (queryParams != null ? queryParams : "(No query params present)"));
    }

    private static <T> void logBody(String label, T body) {
        System.out.print(label + " :\n");
        try {
            if (body != null) {
                String jsonBody = new com.fasterxml.jackson.databind.ObjectMapper()
                        .writerWithDefaultPrettyPrinter().writeValueAsString(body);
                System.out.println("    " + jsonBody.replace("\n", "\n    "));
            } else {
                System.out.println("    (null)");
            }
        } catch (Exception e) {
            System.out.println("    (Error while serializing request body)");
            e.printStackTrace();
        }
    }

    private static void logJsonBody(String label, String body) {
        System.out.print(label + " :\n");
        if (body != null && !body.trim().isEmpty()) {
            try {
                Object json = new com.fasterxml.jackson.databind.ObjectMapper().readValue(body, Object.class);
                String prettyJson = new com.fasterxml.jackson.databind.ObjectMapper()
                        .writerWithDefaultPrettyPrinter().writeValueAsString(json);
                System.out.println("    " + prettyJson.replace("\n", "\n    "));
            } catch (Exception e) {
                System.out.println("    (Unable to parse response body)");
                System.out.println("    Raw Body: " + body);
            }
        } else {
            System.out.println("    (null or empty)");
        }
    }
}