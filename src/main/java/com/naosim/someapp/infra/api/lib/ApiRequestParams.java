package com.naosim.someapp.infra.api.lib;

import spark.QueryParamsMap;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiRequestParams<T> {
    public Stream<RequestParam> getRequestParamStream() {
        return Stream.of(getClass().getFields())
                .map(f -> {
                    try {
                        return f.get(this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(v -> v != null)
                .filter(v -> v instanceof RequestParam)
                .map(RequestParam.class::cast);
    }
    public ApiRequestParams<T> apply(QueryParamsMap queryparamsMap) {
        getRequestParamStream().forEach(v -> {
            System.out.println(v.getKey());
            System.out.println(queryparamsMap.value(v.getKey()));
            v.set(queryparamsMap.value(v.getKey()));
        });
        return this;
    }

    public Object valid(Function<T, Object> ok, Function<List<Exception>, Object> ng) {
        List<RequestParam> requestParamList = getRequestParamStream().collect(Collectors.toList());
        AnyTryCatcher anyTryCatcher = new AnyTryCatcher();
        for(RequestParam requestParam : requestParamList) {
            anyTryCatcher.run(() -> requestParam.validate());
        }

        if(anyTryCatcher.getExceptionList().isEmpty()) {
            return ok.apply((T)this);
        } else {
            return ng.apply(anyTryCatcher.getExceptionList());
        }
    }
}
