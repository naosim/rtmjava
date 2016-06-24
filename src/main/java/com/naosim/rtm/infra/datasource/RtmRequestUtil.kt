package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfig
import com.naosim.rtm.domain.model.ApiSig
import com.naosim.rtm.domain.model.RtmParam
import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.lib.HttpRegularResult
import com.naosim.rtm.lib.HttpRequestUtil
import com.naosim.rtm.lib.StatusCodeOver400Exception
import org.w3c.dom.Element
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RtmRequestUtil(val config: RtmApiConfig) {
    fun request(baseUrl: String, rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>): HttpRegularResult<String> {
        return request(baseUrl, rtmParamsExcludeApiSig) {
            try {
                HttpRequestUtil.convert(it)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    fun requestXML(baseUrl: String, rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>): HttpRegularResult<RtmResponseXml> {
        return request(baseUrl, rtmParamsExcludeApiSig) {
            RtmResponseXml(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it).firstChild as org.w3c.dom.Element)
        }
    }

    fun <T> request(baseUrl: String, rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>, proc: (InputStream) -> T): HttpRegularResult<T> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>(rtmParamsExcludeApiSig)
        rtmParams.put(RtmParam.api_sig, ApiSig(config.sharedSecret, rtmParams))

        val url = baseUrl + "?" + HttpRequestUtil.createQuery(rtmParams, { it.value }, { it.rtmParamValue })
        println(url)

        try {
            val httpRegularResult = HttpRequestUtil.requestByGet(url, proc)
            println(httpRegularResult.body)
            return httpRegularResult
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: StatusCodeOver400Exception) {
            throw RuntimeException(e)
        }
    }
}