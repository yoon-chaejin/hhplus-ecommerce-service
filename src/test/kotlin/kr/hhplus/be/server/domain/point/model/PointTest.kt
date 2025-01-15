package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.point.model.Point
import kr.hhplus.be.server.point.model.Point.Companion.MAX_BALANCE
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PointTest () {

    @Test
    fun `given 잔액이 최솟값 미만인 when 포인트 생성 시 then IllegalArgumentException이 발생한다`() {
        //given

        //when

        //then
        assertFailsWith<IllegalArgumentException> { Point(
            id = 0L,
            userId = 1L,
            balance = Point.MIN_BALANCE-1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ) }
    }

    @Test
    fun `given 잔액이 최댓값 초과인 when 포인트 생성 시 then IllegalArgumentException이 발생한다`() {
        //given

        //when

        //then
        assertFailsWith<IllegalArgumentException> { Point(
            id = 0L,
            userId = 1L,
            balance = Point.MAX_BALANCE+1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ) }
    }

    @Test
    fun `given 충전 금액이 최솟값 미만이고 when 포인트 충전 시 then CustomException이 발생한다`() {
        //given
        val amount = Point.MIN_AMOUNT-1
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when
        val result = assertFailsWith<CustomException> {
            point.plus(amount)
        }

        //then
        assertEquals(CustomExceptionType.INVALID_CHARGE_AMOUNT, result.type)
    }

    @Test
    fun `given 충전 금액이 백만 초과이고 when 포인트 충전 시 then CustomException이 발생한다`() {
        //given
        val amount = Point.MAX_AMOUNT+1
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when
        val result = assertFailsWith<CustomException> {
            point.plus(amount)
        }

        //then
        assertEquals(CustomExceptionType.INVALID_CHARGE_AMOUNT, result.type)
    }

    @Test
    fun `given 잔액이 1이고 when 충전 후 잔액이 백만 초과이면 then CustomException이 발생한다`() {
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
            point.plus(MAX_BALANCE - point.balance + 1)
        }
    }

    @Test
    fun `given 차감 금액이 0이하이고 when 포인트 차감 시 then IllegalArgumentException이 발생한다`() {
        //given
        val amount = 0
        val point = Point(
            id = 0L,
            userId = 1L,
            balance = 1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when

        //then
        assertFailsWith<IllegalArgumentException> {
            point.minus(amount)
        }
    }

    @Test
    fun `given 차감 금액이 잔액보다 크고 when 포인트 차감 시 then CustomException이 발생한다`() {
        //given
        val balance = 100
        val amount = balance + 1

        val point = Point(
            id = 0L,
            userId = 1L,
            balance = balance,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        //when

        //then
        assertFailsWith<CustomException> {
            point.minus(amount)
        }
    }
}