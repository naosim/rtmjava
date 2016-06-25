package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfig
import com.naosim.rtm.RtmApiConfigImpl
import com.naosim.rtm.domain.model.*
import com.naosim.rtm.domain.model.auth.*
import com.naosim.rtm.domain.model.developer.ApiSig
import com.naosim.rtm.lib.HttpRequestUtil
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RtmAuthRepositoryNet(val rtmApiConfig: RtmApiConfig): RtmAuthRepository {
    private val rtmRequestUtil: RtmRequestUtil = RtmRequestUtil(rtmApiConfig)
    private val API_KEY = RtmApiConfigImpl().apiKey
    private val SHARED_SECRET = RtmApiConfigImpl().sharedSecret

    override fun getFrob(): FrobUnAuthed {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob)

        val response: RtmResponseXml = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.msg)
        }
        println(response.getFirstElementValueByTagName("frob"))
        return FrobUnAuthed(response.getFirstElementValueByTagName("frob"))
    }

    override fun getAuthUrl(frob: Frob): String {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.api_key, API_KEY)
        rtmParams.put(RtmParam.perms, RtmPerms.delete);
        rtmParams.put(RtmParam.frob, frob);
        rtmParams.put(RtmParam.api_sig, ApiSig(SHARED_SECRET, rtmParams))

        return rtmRequestUtil.authBaseUrl + "?" + HttpRequestUtil.createQuery(rtmParams, { it.value }, { it.rtmParamValue })
    }

    override fun getToken(frob: FrobAuthed): GetTokenResponse {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_gettoken)
        rtmParams.put(RtmParam.frob, frob);

        val response: RtmResponseXml = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.msg)
        }
        val token = response.getFirstElementValueByTagName("token")
        val userElement = response.getFirstElementByTagName("user")
        return GetTokenResponse(
                CheckedToken(token),
                User(
                        UserId(userElement.getAttribute("id")),
                        UserName(userElement.getAttribute("username")),
                        UserFullName(userElement.getAttribute("fullname"))
                )
        )
    }

    override fun checkToken(token: Token): Optional<CheckedToken> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.auth_token, token);
        rtmParams.put(RtmParam.method, RtmMethod.auth_checktoken)

        val response: RtmResponseXml = rtmRequestUtil.requestXML(rtmParams).body
        return if(response.isOk) Optional.of(CheckedToken(token.rtmParamValue)) else Optional.empty()
    }
}