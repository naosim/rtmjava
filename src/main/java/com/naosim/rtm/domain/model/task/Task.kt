package com.naosim.rtm.domain.model.task

import java.time.LocalDateTime
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

class TaskId(val value: String) {};
class TaskDue(val value: String) {};
class TaskHasDueTime(val value: String) {};

class TaskDateTimes(
        val taskAddedDateTimes: TaskAddedDateTimes,
        val taskCompletedDateTimes: Optional<TaskCompletedDateTimes>,
        val taskDeletedDateTimes: Optional<TaskDeletedDateTimes>
) {};

class TaskAddedDateTimes(val dateTime: LocalDateTime) {};
class TaskCompletedDateTimes(val dateTime: LocalDateTime) {};
class TaskDeletedDateTimes(val dateTime: LocalDateTime) {};

class TaskPostponed(val value: String) {};
class TaskEstimate(val value: String) {};

class TaskIdSet(
        val listId: TaskSeriesListId,
        val taskSeriesId: TaskSeriesId,
        val taskId: TaskId
) {}