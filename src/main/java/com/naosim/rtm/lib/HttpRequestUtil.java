package com.naosim.rtm.lib;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public static String convert(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        try (
                InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
