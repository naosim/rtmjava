package com.naosim.someapp.infra.api.auth.checktoken

import com.naosim.rtm.domain.model.auth.CheckedToken
import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスクEntity
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import com.naosim.someapp.infra.api.lib.RequiredParam
import java.util.*
import java.util.function.Function

class AuthCheckTokenApi(val repositoryFactory: RepositoryFactory): Api<AuthCheckTokenRequest> {
    val mapConvertor = MapConverter()
    override val description = "Token確認"
    override val path = "/auth/checktoken"
    override val requestParams = AuthCheckTokenRequest()
    override val ok: (AuthCheckTokenRequest) -> Any =  {
        val checkToken: Optional<CheckedToken> = checkToken(it.token.get())
        if(checkToken.isPresent) {
            mapConvertor.apiOkResult(listOf(hashMapOf("token" to checkToken.map { it.rtmParamValue }.get())))
        } else {
            mapConvertor.apiErrorResult(400, "invalid token");
        }

    }

    fun checkToken(token: Token): Optional<CheckedToken> {
        return repositoryFactory.createRtmRepository().checkToken(token)
    }

}

class AuthCheckTokenRequest : ApiRequestParams<AuthCheckTokenRequest>() {
    @JvmField
    val token = RequiredParam<Token>("token", Function { Token(it) })
}