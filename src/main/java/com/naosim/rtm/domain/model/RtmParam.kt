package com.naosim.rtm.domain.model

enum class RtmParam {
    method,
    api_sig,
    api_key,
    format,
    frob;
    val value = name
}