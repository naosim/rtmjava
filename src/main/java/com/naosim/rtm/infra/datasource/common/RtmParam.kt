package com.naosim.rtm.infra.datasource.common

import java.util.*

enum class RtmParam(private val strValue: String? = null) {
    method(),
    api_sig(),
    api_key(),
    format(),
    perms(),
    frob(),
//    token,
    auth_token(),
    timeline(),
    name_("name"),
    parse(),
    filter(),
    v()
    ;
    val value = Optional.ofNullable(strValue).orElse(name)
}