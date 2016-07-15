package com.naosim.someapp.infra.api

import com.naosim.someapp.infra.api.lib.Api
import com.naosim.someapp.infra.api.lib.ApiRequestParams
import java.util.function.Function

class HelloApi() : Api<HelloRequest> {
    override val description = ""
    override val path = "/hello"
    override val requestParams = HelloRequest()
    override val ok: (HelloRequest) -> Any = { it.name.get().value + "" + it.enddate.get().isExist }

}