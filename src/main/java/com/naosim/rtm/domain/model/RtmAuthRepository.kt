package com.naosim.rtm.domain.model

import com.naosim.rtm.domain.model.auth.*
import java.util.*

interface RtmAuthRepository {
    fun getFrob(): FrobUnAuthed
    fun getAuthUrl(frob: Frob): String
    fun getToken(frob: FrobAuthed): GetTokenResponse
    fun checkToken(token: Token): Optional<CheckedToken>
}