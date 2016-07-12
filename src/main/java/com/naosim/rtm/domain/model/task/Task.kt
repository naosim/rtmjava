package com.naosim.rtm.domain.model.task

import com.naosim.rtm.domain.model.RtmParamValueObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TaskEntity (
        val taskIdSet: TaskIdSet,
        val taskDue: TaskDue,
        val taskHasDueTime: TaskHasDueTime,
        val taskDateTimes: TaskDateTimes,
        val taskPostponed: TaskPostponed,
        val taskEstimate: TaskEstimate
) {
    val taskId: TaskId = taskIdSet.taskId
}

class TaskId(val value: String): RtmParamValueObject {
    override val rtmParamValue: String = value
}
class TaskDue(val value: String) {};
class TaskHasDueTime(val value: String) {};

class TaskDateTimes(
        val taskAddedDateTime: TaskAddedDateTime,
        val taskCompletedDateTime: Optional<TaskCompletedDateTime>,
        val taskDeletedDateTime: Optional<TaskDeletedDateTime>,
        val taskStartDateTime: Optional<TaskStartDateTime>,
        val taskDueDateTime: Optional<TaskDueDateTime>
) {};

class TaskAddedDateTime(val dateTime: LocalDateTime) {};
class TaskCompletedDateTime(val dateTime: LocalDateTime) {};
class TaskDeletedDateTime(val dateTime: LocalDateTime) {};
class TaskStartDateTime(val dateTime: LocalDateTime): RtmParamValueObject {
    override val rtmParamValue: String = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
};
class TaskDueDateTime(val dateTime: LocalDateTime): RtmParamValueObject {
    override val rtmParamValue: String = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
};

class TaskPostponed(val value: String) {};
class TaskEstimate(val value: String) {};

class TaskIdSet(
        val listId: TaskSeriesListId,
        val taskSeriesId: TaskSeriesId,
        val taskId: TaskId
) {}