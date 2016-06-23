package com.naosim.rtm;

import com.naosim.rtm.domain.model.*;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;
import com.naosim.rtm.lib.HttpRegularResult;
import com.naosim.rtm.lib.HttpRequestUtil;
import com.naosim.rtm.lib.StatusCodeOver400Exception;
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
        RtmRepositoryNet rtmRepository = new RtmRepositoryNet();
        Frob frob = rtmRepository.getFrob();
        rtmRepository.auth(frob);
        rtmRepository.getToken(frob);
//        System.out.print(frob.getRtmParamValue());
    }

}
