package com.naosim.rtm;

import com.naosim.rtm.domain.model.*;
import com.naosim.rtm.lib.HttpRequestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final ApiKey API_KEY = new RtmApiConfigImpl().getApiKey();
    public static final SharedSecret SHARED_SECRET = new RtmApiConfigImpl().getSharedSecret();

    public static void main(String... args) {
        Map<RtmParam, RtmParamValueObject> rtmParams = new HashMap<>();
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob);
        rtmParams.put(RtmParam.api_key, API_KEY);
//        rtmParams.put(RtmParam.format, Format.json);
        rtmParams.put(RtmParam.api_sig, new ApiSig(SHARED_SECRET, rtmParams));

        String url = "https://api.rememberthemilk.com/services/rest/?" + HttpRequestUtil.createQuery(rtmParams, RtmParam::getValue, RtmParamValueObject::getRtmParamValue);
        System.out.println(url);

        HttpClient c = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse res = c.execute(httpGet);
            int code = res.getStatusLine().getStatusCode();
            res.getEntity().getContent();
            String body = HttpRequestUtil.convert(res.getEntity().getContent());
            System.out.println(code);
            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
