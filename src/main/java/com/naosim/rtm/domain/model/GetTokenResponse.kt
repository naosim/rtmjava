package com.naosim.rtm.domain.model

import com.naosim.rtm.domain.model.auth.CheckedToken

class GetTokenResponse(val checkedToken: CheckedToken, val user: User) {}