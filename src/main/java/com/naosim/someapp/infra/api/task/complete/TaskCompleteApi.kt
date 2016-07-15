package com.naosim.someapp.infra.api.task.complete

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスクEntity
import com.naosim.someapp.domain.タスクID
import com.naosim.someapp.domain.タスク名
import com.naosim.someapp.domain.タスク消化予定日Optional
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import com.naosim.someapp.infra.api.lib.RequiredParam
import com.naosim.someapp.infra.api.task.add.TaskAddRequest
import java.util.function.Function

class TaskCompleteApi(val repositoryFactory: RepositoryFactory): Api<TaskCompleteRequest> {
    val mapConvertor = MapConverter()
    override val description = "タスク完了"
    override val path = "/task/complete"
    override val requestParams = TaskCompleteRequest()
    override val ok: (TaskCompleteRequest) -> Any =  {
        val タスクEntity: タスクEntity = completeTask(it.token.get(), it.taskId.get())
        mapConvertor.apiOkResult(listOf(mapConvertor.toMap(タスクEntity)))
    }

    fun completeTask(token: Token, タスクID: タスクID): タスクEntity {
        return repositoryFactory.createタスクRepository(token).完了(タスクID)
    }

}

class TaskCompleteRequest : ApiRequestParams<TaskCompleteRequest>() {
    @JvmField
    val taskId = RequiredParam<タスクID>("task_id", Function { タスクID(it) })

    @JvmField
    val token = RequiredParam<Token>("token", Function { Token(it) })
}