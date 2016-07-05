package com.naosim.someapp.infra.datasource

import com.naosim.someapp.domain.*
import junit.framework.TestCase
import org.junit.Ignore
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class タスクRepositoryOnMemoryTest(name: String?) : TestCase(name) {
    lateinit var sut: タスクRepository

    override fun setUp() {
        super.setUp()
        sut = タスクRepositoryOnMemory();
    }

    @Test
    fun test() {
        val listBefore = sut.すべてのタスク取得()
        assertEquals("初期状態", 0, listBefore.size)
        sut.追加(タスク名("hoge"), タスク消化予定日NotExist())

        val listAfter = sut.すべてのタスク取得()
        assertEquals("タスク追加", 1, listAfter.size)
        assertEquals("リストはイミュータブル", 0, listBefore.size)

        val タスクEntity = listAfter.get(0)
        val タスクID = タスクEntity.タスクID
        assertEquals("追加したタスク名", "hoge", タスクEntity.タスク名.value)
        val リネームされたタスクEntity = タスクEntity.タスク更新.リネーム(タスク名("foo"))
        assertEquals("変更したタスク名", "foo", リネームされたタスクEntity.タスク名.value)
        assertEquals("タスクEntityはイミュータブル", "hoge", タスクEntity.タスク名.value)

        assertEquals("リポジトリから再取得しても名前が変わってる", "foo", sut.すべてのタスク取得().get(0).タスク名.value)

        assertEquals("タスク状態変更前", タスク状態.BACKLOG, sut.すべてのタスク取得().get(0).タスク状態(LocalDateTime.now()))
        リネームされたタスクEntity.タスク更新.タスクDONE()
        assertEquals("タスク状態変更後", タスク状態.DONE, sut.すべてのタスク取得().get(0).タスク状態(LocalDateTime.now()))
    }

    @Test
    fun testentity() {
        assertEquals("初期状態", 0, sut.すべてのタスク取得().size)
        sut.追加(タスク名("hoge"), タスク消化予定日NotExist())

        assertEquals("タスク状態変更前", タスク状態.BACKLOG, findEntity().タスク状態(LocalDateTime.now()))
        findEntity().タスク更新.タスク消化予定日変更(タスク消化予定日(LocalDate.now().plusDays(1)))
        assertEquals("明日開始の場合", タスク状態.TODO, findEntity().タスク状態(LocalDateTime.now()))
        assertEquals("既に開始日を過ぎている", タスク状態.DOING, findEntity().タスク状態(LocalDateTime.now().plusDays(1)))
        findEntity().タスク更新.タスクDONE()
        assertEquals("まだDOING", タスク状態.DOING, findEntity().タスク状態(LocalDateTime.now().minusDays(1)))
        assertEquals("DONE", タスク状態.DONE, findEntity().タスク状態(LocalDateTime.now().plusDays(1)))
    }

    @Test
    fun testdate() {
        assertEquals("2016-01-02", タスク消化予定日(LocalDate.of(2016, 1, 2)).format)
    }

    fun findEntity(): タスクEntity {
        return sut.すべてのタスク取得().get(0)
    }
}