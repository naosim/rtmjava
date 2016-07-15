package com.naosim.rtm.infra.datasource.common

import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.domain.model.developer.ApiSig
import com.naosim.rtm.infra.datasource.RtmApiConfig
import com.naosim.rtm.lib.HttpRegularResult
import com.naosim.rtm.lib.HttpRequestUtil
import com.naosim.rtm.lib.StatusCodeOver400Exception
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RtmRequestUtil(val config: RtmApiConfig) {
    val restBaseUrl = "https://api.rememberthemilk.com/services/rest/"
    val authBaseUrl = "https://www.rememberthemilk.com/services/auth/"

    fun requestString(rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>): HttpRegularResult<String> {
        return request(restBaseUrl, rtmParamsExcludeApiSig) {
            try {
                HttpRequestUtil.convert(it)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    fun requestXML(rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>): HttpRegularResult<RtmResponseXml> {
        return request(restBaseUrl, rtmParamsExcludeApiSig) {
            RtmResponseXml(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it).firstChild as Element)
        }
    }

    fun <T> request(baseUrl: String, rtmParamsExcludeApiSig: HashMap<RtmParam, RtmParamValueObject>, proc: (InputStream) -> T): HttpRegularResult<T> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>(rtmParamsExcludeApiSig)
        rtmParams.put(RtmParam.api_key, config.apiKey)
        rtmParams.put(RtmParam.v, V2())
        if(rtmParams.containsKey(RtmParam.api_sig)) {
            throw RuntimeException("api_sigが既にある")
        }
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

    fun createLocalDateTime(datetime: String): LocalDateTime {
        val result = LocalDateTime.parse(datetime, DateTimeFormatter.ISO_DATE_TIME).plusHours(9)// japan
        return result
    }


    fun convertToList(nodeList: NodeList): List<Element> {
        val list = ArrayList<Element>();
        for(i in 0..nodeList.length - 1) {
            list.add(nodeList.item(i) as Element);
        }
        return list;
    }
}