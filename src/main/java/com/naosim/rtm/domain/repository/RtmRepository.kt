package com.naosim.rtm.domain.repository

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.domain.model.timeline.TimelineId

interface RtmRepository: RtmAuthRepository, RtmTaskRepository {
    fun createTimeline(token: Token): TimelineId
}