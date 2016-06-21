package com.naosim.rtm;

import com.naosim.rtm.domain.model.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparing;

public class Main {
    public static final String API_KEY = new TrmApiConfig().getApiKey();
    public static final String SHARED_SECRET = new TrmApiConfig().getSharedSecret();

    public static void main(String... args) {
        Map<RtmParam, RtmParamValueObject> rtmParams = new HashMap<>();
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob);
        rtmParams.put(RtmParam.api_key, new ApiKey(API_KEY));
        String sharedSecret = SHARED_SECRET;
        System.out.println(createApiSig(sharedSecret, rtmParams));

        HttpClient c = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://api.rememberthemilk.com/services/rest/");
    }

    public static String createQuery(Map<RtmParam, RtmParamValueObject> rtmParams) {
        return null;
    }

    public static final String createApiSig(String sharedSecret, Map<RtmParam, RtmParamValueObject> rtmParams) {
        return md5(sharedSecret + rtmParams.keySet().stream().sorted(comparing(RtmParam::getValue)).map(key -> key.getValue() + rtmParams.get(key).getRtmParamValue()).reduce((v1, v2) -> v1 + v2).orElse(""));
    }

    public static String md5(String str) {
        try {
            byte[] str_bytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5_bytes = md.digest(str_bytes);
            BigInteger big_int = new BigInteger(1, md5_bytes);
            return big_int.toString(16);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
