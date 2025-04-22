package com.petcircle.utils.ApiUtils;

import com.petcircle.model.common.RequestArtifact;
import com.petcircle.utils.ReportUtils.ApiLogger;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiUtil {

    @Autowired
    ApiLogger apiLogger;

    // Existing method: performOperation()

    public <T> Response performOperation(Operation operation, RequestArtifact requestArtifact) {
        RequestSpecification request = RestAssured.given().contentType("application/json");

        // Add headers
        if (requestArtifact.getHeaders() != null) {
            request.headers(requestArtifact.getHeaders());
        }

        // Add query params
        if (requestArtifact.getQueryParams() != null) {
            request.queryParams(requestArtifact.getQueryParams());
        }

        // Add body
        if (requestArtifact.getBody() != null) {
            request.body(requestArtifact.getBody());
        }

        // Execute request
        Response response;
        switch (operation) {
            case GET:
                response = request.when().get(requestArtifact.getUrl()).then().extract().response();
                break;
            case POST:
                response = request.when().post(requestArtifact.getUrl()).then().extract().response();
                break;
            case PUT:
                response = request.when().put(requestArtifact.getUrl()).then().extract().response();
                break;
            case DELETE:
                response = request.when().delete(requestArtifact.getUrl()).then().extract().response();
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }

        // Log everything
        apiLogger.log(
                operation,
                requestArtifact.getUrl(),
                requestArtifact.getBody(),
                requestArtifact.getHeaders(),
                requestArtifact.getQueryParams(),
                response
        );

        return response;
    }

    // Method to deep clone a RequestArtifact
    public RequestArtifact clone(RequestArtifact original) {
        return new RequestArtifact(
                original.getUrl(),
                original.getHeaders() != null ? new HashMap<>(original.getHeaders()) : null,
                original.getBody(),  // If body needs deep cloning, handle it here
                original.getQueryParams() != null ? new HashMap<>(original.getQueryParams()) : null
        );
    }

    // Existing header builder
    public Map<String, String> headerBuilder() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
