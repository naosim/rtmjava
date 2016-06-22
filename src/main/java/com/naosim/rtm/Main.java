package com.naosim.rtm;

import com.naosim.rtm.domain.model.*;
import com.naosim.rtm.lib.HttpRequestUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class Main {
    public static final ApiKey API_KEY = new RtmApiConfigImpl().getApiKey();
    public static final SharedSecret SHARED_SECRET = new RtmApiConfigImpl().getSharedSecret();

    public static void main(String... args) {
        Map<RtmParam, RtmParamValueObject> rtmParams = new HashMap<>();
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob);
        rtmParams.put(RtmParam.api_key, API_KEY);
        rtmParams.put(RtmParam.api_sig, new ApiSig(SHARED_SECRET, rtmParams));
        String url = "https://api.rememberthemilk.com/services/rest/?" + HttpRequestUtil.createQuery(rtmParams, RtmParam::getValue, RtmParamValueObject::getRtmParamValue);
        System.out.println(url);

        HttpClient c = HttpClientBuilder.create().build();
//        HttpGet httpGet = new HttpGet("https://api.rememberthemilk.com/services/rest/?" + createQuery());
    }

}
