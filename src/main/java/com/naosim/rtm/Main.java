package com.naosim.rtm;

import com.naosim.rtm.domain.model.TaskDateTimes;
import com.naosim.rtm.domain.model.TaskEntity;
import com.naosim.rtm.domain.model.TaskSeriesId;

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
//        TaskEntity p = new TaskEntity("hoge");

        Map<RtmParamKey, String> rtmParams = new HashMap<>();
        rtmParams.put(RtmParamKey.method, "rtm.auth.getFrob");
        rtmParams.put(RtmParamKey.api_key, API_KEY);
        String sharedSecret = SHARED_SECRET;
        String org = sharedSecret + rtmParams.keySet().stream().sorted().map(key -> key + rtmParams.get(key)).reduce((v1, v2) -> v1 + v2).orElse("");
        System.out.println(createApiSig(sharedSecret, rtmParams));
        System.out.println(md5(org));
        System.out.println(md5("48c33e83a8923993api_keybbce87d819e9002842abfbc1d3a42374methodrtm.auth.getFrob"));
    }

    interface ValueObject<T> {
        T getValue();
    }

    public enum RtmParamKey implements ValueObject<String> {
        method,
        api_key;

        @Override
        public String getValue() {
            return name();
        }
    }

    public static final String createApiSig(String sharedSecret, Map<RtmParamKey, String> rtmParams) {
        return md5(sharedSecret + rtmParams.keySet().stream().sorted(comparing(RtmParamKey::getValue)).map(key -> key.getValue() + rtmParams.get(key)).reduce((v1, v2) -> v1 + v2).orElse(""));
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
