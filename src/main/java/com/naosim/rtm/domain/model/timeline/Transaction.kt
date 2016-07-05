package com.naosim.rtm.domain.model.timeline

import com.naosim.rtm.domain.model.RtmParamValueObject

class TimelineId(override val rtmParamValue: String) : RtmParamValueObject {}
class TransactionId(override val rtmParamValue: String) : RtmParamValueObject {}
class Undoable(override val rtmParamValue: String) : RtmParamValueObject {}
class Transaction(val timelineId: TimelineId, val transactionId: TransactionId, val undoable: Undoable) {}
class TransactionalResponse<T>(val transaction: Transaction, val response: T) {}