package com.naosim.rtm.domain.model.auth

import com.naosim.rtm.domain.model.auth.User
import com.naosim.rtm.domain.model.auth.CheckedToken

class GetTokenResponse(val checkedToken: CheckedToken, val user: User) {}