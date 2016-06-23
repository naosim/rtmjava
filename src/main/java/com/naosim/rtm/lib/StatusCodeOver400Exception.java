package com.naosim.rtm.lib;

public class StatusCodeOver400Exception extends Exception {
    public StatusCodeOver400Exception(int statusCode) {
        super("status code: " + statusCode);
    }
}
