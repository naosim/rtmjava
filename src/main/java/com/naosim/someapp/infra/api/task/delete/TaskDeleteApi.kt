package com.naosim.someapp.infra.api.task.complete

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスクEntity
import com.naosim.someapp.domain.タスクID
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import com.naosim.someapp.infra.api.lib.RequiredParam
import java.util.function.Function

class TaskDeleteApi(val repositoryFactory: RepositoryFactory): Api<TaskDeleteRequest> {
    val mapConvertor = MapConverter()
    override val description = "タスク削除"
    override val path = "/task/delete"
    override val requestParams = TaskDeleteRequest()
    override val ok: (TaskDeleteRequest) -> Any =  {
        val タスクEntity: タスクEntity = delete(it.token.get(), it.taskId.get())
        mapConvertor.apiOkResult(listOf(mapConvertor.toMap(タスクEntity)))
    }

    fun delete(token: Token, タスクID: タスクID): タスクEntity {
        return repositoryFactory.createタスクRepository(token).削除(タスクID)
    }

}

class TaskDeleteRequest : ApiRequestParams<TaskDeleteRequest>() {
    @JvmField
    val taskId = RequiredParam<タスクID>("task_id", Function { タスクID(it) })

    @JvmField
    val token = RequiredParam<Token>("token", Function { Token(it) })
}