package com.naosim.rtm.lib;

public class HttpRegularResult<T> {
    private final int statusCode;
    private final T body;

    public HttpRegularResult(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getBody() {
        return body;
    }
}
