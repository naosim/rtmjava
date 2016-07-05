package com.naosim.someapp.infra.api

import spark.Route

interface Api {
    val path: String
    val route: Route
}