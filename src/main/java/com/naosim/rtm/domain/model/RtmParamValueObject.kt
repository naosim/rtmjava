package com.naosim.rtm.domain.model

interface RtmParamValueObject {
    val rtmParamValue: String
}

class Filter(override val rtmParamValue: String) : RtmParamValueObject {}
enum class Format : RtmParamValueObject {
    json;
    override val rtmParamValue: String = name
}


