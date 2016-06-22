package com.naosim.rtm.domain.model

enum class RtmParam {
    method,
    api_sig,
    api_key,
    frob;
    val value = name
}