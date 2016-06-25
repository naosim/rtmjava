package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfigImpl
import com.naosim.rtm.domain.model.RtmAuthRepository
import com.naosim.rtm.domain.model.RtmTaskRepository

class RtmRepositoryNet(val rtmAuthRepository: RtmAuthRepository, val rtmTaskRepository: RtmTaskRepository): RtmAuthRepository by rtmAuthRepository, RtmTaskRepository by rtmTaskRepository {}
class RtmRepositoryNetFactory {
    fun create(): RtmRepositoryNet {
        val rtmApiConfigImpl = RtmApiConfigImpl();
        return RtmRepositoryNet(RtmAuthRepositoryNet(rtmApiConfigImpl), RtmTaskRepositoryNet(rtmApiConfigImpl));
    }
}
