package com.naosim.rtm.infra.datasource

import com.naosim.rtm.domain.model.UserId
import com.naosim.rtm.domain.model.auth.CheckedToken
import com.naosim.rtm.domain.model.auth.Token
import java.util.*

class AuthRepository {

    private var token: Token = Token("hoge");

    fun getStoredTokenByUserId(userId: UserId): Optional<Token> {
        return if(userId.rtmParamValue == "naosim") Optional.of(token) else Optional.empty()
    }

    fun saveToken(userId: UserId, checkedToken: CheckedToken) {
        if(userId.rtmParamValue == "naosim") {
            this.token = checkedToken;
        }
    }
}
