package com.naosim.someapp.infra.api.lib;

public class InvalidRegexException extends RuntimeException {
    public InvalidRegexException(String regex, String value) {
        super("regex: " + regex + ", value: " + value);
    }
}
