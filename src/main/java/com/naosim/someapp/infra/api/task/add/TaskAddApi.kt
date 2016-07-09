package com.naosim.someapp.infra.api.task.add

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスクEntity
import com.naosim.someapp.domain.タスク名
import com.naosim.someapp.domain.タスク消化予定日Optional
import com.naosim.someapp.infra.MapConverter
import com.naosim.someapp.infra.RepositoryFactory
import com.naosim.someapp.infra.api.lib.Api
import java.util.function.Function

class TaskAddApi(val repositoryFactory: RepositoryFactory): Api<TaskAddRequest> {
    val mapConvertor = MapConverter()
    override val description = "タスク追加"
    override val path = "/task/add"
    override val requestParams = TaskAddRequest()
    override val ok: (TaskAddRequest) -> Any =  {
        val タスクEntity: タスクEntity = addTask(it.token.get(), it.name.get(), it.enddate.get())
        mapConvertor.apiOkResult(listOf(mapConvertor.toMap(タスクEntity)))
    }

    fun addTask(token: Token, タスク名: タスク名, タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity {
        return repositoryFactory.createタスクRepository(token).追加(タスク名, タスク消化予定日Optional)
    }

}