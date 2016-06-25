package com.naosim.rtm.domain.model.auth

import com.naosim.rtm.domain.model.RtmParamValueObject

open class Frob(override val rtmParamValue: String) : RtmParamValueObject {}
class FrobUnAuthed(override val rtmParamValue: String) : Frob(rtmParamValue) {}
class FrobAuthed(override val rtmParamValue: String) : Frob(rtmParamValue) {}
open class Token(override val rtmParamValue: String) : RtmParamValueObject {}
class CheckedToken(override val rtmParamValue: String) : Token(rtmParamValue) {}
enum class RtmPerms : RtmParamValueObject {
    delete;
    override val rtmParamValue: String = name
}