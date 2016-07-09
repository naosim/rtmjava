package com.naosim.someapp.infra.api.auth.getauthurl

import com.naosim.rtm.domain.model.auth.Frob
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams

class AuthGetAuthUrlApi(val repositoryFactory: RepositoryFactory): Api<AuthGetAuthUrlRequest> {
    val mapConvertor = MapConverter()
    override val description = "認証URL取得"
    override val path = "/auth/getauthurl"
    override val requestParams = AuthGetAuthUrlRequest()
    override val ok: (AuthGetAuthUrlRequest) -> Any =  {
        val result = getAuthUrl()
        mapConvertor.apiOkResult(listOf(hashMapOf("frob" to result.frob.rtmParamValue, "url" to result.url)))
    }

    fun getAuthUrl(): GetAuthUrlResult {
        val rtmRepository = repositoryFactory.createRtmRepository()
        val frobUnAuthed = rtmRepository.getFrob()
        val url = rtmRepository.getAuthUrl(frobUnAuthed)
        return GetAuthUrlResult(frobUnAuthed, url)
    }
}


class AuthGetAuthUrlRequest : ApiRequestParams<AuthGetAuthUrlRequest>()

class GetAuthUrlResult(val frob: Frob, val url: String)