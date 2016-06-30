package com.naosim.rtm.infra.datasource.common

import com.naosim.rtm.domain.model.RtmParamValueObject

enum class RtmMethod(override val rtmParamValue: String) : RtmParamValueObject {
    auth_getfrob("rtm.auth.getFrob"),
    auth_gettoken("rtm.auth.getToken"),
    auth_checktoken("rtm.auth.checkToken"),
    tasks_getlist("rtm.tasks.getList"),
    tasks_add("rtm.tasks.add"),
    timelines_create("rtm.timelines.create")
    ;
}