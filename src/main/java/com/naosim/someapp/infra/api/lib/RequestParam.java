package com.naosim.someapp.infra.api.lib;

import java.util.function.Function;

public abstract class RequestParam<T> {
    private final String key;
    private String value;
    private final Function<String, T> factory;

    public RequestParam(String key, Function<String, T> factory) {
        this.key = key;
        this.factory = factory;
    }

    protected String getValue() {
        return value;
    }
    public void set(String value) {
        System.out.println(key + " = " + value);
        this.value = value;
    }

    public abstract void validate();

    public String getKey() {
        return key;
    }

    public T get() {
        return factory.apply(getValue());
    }
}
