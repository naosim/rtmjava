package com.naosim.someapp.infra.api.task.add

import com.naosim.rtm.domain.model.auth.Token
import com.naosim.someapp.domain.タスク名
import com.naosim.someapp.domain.タスク消化予定日
import com.naosim.someapp.domain.タスク消化予定日NotExist
import com.naosim.someapp.domain.タスク消化予定日Optional
import com.naosim.someapp.infra.api.lib.*
import com.naosim.someapp.infra.api.lib.RequestParamRegexEnum.date
import java.time.LocalDate
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

class TaskAddRequest : ApiRequestParams<TaskAddRequest>() {
    @JvmField
    val name = RequiredParam<タスク名>("name", Function { タスク名(it) })

    @JvmField
    val token = RequiredParam<Token>("token", Function { Token(it) })

    @JvmField
    val enddate = OptionParam<タスク消化予定日Optional>(
            "enddate",
            date,
            Function { タスク消化予定日(LocalDate.parse(it)) },
            Supplier<タスク消化予定日Optional> { タスク消化予定日NotExist() }
    )
}