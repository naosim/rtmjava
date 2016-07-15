package com.naosim.someapp.infra.datasource

import com.naosim.someapp.domain.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class タスクEntityImpl(
        override val タスクID: タスクID,
        override val タスク更新: タスク更新Repository,
        override val タスク名: タスク名,
        override val タスク消化予定日Optional: タスク消化予定日Optional,
        override val タスク完了日Optional: タスク完了日Optional
) : タスクEntity {

}


class タスクRepositoryOnMemory: タスクRepository {
    override fun 完了(タスクID: タスクID): タスクEntity {
        throw UnsupportedOperationException()
    }

    val map = HashMap<String, タスクEntity>();
    override fun 追加(タスク名: タスク名, タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity {
        val タスクID = タスクID("TID" + LocalDateTime.now().toString());

        val タスクEntity = タスクEntityImpl(
                タスクID,
                タスク更新RepositoryOnMemory(タスクID, this),
                タスク名,
                タスク消化予定日Optional,
                タスク完了日NotExist()
        )

        map.put(タスクID.value, タスクEntity)

        return タスクEntity
    }

    fun 追加(タスクEntity: タスクEntity) {
        map.put(タスクEntity.タスクID.value, タスクEntity)
    }

    override fun すべてのタスク取得(): List<タスクEntity> {
        return map.values.toList()
    }

    fun findByタスクID(タスクID: タスクID): Optional<タスクEntity> {
        return Optional.ofNullable(map.get(タスクID.value))
    }
}

class タスク更新RepositoryOnMemory(val タスクID: タスクID, val タスクRepositoryOnMemory: タスクRepositoryOnMemory): タスク更新Repository {
    override fun タスク消化予定日変更(タスク消化予定日Optional: タスク消化予定日Optional): タスクEntity {
        val 古いタスクEntity = タスクRepositoryOnMemory.findByタスクID(タスクID).get()
        val タスクEntity = タスクEntityImpl(
                古いタスクEntity.タスクID,
                タスク更新RepositoryOnMemory(古いタスクEntity.タスクID, タスクRepositoryOnMemory),
                古いタスクEntity.タスク名,
                タスク消化予定日Optional,// 変更
                古いタスクEntity.タスク完了日Optional
        )
        タスクRepositoryOnMemory.追加(タスクEntity)
        return タスクEntity
    }

    override fun リネーム(タスク名: タスク名): タスクEntity {
        val 古いタスクEntity = タスクRepositoryOnMemory.findByタスクID(タスクID).get()
        val タスクEntity = タスクEntityImpl(
                古いタスクEntity.タスクID,
                タスク更新RepositoryOnMemory(古いタスクEntity.タスクID, タスクRepositoryOnMemory),
                タスク名,// 変更
                古いタスクEntity.タスク消化予定日Optional,
                古いタスクEntity.タスク完了日Optional
        )
        タスクRepositoryOnMemory.追加(タスクEntity)
        return タスクEntity
    }

    override fun タスクDONE(): タスクEntity {
        val 古いタスクEntity = タスクRepositoryOnMemory.findByタスクID(タスクID).get()
        val タスクEntity = タスクEntityImpl(
                古いタスクEntity.タスクID,
                タスク更新RepositoryOnMemory(古いタスクEntity.タスクID, タスクRepositoryOnMemory),
                古いタスクEntity.タスク名,
                古いタスクEntity.タスク消化予定日Optional,
                タスク完了日(LocalDate.now())// 変更
        )
        タスクRepositoryOnMemory.追加(タスクEntity)
        return タスクEntity
    }

}

