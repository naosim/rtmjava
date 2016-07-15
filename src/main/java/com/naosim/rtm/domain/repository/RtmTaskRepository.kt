package com.naosim.rtm.domain.repository

import com.naosim.rtm.domain.model.Filter
import com.naosim.rtm.domain.model.auth.*
import com.naosim.rtm.domain.model.task.*
import com.naosim.rtm.domain.model.timeline.TimelineId
import com.naosim.rtm.domain.model.timeline.TransactionalResponse
import java.util.*

interface RtmTaskRepository {
    fun getTaskList(token: Token, filter: Filter? = null): List<TaskSeriesListEntity>
    fun addTask(token: Token, timelineId: TimelineId, name: TaskSeriesName/*, parse: Optional<Parse> うまく動作しない */): TransactionalResponse<TaskSeriesEntity>
    fun completeTask(token: Token, timelineId: TimelineId, taskIdSet: TaskIdSet): TransactionalResponse<TaskSeriesEntity>
    fun updateStartDateTime(token: Token, timelineId: TimelineId, taskIdSet: TaskIdSet, startDateTime: Optional<TaskStartDateTime>): TransactionalResponse<TaskSeriesEntity>
    fun updateDueDateTime(token: Token, timelineId: TimelineId, taskIdSet: TaskIdSet, dueDateTime: Optional<TaskDueDateTime>): TransactionalResponse<TaskSeriesEntity>

}
