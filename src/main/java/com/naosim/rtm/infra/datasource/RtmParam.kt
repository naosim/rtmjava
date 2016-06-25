package com.naosim.rtm.infra.datasource

enum class RtmParam {
    method,
    api_sig,
    api_key,
    format,
    perms,
    frob,
//    token,
    auth_token,
    filter
    ;
    val value = name
}