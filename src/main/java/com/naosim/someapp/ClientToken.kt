package com.naosim.someapp

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.lib.MD5.md5

class ClientToken(val value: String) {
    constructor(token: Token): this(md5(token.rtmParamValue))
}