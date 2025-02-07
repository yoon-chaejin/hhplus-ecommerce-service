package kr.hhplus.be.server.domain.product.model

import kr.hhplus.be.server.common.exception.CustomException
import kr.hhplus.be.server.common.exception.CustomExceptionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ProductTest {

    @Nested
    @DisplayName("상품 생성 테스트")
    inner class ConstructorTests {
        @Test
        fun `(실패) 상품 가격이 100 단위가 아닌 경우, IllegalArgumentException이 발생한다`() {
            //given
            val unitPrice = 150

            //then
            assertFailsWith<IllegalArgumentException>() {
                Product(
                    id = 1L,
                    name = "상품명",
                    remainingQuantity = 0,
                    unitPrice = unitPrice,
                )
            }
        }

        @Test
        fun `(성공) 상품 가격이 100 단위인 경우, 상품을 생성한다`() {
            //given
            val unitPrice = 100

            //when
            val result = Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 0,
                unitPrice = unitPrice,
            )

            //then
            assertEquals(unitPrice, result.unitPrice)
        }
    }

    @Nested
    @DisplayName("상품 상태 조회 테스트")
    inner class GetStatusTests {
        @Test
        fun `(성공) 상품의 잔여 수량이 0 이하인 경우, 상품 상태는 UNAVAILABLE을 반환한다`() {
            //given
            val product = Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 0,
                unitPrice = 100,
            )

            //when
            val result = product.getStatus()

            //then
            assertEquals(ProductStatus.UNAVAILABLE, result)
        }

        @Test
        fun `(성공) 상품의 잔여 수량이 0 초과인 경우, 상품 상태는 AVAILABLE을 반환한다`() {
            //given
            val product = Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = 50,
                unitPrice = 100,
            )

            //when
            val result = product.getStatus()

            //then
            assertEquals(ProductStatus.AVAILABLE, result)
        }
    }

    @Nested
    @DisplayName("상품 수량 감소 테스트")
    inner class DecreaseQuantityByTests {
        @Test
        fun `(실패) 잔여 수량을 초과한 경우, CustomException이 발생한다`() {
            //given
            val remainingQuantity = 5
            val amount = remainingQuantity + 1
            val product = Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = remainingQuantity,
                unitPrice = 100,
            )

            //when
            val result = assertFailsWith<CustomException>() {
                product.decreaseQuantityBy(amount)
            }
            //then
            assertEquals(CustomExceptionType.NOT_ENOUGH_QUANTITY, result.type)
        }

        @Test
        fun `(성공) 잔여 수량을 초과하지 않은 경우, 상품의 수량이 감소된다`() {
            //given
            val remainingQuantity = 5
            val amount = remainingQuantity
            val product = Product(
                id = 1L,
                name = "상품명",
                remainingQuantity = remainingQuantity,
                unitPrice = 100,
            )

            //when
            val result = product.decreaseQuantityBy(amount)

            //then
            assertEquals(remainingQuantity - amount, result.remainingQuantity)
        }
    }
}