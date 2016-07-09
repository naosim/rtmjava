package com.naosim.someapp.infra.api.lib

import spark.Request
import spark.Response
import java.util.function.Function

interface Api<T: ApiRequestParams<T>> {
    val description: String
    val path: String
    val requestParams: T
    fun router(req: Request, res: Response):Any {
        return requestParams.apply(req.queryMap()).valid(
                Function { ok(it) },
                Function {
                    res.status(400)
                    throw RuntimeException(it.map{ it.message }.joinToString())
                }
        )
    }

    val ok: (T) -> Any;
}