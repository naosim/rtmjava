package com.naosim.rtm.domain.model.developer

import com.naosim.rtm.infra.datasource.RtmParam
import com.naosim.rtm.domain.model.RtmParamValueObject
import java.math.BigInteger
import java.security.MessageDigest

class ApiKey(override val rtmParamValue: String) : RtmParamValueObject {}
class SharedSecret(override val rtmParamValue: String) : RtmParamValueObject {}
class ApiSig(val sharedSecret: SharedSecret, val rtmParams: Map<RtmParam, RtmParamValueObject>) : RtmParamValueObject {
    override val rtmParamValue: String = md5(sharedSecret.rtmParamValue + rtmParams.keys.sortedBy { it.value }.map { it.value + rtmParams[it]!!.rtmParamValue }.reduce { v1, v2 -> v1 + v2 })//(comparing<RtmParam, String>(Function<RtmParam, String> { it.getValue() })).map({ key -> key.value + rtmParams[key].rtmParamValue }).reduce({ v1, v2 -> v1 + v2 }).orElse(""))

    fun md5(str: String): String {
        try {
            val str_bytes = str.toByteArray(charset("UTF-8"))
            val md = MessageDigest.getInstance("MD5")
            val md5_bytes = md.digest(str_bytes)
            val big_int = BigInteger(1, md5_bytes)
            return big_int.toString(16)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}