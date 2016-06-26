package com.naosim.rtm.infra.datasource

import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.domain.model.timeline.TimelineId
import com.naosim.rtm.domain.repository.RtmAuthRepository
import com.naosim.rtm.domain.repository.RtmRepository
import com.naosim.rtm.domain.repository.RtmTaskRepository
import com.naosim.rtm.infra.datasource.common.RtmMethod
import com.naosim.rtm.infra.datasource.common.RtmParam
import com.naosim.rtm.infra.datasource.common.RtmRequestUtil
import java.util.*

class RtmRepositoryNet(val rtmApiConfig: RtmApiConfig, val rtmAuthRepository: RtmAuthRepository, val rtmTaskRepository: RtmTaskRepository): RtmRepository, RtmAuthRepository by rtmAuthRepository, RtmTaskRepository by rtmTaskRepository {
    private val rtmRequestUtil = RtmRequestUtil(rtmApiConfig)

    fun createTimeline(token: Token): TimelineId {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.timelines_create)
        rtmParams.put(RtmParam.auth_token, token)

        val response = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.code + " " + response.failedResponse!!.msg)
        }

        return TimelineId(response.getFirstElementValueByTagName("timeline"))
    }


}

class RtmRepositoryNetFactory {
    fun create(rtmApiConfig: RtmApiConfig): RtmRepositoryNet {
        return RtmRepositoryNet(
                rtmApiConfig,
                RtmAuthRepositoryNet(rtmApiConfig),
                RtmTaskRepositoryNet(rtmApiConfig)
        )
    }
}
