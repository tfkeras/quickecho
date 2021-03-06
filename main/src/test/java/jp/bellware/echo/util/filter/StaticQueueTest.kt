package jp.bellware.echo.util.filter

import io.kotlintest.shouldBe
import org.junit.Test

/**
 * 一定のサイズを超えないキューのテスト
 */
class StaticQueueTest {
    @Test
    fun test() {
        val queue = StaticQueue(3, 0)
        queue.size shouldBe 0
        //
        queue.add(1)
        queue.size shouldBe 1
        queue[0] shouldBe 1
        //
        queue.add(2)
        queue.size shouldBe 2
        queue[0] shouldBe 2
        queue[1] shouldBe 1
        //
        queue.add(3)
        queue.size shouldBe 3
        queue[0] shouldBe 3
        queue[1] shouldBe 2
        queue[2] shouldBe 1
        // 4番目の値を入れると、1番目がなくなる
        queue.add(4)
        queue.size shouldBe 3
        queue[0] shouldBe 4
        queue[1] shouldBe 3
        queue[2] shouldBe 2
    }
}
