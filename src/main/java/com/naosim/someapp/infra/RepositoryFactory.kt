package com.naosim.someapp.infra

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.domain.repository.RtmRepository
import com.naosim.rtm.infra.datasource.RtmRepositoryNetFactory
import com.naosim.someapp.RtmApiConfigImpl
import com.naosim.someapp.domain.タスクRepository
import com.naosim.someapp.infra.datasource.タスクRepositoryWithRTM

class RepositoryFactory {
    fun createRtmRepository(): RtmRepository {
        return RtmRepositoryNetFactory().create(RtmApiConfigImpl())
    }
    fun createタスクRepository(token: Token): タスクRepository {
        return タスクRepositoryWithRTM(token, createRtmRepository())
    }
}