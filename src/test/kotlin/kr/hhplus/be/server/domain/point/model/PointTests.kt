package kr.hhplus.be.server.domain.point.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import kr.hhplus.be.server.point.model.Point
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PointTests {

    @Nested
    @DisplayName("포인트 생성 테스트")
    inner class ConstructorTests {
        @Test
        fun `(실패) 잔액이 최솟값 미만인 경우, IllegalArgumentException이 발생한다`() {
            assertFailsWith<IllegalArgumentException> {
                Point(
                    id = 0L,
                    userId = 1L,
                    balance = Point.MIN_BALANCE-1,
                )
            }
        }

        @Test
        fun `(실패) 잔액이 최댓값 초과인 경우, IllegalArgumentException이 발생한다`() {
            assertFailsWith<IllegalArgumentException> {
                Point(
                    id = 0L,
                    userId = 1L,
                    balance = Point.MAX_BALANCE+1,
                )
            }
        }
    }

    @Nested
    @DisplayName("포인트 충전 테스트")
    inner class PlusTests {
        @Test
        fun `(실패) 충전 금액이 최솟값 미만인 경우, CustomException이 발생한다`() {
            //given
            val amount = Point.MIN_AMOUNT-1
            val point = Point(
                id = 0L,
                userId = 1L,
                balance = 0,
            )

            //when
            val result = assertFailsWith<CustomException> {
                point.plus(amount)
            }

            //then
            assertEquals(CustomExceptionType.INVALID_CHARGE_AMOUNT, result.type)
        }

        @Test
        fun `(실패) 충전 금액이 최댓값 초과인 경우, CustomException이 발생한다`() {
            //given
            val amount = Point.MAX_AMOUNT+1
            val point = Point(
                id = 0L,
                userId = 1L,
                balance = 0,
            )

            //when
            val result = assertFailsWith<CustomException> {
                point.plus(amount)
            }

            //then
            assertEquals(CustomExceptionType.INVALID_CHARGE_AMOUNT, result.type)
        }

        @Test
        fun `(실패) 잔액이 1이고 충전 후 잔액이 백만 초과인 경우, CustomException이 발생한다`() {
            //given
            val point = Point(
                id = 0L,
                userId = 1L,
                balance = 1,
            )

            //when
            val result = assertFailsWith<CustomException> {
                point.plus(Point.MAX_BALANCE - point.balance + 1)
            }

            //then
            assertEquals(CustomExceptionType.INVALID_BALANCE, result.type)
        }
    }

    @Nested
    @DisplayName("포인트 차감 테스트")
    inner class MinusTests {
        @Test
        fun `(실패) 차감 금액이 0이하인 경우, IllegalArgumentException이 발생한다`() {
            //given
            val amount = 0
            val point = Point(
                id = 0L,
                userId = 1L,
                balance = 1,
            )

            //when

            //then
            assertFailsWith<IllegalArgumentException> {
                point.minus(amount)
            }
        }

        @Test
        fun `(실패) 차감 금액이 잔액보다 큰 경우, CustomException이 발생한다`() {
            //given
            val balance = 100
            val amount = balance + 1

            val point = Point(
                id = 0L,
                userId = 1L,
                balance = balance,
            )

            //when
            val result = assertFailsWith<CustomException> {
                point.minus(amount)
            }

            //then
            assertEquals(CustomExceptionType.NOT_ENOUGH_POINT, result.type)
        }
    }
}