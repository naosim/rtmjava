package com.naosim.rtm.domain.model

import org.apache.http.client.utils.URLEncodedUtils

interface RtmParamValueObject {
    val rtmParamValue: String
}
class ApiKey(override val rtmParamValue: String) : RtmParamValueObject {
}

enum class RtmMethod(override val rtmParamValue: String) : RtmParamValueObject {
    auth_getfrob("rtm.auth.getFrob");
}