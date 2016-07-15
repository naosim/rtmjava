package com.naosim.someapp.infra.datasource

import com.naosim.rtm.domain.model.Filter
import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.domain.model.task.*
import com.naosim.rtm.domain.model.timeline.TransactionalResponse
import com.naosim.rtm.domain.repository.RtmRepository
import com.naosim.someapp.domain.*


class タスクRepositoryWithRTM(val token: Token, val rtmRepository: RtmRepository): タスクRepository {
    val タスクIDConverter = タスクIDConverter()
    override fun 追加(タスク名: タスク名, タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity {
        val timeline = rtmRepository.createTimeline(token)
        val taskDueDateTimeOptional = タスク消化予定日Optional.get().map{ TaskDueDateTime(it.localDate.atStartOfDay()) }
        val taskIdSet = rtmRepository.addTask(token, timeline, TaskSeriesName(タスク名.value)).response.taskIdSet
        rtmRepository.updateDueDateTime(token, timeline, taskIdSet, taskDueDateTimeOptional)
        val タスクID = タスクIDConverter.createタスクID(taskIdSet)
        return タスクEntityWithRTM(
                タスクID,
                タスク更新RepositoryWithRTM(token, taskIdSet, this, rtmRepository),
                タスク名,
                タスク消化予定日Optional,
                タスク完了日NotExist(),
                タスク削除日NotExist()
        )
    }

    fun convertTaskSeriesEntityToタスクEntity(taskSeriesEntity: TaskSeriesEntity): タスクEntityWithRTM {
        val タスクID = タスクIDConverter.createタスクID(taskSeriesEntity.taskIdSet)
        val タスク消化予定日Optional = taskSeriesEntity.taskEntity.taskDateTimes.taskDueDateTime
                .map { タスク消化予定日(it.dateTime.toLocalDate()) as タスク消化予定日Optional }
                .orElse(タスク消化予定日NotExist())
        val タスク完了日Optional = taskSeriesEntity.taskEntity.taskDateTimes.taskCompletedDateTime
                .map { タスク完了日(it.dateTime.toLocalDate()) as タスク完了日Optional }
                .orElse(タスク完了日NotExist())
        val タスク削除日Optional = taskSeriesEntity.taskEntity.taskDateTimes.taskDeletedDateTime
                .map { タスク削除日(it.dateTime.toLocalDate()) as タスク削除日Optional }
                .orElse(タスク削除日NotExist())

        return タスクEntityWithRTM(
                タスクID,
                タスク更新RepositoryWithRTM(token, taskSeriesEntity.taskIdSet, this, rtmRepository),
                タスク名(taskSeriesEntity.taskSeriesName.rtmParamValue),
                タスク消化予定日Optional,
                タスク完了日Optional,
                タスク削除日Optional
        )
    }

    override fun すべてのタスク取得(): List<タスクEntity> {
        val タスクEntityList: List<タスクEntity> = rtmRepository.getTaskList(token, Filter("(status:incomplete)or(completedAfter:25/06/2016)"))
                .map { it.taskSeriesEntityList }
                .reduce { a, b -> a.plus(b) }
                .map { convertTaskSeriesEntityToタスクEntity(it) }
//                .sorted()
        return タスクEntityList
    }

    override fun 完了(タスクID: タスクID): タスクEntity {
        val timelineId = rtmRepository.createTimeline(token)
        val result = rtmRepository.completeTask(token, timelineId, タスクIDConverter.createTaskIdSet(タスクID)).response
        return convertTaskSeriesEntityToタスクEntity(result);
    }

    override fun 削除(タスクID: タスクID): タスクEntity {
        val timelineId = rtmRepository.createTimeline(token)
        val result = rtmRepository.delete(token, timelineId, タスクIDConverter.createTaskIdSet(タスクID)).response
        return convertTaskSeriesEntityToタスクEntity(result);
    }
}

class タスク更新RepositoryWithRTM(
        val token: Token,
        val taskIdSet: TaskIdSet,
        val タスクRepositoryWithRTM: タスクRepositoryWithRTM,
        val rtmRepository: RtmRepository
): タスク更新Repository {

    override fun タスク消化予定日変更(タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity {
        val res = rtmRepository.updateStartDateTime(
                token,
                rtmRepository.createTimeline(token),
                taskIdSet,
                タスク消化予定日Optional.get().map { TaskStartDateTime(it.localDate.atStartOfDay()) }
        )

        return タスクRepositoryWithRTM.convertTaskSeriesEntityToタスクEntity(res.response)
    }

    override fun リネーム(タスク名: タスク名): タスクEntity {
        throw NotImplementedError()
    }

    override fun タスクDONE(): タスクEntity {
        val res = rtmRepository.completeTask(
                token,
                rtmRepository.createTimeline(token),
                taskIdSet
        )
        return タスクRepositoryWithRTM.convertTaskSeriesEntityToタスクEntity(res.response)
    }

}
