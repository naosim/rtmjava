package com.naosim.someapp.infra.datasource

import com.naosim.someapp.domain.*

class タスクEntityImpl(
        override val タスクID: タスクID,
        override val タスク更新: タスク更新Repository,
        override val タスク名: タスク名,
        override val タスク消化予定日Optional: タスク消化予定日Optional,
        override val タスク完了日Optional: タスク完了日Optional,
        override val タスク削除日Optional: タスク削除日Optional
) : タスクEntity {

}
