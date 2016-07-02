package com.naosim.someapp.infra.datasource

import com.naosim.rtm.domain.model.task.TaskId
import com.naosim.rtm.domain.model.task.TaskIdSet
import com.naosim.rtm.domain.model.task.TaskSeriesId
import com.naosim.rtm.domain.model.task.TaskSeriesListId
import com.naosim.someapp.domain.*

class タスクIDConverter {
    fun createタスクID(taskIdSet: TaskIdSet): タスクID {
        return タスクID("%s_%s_%s".format(taskIdSet.listId.value, taskIdSet.taskSeriesId.value, taskIdSet.taskId.value))
    }

    fun createTaskIdSet(タスクID: タスクID): TaskIdSet {
        val ids = タスクID.value.split("_")
        if(ids.size != 3) {
            throw RuntimeException("タスクIDがRTMに対応していない")
        }
        return TaskIdSet(
                TaskSeriesListId(ids.get(0)),
                TaskSeriesId(ids.get(1)),
                TaskId(ids.get(2))
        )
    }
}

class タスクEntityWithRTM(
        override val タスクID: タスクID,
        override val タスク更新: タスク更新Repository,
        override val タスク名: タスク名,
        override val タスク消化予定日Optional: タスク消化予定日Optional,
        override val タスク完了日Optional: タスク完了日Optional
) : タスクEntity {

}