package com.naosim.rtm;

import com.naosim.rtm.domain.model.ApiKey;
import com.naosim.rtm.domain.model.SharedSecret;

public interface RtmApiConfig {
    ApiKey getApiKey();
    SharedSecret getSharedSecret();
}
