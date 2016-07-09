package com.naosim.someapp.infra.api.lib

interface RequestParamRegex {
    val value: String
}

enum class RequestParamRegexEnum(override val value: String): RequestParamRegex {
    date("[0-9]{4}-[0-9]{2}-[0-9]{2}");
}