package com.naosim.someapp;


import com.naosim.rtm.infra.datasource.RtmApiConfig;
import com.naosim.rtm.domain.model.developer.ApiKey;
import com.naosim.rtm.domain.model.developer.SharedSecret;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
