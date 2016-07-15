package com.naosim.someapp.infra

import com.naosim.someapp.domain.タスクEntity
import java.time.format.DateTimeFormatter

class MapConverter {
    fun toMap(タスクEntity: タスクEntity): Map<String, Any> {
        return hashMapOf(
                "task_id" to タスクEntity.タスクID.value,
                "task_name" to タスクEntity.タスク名.value,
                "task_end_date_optional" to タスクEntity.タスク消化予定日Optional.get().map { it.localDate.format(DateTimeFormatter.ISO_DATE) }.orElse(null),
                "task_completed_date_optional" to タスクEntity.タスク完了日Optional.get().map { it.localDate.format(DateTimeFormatter.ISO_DATE) }.orElse(null),
                "task_deleted_date_optional" to タスクEntity.タスク削除日Optional.get().map { it.localDate.format(DateTimeFormatter.ISO_DATE) }.orElse(null)
        )
    }

    fun apiOkResult(detail: List<Map<String, Any>>): Map<String, Any> {
        return hashMapOf(
                "header" to hashMapOf(
                        "code" to 200,
                        "status" to "ok",
                        "message" to "ok"
                ),
                "detail" to detail
        )
    }

    fun apiOkResult(onedetail: Map<String, Any>): Map<String, Any> {
        return hashMapOf(
                "header" to hashMapOf(
                        "code" to 200,
                        "status" to "ok",
                        "message" to "ok"
                ),
                "detail" to listOf<Map<String, Any>>(onedetail)
        )
    }

    fun apiErrorResult(code: Int, message: String): Map<String, Any> {
        return hashMapOf(
                "header" to hashMapOf(
                        "code" to code,
                        "status" to "ng",
                        "message" to message
                )
        )
    }
}

