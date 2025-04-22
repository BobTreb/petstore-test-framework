package com.petcircle.utils.TestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcircle.model.common.GenericResponse;
import com.petcircle.utils.ReportUtils.ApiLogger;
import io.restassured.response.Response;

import java.lang.reflect.Field;
import java.util.Objects;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        String message = "Status code should be " + expectedStatusCode;
        try {
            int actual = response.getStatusCode();
            if (actual != expectedStatusCode) {
                throw new AssertionError("Expected: " + expectedStatusCode + ", but was: " + actual);
            }
            ApiLogger.logAssertionResult(message, true);
        } catch (AssertionError e) {
            ApiLogger.logAssertionResult(message + " - " + e.getMessage(), false);
            throw e;
        }
    }

    public static void validateBodyIsEmpty(Response response, boolean shouldBeEmpty) {
        String actualBody = response.getBody() != null ? response.getBody().asString().trim() : null;
        String message = "Response body is expected to be empty: " + shouldBeEmpty;

        try {
            if (shouldBeEmpty) {
                if (actualBody != null && !actualBody.isEmpty()) {
                    throw new AssertionError("Response body is not empty. Actual content: \"" + actualBody + "\"");
                }
            } else {
                if (actualBody == null || actualBody.isEmpty()) {
                    throw new AssertionError("Response body is expected to have content, but it is empty");
                }
            }
            ApiLogger.logAssertionResult(message, true);
        } catch (AssertionError e) {
            ApiLogger.logAssertionResult(message + " - " + e.getMessage(), false);
            throw e;
        }
    }

    public static void validateSchema(Response response, String schemaPath) {
        String message = "Response should match schema: " + schemaPath;
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            ApiLogger.logAssertionResult(message, true);
        } catch (AssertionError e) {
            ApiLogger.logAssertionResult(message + " - " + e.getMessage(), false);
            throw e;
        }
    }

    public static <T> void validateJsonEquality(Object expected, Response response, Class<T> clazz, String assertionLabel) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            T expectedObj = mapper.convertValue(expected, clazz);
            T actualObj = response.as(clazz);

            StringBuilder differences = new StringBuilder();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object expectedValue = field.get(expectedObj);
                Object actualValue = field.get(actualObj);

                // Convert both values to JSON for readable, deep comparison
                String expectedJson = mapper.writeValueAsString(expectedValue);
                String actualJson = mapper.writeValueAsString(actualValue);

                if (!Objects.equals(expectedJson, actualJson)) {
                    differences.append(String.format(
                            "Field '%s' - expected: %s, actual: %s%n",
                            field.getName(), expectedJson, actualJson
                    ));
                }
            }

            if (differences.length() > 0) {
                String diffStr = differences.toString();
                ApiLogger.logAssertionResult(assertionLabel + " - " + diffStr, false);
                throw new AssertionError("Object mismatch:\n" + diffStr);
            }

            ApiLogger.logAssertionResult(assertionLabel, true);
        } catch (Exception e) {
            ApiLogger.logAssertionResult(assertionLabel + " - " + e.getMessage(), false);
            throw new AssertionError(assertionLabel + " - " + e.getMessage(), e);
        }
    }

    public static void validateErrorResponse(Response response, Integer code, String type, String expectedMessage) {
        String assertionLabel = "Validating error response matches expected fields";
        try {
            GenericResponse actual = response.as(GenericResponse.class);
            GenericResponse expected = new GenericResponse(code, type, expectedMessage);

            StringBuilder differences = new StringBuilder();

            if (expected.getCode() != actual.getCode()) {
                differences.append(String.format("Field 'code' - expected: %d, actual: %d%n",
                        expected.getCode(), actual.getCode()));
            }

            if (!expected.getType().equals(actual.getType())) {
                differences.append(String.format("Field 'type' - expected: %s, actual: %s%n",
                        expected.getType(), actual.getType()));
            }

            if (!expected.getMessage().equals(actual.getMessage())) {
                differences.append(String.format("Field 'message' - expected: %s, actual: %s%n",
                        expected.getMessage(), actual.getMessage()));
            }

            if (differences.length() > 0) {
                String diffStr = differences.toString();
                ApiLogger.logAssertionResult(assertionLabel + " - " + diffStr, false);
                throw new AssertionError("ErrorResponse mismatch:\n" + diffStr);
            }

            ApiLogger.logAssertionResult(assertionLabel, true);
        } catch (Exception e) {
            ApiLogger.logAssertionResult(assertionLabel + " - " + e.getMessage(), false);
            throw new AssertionError(assertionLabel + " - " + e.getMessage(), e);
        }
    }
}
