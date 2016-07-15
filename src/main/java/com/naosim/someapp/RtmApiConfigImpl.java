package com.naosim.someapp;


import com.naosim.rtm.domain.model.developer.ApiKey;
import com.naosim.rtm.domain.model.developer.SharedSecret;
import com.naosim.rtm.infra.datasource.RtmApiConfig;

public class RtmApiConfigImpl implements RtmApiConfig {
    @Override
    public ApiKey getApiKey() {
        return new ApiKey("bbce87d819e9002842abfbc1d3a42374");
    }

    @Override
    public SharedSecret getSharedSecret() {
        return new SharedSecret("48c33e83a8923993");
    }
}
