package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfigImpl
import com.naosim.rtm.domain.model.*
import com.naosim.rtm.lib.HttpRegularResult
import com.naosim.rtm.lib.HttpRequestUtil
import com.naosim.rtm.lib.StatusCodeOver400Exception
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RtmRepositoryNet {
    private val rtmRequestUtil: RtmRequestUtil = RtmRequestUtil(RtmApiConfigImpl())
    private val API_KEY = RtmApiConfigImpl().apiKey
    private val SHARED_SECRET = RtmApiConfigImpl().sharedSecret
    private val restBaseUrl = "https://api.rememberthemilk.com/services/rest/"
    private val authBaseUrl = "https://www.rememberthemilk.com/services/auth/"

    fun getFrob(): FrobUnAuthed {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob)
        rtmParams.put(RtmParam.api_key, API_KEY)

        val response: RtmResponseXml = rtmRequestUtil.requestXML(restBaseUrl, rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.msg)
        }
        println(response.getFirstElementValueByTagName("frob"))
        return FrobUnAuthed(response.getFirstElementValueByTagName("frob"))
    }

    fun getAuthUrl(frob: Frob): String {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.api_key, API_KEY)
        rtmParams.put(RtmParam.perms, RtmPerms.delete);
        rtmParams.put(RtmParam.frob, frob);
        rtmParams.put(RtmParam.api_sig, ApiSig(SHARED_SECRET, rtmParams))

        return authBaseUrl + "?" + HttpRequestUtil.createQuery(rtmParams, { it.value }, { it.rtmParamValue })
    }

    fun getToken(frob: FrobAuthed): GetTokenResponse {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_gettoken)
        rtmParams.put(RtmParam.api_key, API_KEY)
        rtmParams.put(RtmParam.frob, frob);

        val response: RtmResponseXml = rtmRequestUtil.requestXML(restBaseUrl, rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.msg)
        }
        val token = response.getFirstElementValueByTagName("token")
        val userElement = response.getFirstElementByTagName("user")
        return GetTokenResponse(
                Token(token),
                User(
                        UserId(userElement.getAttribute("id")),
                        UserName(userElement.getAttribute("username")),
                        UserFullName(userElement.getAttribute("fullname"))
                )
        )
    }
}