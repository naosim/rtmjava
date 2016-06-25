package com.naosim.rtm.lib;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
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

    public static <T> HttpRegularResult<T> requestByGet(String url, Function<InputStream, T> proc) throws IOException, StatusCodeOver400Exception {
        HttpClient c = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse res = c.execute(httpGet);
        int code = res.getStatusLine().getStatusCode();
        if(code >= 400) {
            throw new StatusCodeOver400Exception(code);
        }
        res.getEntity().getContent();
        T body = proc.apply(res.getEntity().getContent());
        return new HttpRegularResult<T>(code, body);
    }
}
