package com.naosim.rtm.domain.model.task

import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.domain.model.task.Tag
import java.time.LocalDateTime

class TaskSeriesEntity(
        val taskIdSet: TaskIdSet,
        val taskSeriesName: TaskSeriesName,
        val taskEntity: TaskEntity,
        val tags: List<Tag>,
        val taskSeriesSource: TaskSeriesSource,
        val taskSeriesLocationid: TaskSeriesLocationid,
        val taskSeriesDateTimes: TaskSeriesDateTimes
) {
    val taskSeriesId: TaskSeriesId = taskIdSet.taskSeriesId
}

class TaskSeriesId(val value: String): RtmParamValueObject {
    override val rtmParamValue: String = value
}

class TaskSeriesDateTimes(
        val taskSeriesCreatedDateTime: TaskSeriesCreatedDateTime,
        val taskSeriesModifiedDateTime: TaskSeriesModifiedDateTime
) {}

class TaskSeriesCreatedDateTime(val dateTime: LocalDateTime) {}
class TaskSeriesModifiedDateTime(val dateTime: LocalDateTime) {}

class TaskSeriesName(override val rtmParamValue: String) : RtmParamValueObject {}
class TaskSeriesSource(val value: String) {}
class TaskSeriesLocationid(val value: String) {}