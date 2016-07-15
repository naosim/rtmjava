package com.naosim.someapp.infra.api.task.list

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスクEntity
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api

class TaskListApi(val repositoryFactory: RepositoryFactory): Api<TaskListRequest> {
    val mapConvertor = MapConverter()
    override val description = "タスク取得"
    override val path = "/task/list"
    override val requestParams = TaskListRequest()
    override val ok: (TaskListRequest) -> Any =  {
        val タスクEntityList: List<タスクEntity> = getTaskList(it.token.get())
        mapConvertor.apiOkResult(タスクEntityList.map { mapConvertor.toMap(it) })
    }

    fun getTaskList(token: Token): List<タスクEntity> {
        return repositoryFactory.createタスクRepository(token).すべてのタスク取得()
    }

}