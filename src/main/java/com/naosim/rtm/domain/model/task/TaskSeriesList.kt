package com.naosim.rtm.domain.model.task

import com.naosim.rtm.domain.model.RtmParamValueObject
import java.util.*

class TaskSeriesListEntity(
        val taskSeriesListId: TaskSeriesListId,
        val taskSeriesListName: Optional<TaskSeriesListName>,
        val taskSeriesEntityList: List<TaskSeriesEntity>

) {}

class TaskSeriesListId(val value: String) : RtmParamValueObject {
    override val rtmParamValue: String = value
}
class TaskSeriesListName(val value: String){}