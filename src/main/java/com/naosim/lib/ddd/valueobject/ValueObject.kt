package com.naosim.lib.ddd.valueobject

import java.util.*

interface IsExist<SUPER, EXIST: SUPER> {
    val isExist: Boolean;
    fun isNotExist(): Boolean {
        return !isExist
    }
    fun get(): Optional<EXIST> {
        return if(isExist) Optional.of(this as EXIST) else Optional.empty<EXIST>()
    }
    fun compareIfExist(predicate: (obj: EXIST) -> Boolean): Boolean {
        if(isNotExist()) {
            return false;
        }
        val obj = this as EXIST
        return predicate.invoke(obj)
    }
}