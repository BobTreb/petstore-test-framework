package com.petcircle.model.common;

import java.util.Map;

public class RequestArtifact {

    private String url;
    private Map<String, String> headers;
    private Object body;
    private Map<String, String> queryParams;

    public RequestArtifact(String url, Map<String, String> headers, Object body, Map<String, String> queryParams) {
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
    }

    // Getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
}
