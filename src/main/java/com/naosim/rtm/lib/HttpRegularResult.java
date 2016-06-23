package com.naosim.rtm.lib;

public class HttpRegularResult {
    private final int statusCode;
    private final String body;

    public HttpRegularResult(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
