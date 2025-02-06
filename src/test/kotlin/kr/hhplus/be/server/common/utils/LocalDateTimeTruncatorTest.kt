package kr.hhplus.be.server.common.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class LocalDateTimeTruncatorTest {
    val sut = LocalDateTimeTruncator

    @Test
    fun `시간이 주어지면, 5분 단위 기준으로 최근의 시간을 반환한다`() {
        val now = LocalDateTime.of(2025, 2, 1, 10, 24, 37)
        val expected = LocalDateTime.of(2025, 2, 1, 10, 20, 0)

        val result = sut.truncateToNearestFiveMinutes(now)

        assertEquals(expected, result)
    }
}