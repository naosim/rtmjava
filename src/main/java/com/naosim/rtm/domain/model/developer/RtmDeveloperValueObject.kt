package com.naosim.rtm.domain.model.developer

import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.infra.datasource.common.RtmParam
import com.naosim.rtm.lib.MD5.md5

class ApiKey(override val rtmParamValue: String) : RtmParamValueObject {}
class SharedSecret(override val rtmParamValue: String) : RtmParamValueObject {}
class ApiSig(val sharedSecret: SharedSecret, val rtmParams: Map<RtmParam, RtmParamValueObject>) : RtmParamValueObject {
    override val rtmParamValue: String = md5(sharedSecret.rtmParamValue + rtmParams.keys.sortedBy { it.value }.map { it.value + rtmParams[it]!!.rtmParamValue }.reduce { v1, v2 -> v1 + v2 })//(comparing<RtmParam, String>(Function<RtmParam, String> { it.getValue() })).map({ key -> key.value + rtmParams[key].rtmParamValue }).reduce({ v1, v2 -> v1 + v2 }).orElse(""))
}