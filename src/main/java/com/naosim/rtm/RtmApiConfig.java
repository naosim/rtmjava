package com.naosim.rtm;

import com.naosim.rtm.domain.model.developer.ApiKey;
import com.naosim.rtm.domain.model.developer.SharedSecret;

public interface RtmApiConfig {
    ApiKey getApiKey();
    SharedSecret getSharedSecret();
}
