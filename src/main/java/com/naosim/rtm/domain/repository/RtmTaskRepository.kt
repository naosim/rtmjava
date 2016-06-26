package com.naosim.rtm.domain.repository

import com.naosim.rtm.domain.model.Filter
import com.naosim.rtm.domain.model.task.TaskSeriesEntity
import com.naosim.rtm.domain.model.task.TaskSeriesListEntity
import com.naosim.rtm.domain.model.task.TaskSeriesName
import com.naosim.rtm.domain.model.auth.*
import com.naosim.rtm.domain.model.task.Parse
import com.naosim.rtm.domain.model.timeline.TimelineId
import com.naosim.rtm.domain.model.timeline.TransactionalResponse
import java.util.*

interface RtmTaskRepository {
    fun getTaskList(token: Token, filter: Filter? = null): List<TaskSeriesListEntity>
    fun addTask(token: Token, timelineId: TimelineId, name: TaskSeriesName, parse: Optional<Parse>): TransactionalResponse<TaskSeriesEntity>
}