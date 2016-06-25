package com.naosim.rtm.domain.model

import com.naosim.rtm.domain.model.auth.*
import java.util.*

interface RtmTaskRepository {
    fun getTaskList(token: Token, filter: Filter? = null): List<TaskSeriesListEntity>
}