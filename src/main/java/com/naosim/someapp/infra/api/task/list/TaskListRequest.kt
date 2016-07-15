package com.naosim.someapp.infra.api.task.list

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import com.naosim.someapp.infra.api.lib.RequiredParam
import java.util.function.Function

class TaskListRequest : ApiRequestParams<TaskListRequest>() {
    @JvmField
    val token = RequiredParam<Token>("token", Function { Token(it) })
}