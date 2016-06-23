package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfigImpl
import com.naosim.rtm.domain.model.*
import com.naosim.rtm.lib.HttpRegularResult
import com.naosim.rtm.lib.HttpRequestUtil
import com.naosim.rtm.lib.StatusCodeOver400Exception
import java.io.IOException
import java.util.*

class RtmRepositoryNet {
    private val API_KEY = RtmApiConfigImpl().apiKey
    private val SHARED_SECRET = RtmApiConfigImpl().sharedSecret
    private val restBaseUrl = "https://api.rememberthemilk.com/services/rest/"
    private val authBaseUrl = "https://www.rememberthemilk.com/services/auth/"

    fun getFrob(): FrobUnAuthed {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_getfrob)
        rtmParams.put(RtmParam.api_key, API_KEY)

        val httpRegularResult = request(restBaseUrl, rtmParams);
        rtmParams.put(RtmParam.api_sig, ApiSig(SHARED_SECRET, rtmParams))
        val strFrob: String = httpRegularResult.getBody().substring(httpRegularResult.body.indexOf("<frob>") + "<frob>".length, httpRegularResult.body.indexOf("</frob>"))
        return FrobUnAuthed(strFrob);
    }

    fun getAuthUrl(frob: Frob): String {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.api_key, API_KEY)
        rtmParams.put(RtmParam.perms, RtmPerms.delete);
        rtmParams.put(RtmParam.frob, frob);
        rtmParams.put(RtmParam.api_sig, ApiSig(SHARED_SECRET, rtmParams))

        return authBaseUrl + "?" + HttpRequestUtil.createQuery(rtmParams, { it.value }, { it.rtmParamValue })
    }

    fun getToken(frob: FrobAuthed) {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.auth_gettoken)
        rtmParams.put(RtmParam.api_key, API_KEY)
        rtmParams.put(RtmParam.frob, frob);

        val httpRegularResult = request(restBaseUrl, rtmParams);
    }

    fun request(baseUrl: String, rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>): HttpRegularResult {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>(rtmParamsExcludeApiSig)
        rtmParams.put(RtmParam.api_sig, ApiSig(SHARED_SECRET, rtmParams))

        val url = baseUrl + "?" + HttpRequestUtil.createQuery(rtmParams, { it.value }, { it.rtmParamValue })
        println(url)

        try {
            val httpRegularResult = HttpRequestUtil.requestByGet(url)
            println(httpRegularResult.body)
            return httpRegularResult
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: StatusCodeOver400Exception) {
            throw RuntimeException(e)
        }
    }
}