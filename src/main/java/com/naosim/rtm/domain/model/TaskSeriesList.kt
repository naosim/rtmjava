package com.naosim.rtm.domain.model

import java.util.*

class TaskSeriesListEntity(
        val taskSeriesListId: TaskSeriesListId,
        val taskSeriesListName: Optional<TaskSeriesListName>
) {}

class TaskSeriesListId(val value: String){}
class TaskSeriesListName(val value: String){}