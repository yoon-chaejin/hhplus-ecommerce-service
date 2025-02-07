package kr.hhplus.be.server.common.utils

import java.time.LocalDateTime

object LocalDateTimeTruncator {
    fun truncateToNearestFiveMinutes(time: LocalDateTime): LocalDateTime {
        val truncatedMinute = (time.minute / 5) * 5
        return time.withMinute(truncatedMinute).withSecond(0).withNano(0)
    }
}