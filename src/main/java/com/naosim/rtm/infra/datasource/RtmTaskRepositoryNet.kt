package com.naosim.rtm.infra.datasource

import com.naosim.rtm.domain.model.Filter
import com.naosim.rtm.domain.model.RtmParamValueObject
import com.naosim.rtm.domain.model.auth.Token
import com.naosim.rtm.domain.model.task.*
import com.naosim.rtm.domain.model.timeline.*
import com.naosim.rtm.domain.repository.RtmTaskRepository
import com.naosim.rtm.infra.datasource.common.RtmMethod
import com.naosim.rtm.infra.datasource.common.RtmParam
import com.naosim.rtm.infra.datasource.common.RtmRequestUtil
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
        return rtmRequestUtil.convertToList(response.rootElement.getElementsByTagName("list")).map {
            createTaskSeriesListEntity(it)
        }
    }

    override fun addTask(token: Token, timelineId: TimelineId, name: TaskSeriesName/*, parse: Optional<Parse> うまく動作しない */): TransactionalResponse<TaskSeriesEntity> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.tasks_add)
        rtmParams.put(RtmParam.auth_token, token)
        rtmParams.put(RtmParam.timeline, timelineId)
        rtmParams.put(RtmParam.name_, name)
//        parse.ifPresent { rtmParams.put(RtmParam.parse, it) }

        val response = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.code + " " + response.failedResponse!!.msg)
        }

        val transactionElement = response.getFirstElementByTagName("transaction")

        return TransactionalResponse(
                Transaction(timelineId, TransactionId(transactionElement.getAttribute("id")), Undoable(transactionElement.getAttribute("undoable"))),
                createTaskSeriesListEntity(response.getFirstElementByTagName("list")).taskSeriesEntityList.get(0))
    }

    override fun completeTask(token: Token, timelineId: TimelineId, taskIdSet: TaskIdSet): TransactionalResponse<TaskSeriesEntity> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.tasks_complete)
        rtmParams.put(RtmParam.auth_token, token)
        rtmParams.put(RtmParam.timeline, timelineId)
        rtmParams.put(RtmParam.list_id, taskIdSet.listId)
        rtmParams.put(RtmParam.taskseries_id, taskIdSet.taskSeriesId)
        rtmParams.put(RtmParam.task_id, taskIdSet.taskId)

        val response = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.code + " " + response.failedResponse!!.msg)
        }

        val transactionElement = response.getFirstElementByTagName("transaction")

        return TransactionalResponse(
                Transaction(timelineId, TransactionId(transactionElement.getAttribute("id")), Undoable(transactionElement.getAttribute("undoable"))),
                createTaskSeriesListEntity(response.getFirstElementByTagName("list")).taskSeriesEntityList.get(0))
    }

    override fun updateStartDateTime(token: Token, timelineId: TimelineId, taskIdSet: TaskIdSet, startDateTime: Optional<TaskStartDateTime>): TransactionalResponse<TaskSeriesEntity> {
        val rtmParams = HashMap<RtmParam, RtmParamValueObject>()
        rtmParams.put(RtmParam.method, RtmMethod.tasks_setstartdate)
        rtmParams.put(RtmParam.auth_token, token)
        rtmParams.put(RtmParam.timeline, timelineId)
        rtmParams.put(RtmParam.list_id, taskIdSet.listId)
        rtmParams.put(RtmParam.taskseries_id, taskIdSet.taskSeriesId)
        rtmParams.put(RtmParam.task_id, taskIdSet.taskId)
        startDateTime.ifPresent { rtmParams.put(RtmParam.start, it) }


        val response = rtmRequestUtil.requestXML(rtmParams).body
        if(response.isFailed) {
            throw RuntimeException(response.failedResponse!!.code + " " + response.failedResponse!!.msg)
        }

        val transactionElement = response.getFirstElementByTagName("transaction")

        return TransactionalResponse(
                Transaction(timelineId, TransactionId(transactionElement.getAttribute("id")), Undoable(transactionElement.getAttribute("undoable"))),
                createTaskSeriesListEntity(response.getFirstElementByTagName("list")).taskSeriesEntityList.get(0))
    }

    fun createTaskSeriesListEntity(listElement: Element): TaskSeriesListEntity {
        val taskSeriesListId = TaskSeriesListId(listElement.getAttribute("id"))

        return TaskSeriesListEntity(
                taskSeriesListId,
                Optional.ofNullable(listElement.getAttribute("name")).filter { it.isNotEmpty() }.map { TaskSeriesListName(it) },
                rtmRequestUtil.convertToList(listElement.getElementsByTagName("taskseries")).map { createTaskSeriesEntity(taskSeriesListId, it) }
        )
    }

    fun createTaskSeriesEntity(taskSeriesListId: TaskSeriesListId, taskSeriesElement: Element): TaskSeriesEntity {
        val taskSeriesId = TaskSeriesId(taskSeriesElement.getAttribute("id"))

        val taskElement: Element = taskSeriesElement.getElementsByTagName("task").item(0) as Element
        val taskId = TaskId(taskElement.getAttribute("id"))
        val taskIdSet = TaskIdSet(taskSeriesListId, taskSeriesId, taskId);

        val taskEntity = TaskEntity(
                taskIdSet,
                TaskDue(taskElement.getAttribute("due")),
                TaskHasDueTime(taskElement.getAttribute("has_due_time")),
                TaskDateTimes(
                        TaskAddedDateTime(rtmRequestUtil.createLocalDateTime(taskElement.getAttribute("added"))),
                        Optional.ofNullable(taskElement.getAttribute("completed")).filter { it.isNotEmpty() }.map { rtmRequestUtil.createLocalDateTime(it) }.map { TaskCompletedDateTime(it) },
                        Optional.ofNullable(taskElement.getAttribute("deleted")).filter { it.isNotEmpty() }.map { rtmRequestUtil.createLocalDateTime(it) }.map { TaskDeletedDateTime(it) },
                        Optional.ofNullable(taskElement.getAttribute("start")).filter { it.isNotEmpty() }.map { rtmRequestUtil.createLocalDateTime(it) }.map { TaskStartDateTime(it) }
                ),
                TaskPostponed(taskElement.getAttribute("postponed")),
                TaskEstimate(taskElement.getAttribute("estimate"))
        )
        return TaskSeriesEntity(
                taskIdSet,
                TaskSeriesName(taskSeriesElement.getAttribute("name")),
                taskEntity,
                ArrayList(), // todo tag
                TaskSeriesSource(taskSeriesElement.getAttribute("source")),
                TaskSeriesLocationid(taskSeriesElement.getAttribute("location_id")),
                TaskSeriesDateTimes(
                        TaskSeriesCreatedDateTime(rtmRequestUtil.createLocalDateTime(taskSeriesElement.getAttribute("created"))),
                        TaskSeriesModifiedDateTime(rtmRequestUtil.createLocalDateTime(taskSeriesElement.getAttribute("modified")))
                )
        )
    }
}