package com.naosim.rtm.domain.model

import com.naosim.rtm.domain.model.auth.CheckedToken

class CheckTokenResponse(val checkedToken: CheckedToken, val user: User) {}
class RtmLoginFailedException(override val message: String): Exception(message) {}