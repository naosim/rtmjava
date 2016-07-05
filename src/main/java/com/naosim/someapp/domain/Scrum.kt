package com.naosim.someapp.domain

import com.naosim.lib.ddd.valueobject.IsExist
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


interface タスク {
    val タスク名: タスク名
    val タスク消化予定日Optional: タスク消化予定日Optional
    val タスク完了日Optional: タスク完了日Optional

    fun タスク状態(localDateTime: LocalDateTime): タスク状態 {
        if(this.タスク完了日Optional.isBefore(localDateTime)) {
            return タスク状態.DONE
        }
        if(this.タスク消化予定日Optional.isBefore(localDateTime)) {
            return タスク状態.DOING
        }
        if(this.タスク消化予定日Optional.isExist) {
            return タスク状態.TODO
        }
        return タスク状態.BACKLOG
    }
}

interface タスクEntity : タスク {
    val タスクID: タスクID
    val タスク更新: タスク更新Repository
}

interface タスク更新Repository {
    fun リネーム(タスク名: タスク名): タスクEntity;
    fun タスクDONE(): タスクEntity;
    fun タスク消化予定日変更(タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity;
}

interface タスクRepository {
    fun 追加(タスク名: タスク名, タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity;
    fun すべてのタスク取得(): List<タスクEntity>
}



open class タスクID(val value: String)
class タスク名(val value: String)

interface タスク消化予定日Optional : IsExist<タスク消化予定日Optional, タスク消化予定日> {
    fun isBefore(localDateTime: LocalDateTime): Boolean {
        return compareIfExist({ it.localDate.atStartOfDay().isBefore(localDateTime)})
    }
}

class タスク消化予定日(val localDate: LocalDate): タスク消化予定日Optional {
    val format = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    override val isExist: Boolean = true
}

class タスク消化予定日NotExist : タスク消化予定日Optional {
    override val isExist: Boolean = false
}

interface タスク完了日Optional : IsExist<タスク完了日Optional, タスク完了日> {
    fun isBefore(localDateTime: LocalDateTime): Boolean {
        return compareIfExist({
            it.localDate.atStartOfDay().isBefore(localDateTime)
        })
    }
}

class タスク完了日(val localDate: LocalDate): タスク完了日Optional {
    override val isExist: Boolean = true
}

class タスク完了日NotExist : タスク完了日Optional {
    override val isExist: Boolean = false
}

enum class タスク状態 {
    BACKLOG,//開始日が無い
    TODO,// 開始前
    DOING,// 開始予定日後、完了前
    DONE// 完了
}
