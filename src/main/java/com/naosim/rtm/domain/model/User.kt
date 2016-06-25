package com.naosim.rtm.domain.model

class UserId(override val rtmParamValue: String) : RtmParamValueObject {}
class UserName(override val rtmParamValue: String) : RtmParamValueObject {}
class UserFullName(override val rtmParamValue: String) : RtmParamValueObject {}
class User(val userId: UserId, val userName: UserName, val userFullName: UserFullName){}