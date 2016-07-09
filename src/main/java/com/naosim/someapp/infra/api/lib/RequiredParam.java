package com.naosim.someapp.infra.api.lib;

import java.util.Optional;
import java.util.function.Function;

import static spark.utils.StringUtils.isEmpty;

public class RequiredParam<T> extends RequestParam<T> {
    private final Optional<RequestParamRegex> regex;
    public RequiredParam(String key, Function<String, T> factory) {
        this(key, null, factory);
    }

    public RequiredParam(String key, RequestParamRegex regex, Function<String, T> factory) {
        super(key, factory);
        this.regex = Optional.ofNullable(regex);
    }

    @Override
    public void validate() {
        if(isEmpty(getValue())){
            throw new ParamRequiredException(getKey());
        }
        if(!regex.isPresent()) {
            return;
        }
        regex.map(RequestParamRegex::getValue).filter(getValue()::matches).orElseThrow(() -> new InvalidRegexException(regex.get().getValue(), getValue()));
    }
}
