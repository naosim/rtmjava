package com.naosim.someapp.infra.api.lib;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static spark.utils.StringUtils.isEmpty;

public class OptionParam<T> extends RequestParam<T> {
    private final Optional<RequestParamRegex> regex;
    private final Supplier<T> factoryIfEmpty;
    public OptionParam(String key, Function<String, T> factory, Supplier<T> factoryIfEmpty) {
        this(key, null, factory, factoryIfEmpty);
    }

    public OptionParam(String key, RequestParamRegex regex, Function<String, T> factory, Supplier<T> factoryIfEmpty) {
        super(key, factory);
        this.regex = Optional.ofNullable(regex);
        this.factoryIfEmpty = factoryIfEmpty;
    }

    @Override
    public void validate() {
        if(isEmpty(getValue())){
            return;
        }
        if(!regex.isPresent()) {
            return;
        }
        regex.map(RequestParamRegex::getValue).filter(getValue()::matches).orElseThrow(() -> new InvalidRegexException(regex.get().getValue(), getValue()));
    }

    public T get() {
        if(isEmpty(getValue())) {
            return factoryIfEmpty.get();
        }
        return super.get();
    }
}
