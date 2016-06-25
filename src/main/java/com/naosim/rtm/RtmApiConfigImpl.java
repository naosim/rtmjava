package com.naosim.rtm;


import com.naosim.rtm.domain.model.developer.ApiKey;
import com.naosim.rtm.domain.model.developer.SharedSecret;

public class RtmApiConfigImpl implements RtmApiConfig {
    @Override
    public ApiKey getApiKey() {
        return new ApiKey("");
    }

    @Override
    public SharedSecret getSharedSecret() {
        return new SharedSecret("");
    }
}
