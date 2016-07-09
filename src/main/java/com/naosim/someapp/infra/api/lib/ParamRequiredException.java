package com.naosim.someapp.infra.api.lib;

public class ParamRequiredException extends RuntimeException {
    private final String paramKey;

    public ParamRequiredException(String paramKey) {
        super("required: " + paramKey);
        this.paramKey = paramKey;
    }
}
