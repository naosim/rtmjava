package com.naosim.rtm.infra.datasource

import com.naosim.rtm.RtmApiConfig
import com.naosim.rtm.domain.model.*
import com.naosim.rtm.domain.model.auth.Token
import org.w3c.dom.Element
import java.util.*

class RtmTaskRepositoryNet(val rtmApiConfig: RtmApiConfig): RtmTaskRepository {
    private val rtmRequestUtil = RtmRequestUtil(rtmApiConfig)

    override fun getTaskList(token: Token, filter: Filter?): List<TaskSeriesListEntity> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.tasks_getlist)
        rtmParams.put(RtmParam.auth_token, token)
        if(filter != null) {
            rtmParams.put(RtmParam.filter, filter)
        }

        val response = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.code + " " + response.failedResponse!!.msg)
        }
        println(response.rootElement.getElementsByTagName("list"))
        val taskSeriesListEntityList = rtmRequestUtil.convertToList(response.rootElement.getElementsByTagName("list")).map {
            val taskSeriesListId = TaskSeriesListId(it.getAttribute("id"))

            TaskSeriesListEntity(
                    taskSeriesListId,
                    Optional.ofNullable(it.getAttribute("name")).filter { it.isNotEmpty() }.map{ TaskSeriesListName(it) },
                    rtmRequestUtil.convertToList(it.getElementsByTagName("taskseries")).map { createTaskSeriesEntity(taskSeriesListId, it) }
            )
        }

        return taskSeriesListEntityList
    }

    fun createTaskSeriesEntity(taskSeriesListId:TaskSeriesListId, taskSeriesElement: Element): TaskSeriesEntity {
        val taskSeriesId = TaskSeriesId(taskSeriesElement.getAttribute("id"))

        val taskElement: Element = taskSeriesElement.getElementsByTagName("task").item(0) as Element
        val taskId = TaskId(taskElement.getAttribute("id"))
        val taskIdSet = TaskIdSet(taskSeriesListId, taskSeriesId, taskId);

        val taskEntity = TaskEntity(
                taskIdSet,
                TaskDue(taskElement.getAttribute("due")),
                TaskHasDueTime(taskElement.getAttribute("has_due_time")),
                TaskDateTimes(
                        TaskAddedDateTimes(rtmRequestUtil.createLocalDateTime(taskElement.getAttribute("added"))),
                        Optional.ofNullable(taskElement.getAttribute("completed")).filter { it.isNotEmpty() }.map { rtmRequestUtil.createLocalDateTime(it) }.map { TaskCompletedDateTimes(it) },
                        Optional.ofNullable(taskElement.getAttribute("deleted")).filter { it.isNotEmpty() }.map { rtmRequestUtil.createLocalDateTime(it) }.map { TaskDeletedDateTimes(it) }
                ),
                TaskPostponed(taskElement.getAttribute("postponed")),
                TaskEstimate(taskElement.getAttribute("estimate"))
        )
        return TaskSeriesEntity(
                taskIdSet,
                TaskSeriesName(taskSeriesElement.getAttribute("name")),
                taskEntity,
                ArrayList(),// todo tag
                TaskSeriesSource(taskSeriesElement.getAttribute("source")),
                TaskSeriesLocationid(taskSeriesElement.getAttribute("location_id")),
                TaskSeriesDateTimes(
                        TaskSeriesCreatedDateTime(rtmRequestUtil.createLocalDateTime(taskSeriesElement.getAttribute("created"))),
                        TaskSeriesModifiedDateTime(rtmRequestUtil.createLocalDateTime(taskSeriesElement.getAttribute("modified")))
                )
        )
    }
}