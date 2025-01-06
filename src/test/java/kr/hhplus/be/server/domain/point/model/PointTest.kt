package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.point.model.Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class PointTest () {

    @Test
    fun `given when 포인트 생성 시 잔액이 0 미만 이면 then IllegalArgumentException이 발생한다`() {
        //given

        //when

        //then
        assertFailsWith<IllegalArgumentException> { Point(
            id = 0L,
            userId = 1L,
            balance = -1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ) }
    }

    @Test
    fun `given when 포인트 생성 시 잔액이 백만 초과 이면 then IllegalArgumentException이 발생한다`() {
        //given

        //when

        //then
        assertFailsWith<IllegalArgumentException> { Point(
            id = 0L,
            userId = 1L,
            balance = 1_000_001,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ) }
    }

    @Test
    fun `given when 포인트 충전 시 충전 금액이 0 이하이면 then IllegalArgumentException이 발생한다`() {
        //given
        val amount = 0
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when

        //then
        assertFailsWith<IllegalArgumentException> {
            point.plus(amount)
        }
    }

    @Test
    fun `given when 포인트 충전 시 충전 금액이 백만 초과이면 then IllegalArgumentException이 발생한다`() {
        //given
        val amount = 1_000_001
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when

        //then
        assertFailsWith<IllegalArgumentException> {
            point.plus(amount)
        }
    }

    @Test
    fun `given 잔액이 1이고 when 충전 후 잔액이 백만 초과이면 then CustomeException이 발생한다`() {
        //given
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when

        //then
        assertFailsWith<CustomException>() {
            point.plus(1_000_000)
        }
    }

}