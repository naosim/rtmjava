package com.naosim.rtm.domain.model

import com.naosim.rtm.domain.model.auth.CheckedToken
import com.naosim.rtm.domain.model.auth.Token
import java.util.*

interface RtmRepository {
    fun checkToken(token: Token): Optional<CheckedToken>
}