package com.naosim.rtm.lib;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HttpRequestUtil {
    public static <K, V> String createQuery(Map<K, V> rtmParams, Function<K, String> getKeyStringAction, Function<V, String> getValueStringAction) {
        List<BasicNameValuePair> params = rtmParams.entrySet().stream().map(v -> new BasicNameValuePair(getKeyStringAction.apply(v.getKey()), getValueStringAction.apply(v.getValue()))).collect(Collectors.toList());
        return URLEncodedUtils.format(params, "utf-8");
    }

    public static <K, V> String createQuery(Map<String, String> rtmParams) {
        return createQuery(rtmParams, String::toString, String::toString);
    }
}
