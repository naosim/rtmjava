package com.naosim.someapp.infra.api.auth.gettoken

import com.naosim.rtm.domain.model.auth.CheckedToken
import com.naosim.rtm.domain.model.auth.FrobAuthed
import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import com.naosim.someapp.infra.api.lib.RequiredParam
import java.util.*
import java.util.function.Function

class AuthGetTokenApi(val repositoryFactory: RepositoryFactory): Api<AuthGetTokenRequest> {
    val mapConvertor = MapConverter()
    override val description = "Token取得"
    override val path = "/auth/gettoken"
    override val requestParams = AuthGetTokenRequest()
    override val ok: (AuthGetTokenRequest) -> Any =  {
        val checkedToken: CheckedToken = getToken(it.frob.get())
        mapConvertor.apiOkResult(hashMapOf("token" to checkedToken.rtmParamValue))
    }

    fun getToken(frobAuthed: FrobAuthed): CheckedToken {
        return repositoryFactory.createRtmRepository().getToken(frobAuthed).checkedToken
    }

}

class AuthGetTokenRequest : ApiRequestParams<AuthGetTokenRequest>() {
    @JvmField
    val frob = RequiredParam<FrobAuthed>("frob", Function { FrobAuthed(it) })
}