package com.naosim.rtm.domain.model.task

import java.util.*

class TaskSeriesListEntity(
        val taskSeriesListId: TaskSeriesListId,
        val taskSeriesListName: Optional<TaskSeriesListName>,
        val taskSeriesEntityList: List<TaskSeriesEntity>

) {}

class TaskSeriesListId(val value: String){}
class TaskSeriesListName(val value: String){}